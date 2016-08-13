package com.dentist.webapp;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dentist.domain.Appointment;
import com.dentist.domain.AppointmentRequest;
import com.dentist.domain.AppointmentRequestStatus;
import com.dentist.domain.AppointmentStatus;
import com.dentist.domain.Patient;
import com.dentist.googlecalendar.CalendarEventHandler;
import com.dentist.mail.EmailGenerator;
import com.dentist.mail.EmailStructure;
import com.dentist.mail.EmailTemplate;
import com.dentist.service.CustomUserDetails;
import com.dentist.service.UserServiceInterface;
import com.dentist.util.WebUtility;
import com.google.api.services.calendar.model.Event;


@RestController
@EnableAsync
@Transactional
@RequestMapping("/appointmentrequests")
public class AppointmentRequestInfoController {

	private static final Logger LOGGER = Logger.getLogger(AppointmentRequestInfoController.class);
	@Autowired
	private UserServiceInterface userServiceInterface;
	@Autowired
	private EmailGenerator emailSender;
	@Autowired
	private EmailStructure emailStructure;
	@Autowired
	@Qualifier("encryptableProps")
	private Properties encryptableProps;
	@Autowired
	private CalendarEventHandler calendarEventHandler;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{appointmentRequestID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AppointmentRequest> getAppointmentRequestByID(@PathVariable("appointmentRequestID") long appointmentRequestID) {
		LOGGER.info("processing get request to /appointmentrequests/{appointmentRequestID}");
		AppointmentRequest appointmentRequest = userServiceInterface.getAppointmentRequestByID(appointmentRequestID);
		return new ResponseEntity<AppointmentRequest>(appointmentRequest, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')") // change to ROLE_ADMIN
	@RequestMapping(value = "/patient/{patientID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentRequest>> getAppointmentRequestsByPatientID(@PathVariable("patientID") long patientID) {
		LOGGER.info("processing get request to /appointmentrequests/patient/{patientID}");
		List<AppointmentRequest> appointmentRequests = userServiceInterface.getAppointmentRequestsByPatientID(patientID);
		return new ResponseEntity<List<AppointmentRequest>>(appointmentRequests, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/allappointmentrequests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getAllAppointmentRequests() {
		LOGGER.info("processing get request to /appointmentrequests/allappointmentrequests");
		Map<String, Object> map = new HashMap<>();
		List<AppointmentRequest> appointmentRequests = userServiceInterface.getAllAppointmentRequests();
		Collections.reverse(appointmentRequests);
		map.put("data", appointmentRequests);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	/*******************************************************
	 * POST API END POINTS TO HANDLE APPOINTMENT REQUESTS FROM PATIENT
	 ******************************************************/

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = {"!status"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> createAppointmentRequest(@RequestParam(name = "note") String note,
			@RequestParam(name = "appointmentdate") String appointmentdate, @RequestParam(name = "appointmenttime") String appointmenttime) {

		Boolean valid = true;
		Map<String, Object> map = new HashMap<>();
		if (note.length() > 1000 || note.isEmpty()) {
			map.put("errorNote", "The note field should contain length between 0 to 1000");
			valid = false;
		}
		DateTime startTime = WebUtility.getDateTimeFromHtmlDate(appointmentdate, appointmenttime);

		if (startTime == null) {
			valid = false;
		}

		if (valid) {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
			Patient patient = userServiceInterface.getPatientInfoById(user.getUserID());

			AppointmentRequest appointmentRequest = new AppointmentRequest();
			appointmentRequest.setAppointmentStartTime(startTime);
			appointmentRequest.setStatus(AppointmentRequestStatus.WAITING_FOR_APPROVAL);
			appointmentRequest.setNote(note);
			appointmentRequest.setRequestInsertedTime(new DateTime());
			appointmentRequest.setAppointmentPatient(patient);
			userServiceInterface.setAppointmentRequest(appointmentRequest);

			// Prepare and send appointment request created email to admin
			Map<String, Object> emailMap = new HashMap<String, Object>();
			emailMap.put("user", "Admin");
			emailMap.put("appointmentRequest", appointmentRequest);

			String body = emailSender.prepareBody(EmailTemplate.APPOINTMENTREQUEST_CREATED, emailMap);
			emailStructure.setBody(body);
			emailStructure.setSenderEmail(encryptableProps.getProperty("email.id"));
			emailStructure.setSubject("New Appointment Request");
			emailStructure.addRecipient(encryptableProps.getProperty("admin.email"));
			emailSender.sendEmail(emailStructure);

			map.put("Success", "Success");

		}

		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/update", method = RequestMethod.POST, params = {"status=CANCEL"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> cancelAppointmentRequest(@RequestParam("appointmentRequestID") long appointmentRequestID) {
		Map<String, String> map = new HashMap<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
		AppointmentRequest appointmentRequest = userServiceInterface.getAppointmentRequestByIDandPatientID(appointmentRequestID, user.getUserID());
		if (appointmentRequest != null) {
			if (appointmentRequest.getStatus().equals(AppointmentRequestStatus.WAITING_FOR_APPROVAL)) {
				appointmentRequest.setStatus(AppointmentRequestStatus.CANCELLED);
				map.put("Success", "Success");
			} else {
				map.put("error", "unable to cancel the appointment request");
			}

		} else {
			map.put("error", "Invalid patient ID or appointRequestID ");
		}

		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}

	/*******************************************************
	 * POST API END POINTS TO HANDLE APPOINTMENT REQUESTS FROM ADMIN
	 ******************************************************/

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/update/{patientID}/{appointmentRequestID}", method = RequestMethod.POST, params = {
			"status=ACCEPT"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> acceptAppointmentRequest(@PathVariable("patientID") long patientID,
			@PathVariable("appointmentRequestID") long appointmentRequestID, @RequestParam(name = "note") String note,
			@RequestParam("expectedAmount") BigDecimal expectedAmount) {
		Map<String, Object> map = new HashMap<>();
		AppointmentRequest appointmentRequest = userServiceInterface.getAppointmentRequestByIDandPatientID(appointmentRequestID, patientID);
		if (appointmentRequest != null) {
			appointmentRequest.setStatus(AppointmentRequestStatus.ACCEPTED);
			DateTime startTime = appointmentRequest.getAppointmentStartTime().toDateTime(DateTimeZone.forID("America/New_York"));
			Patient patient = userServiceInterface.getPatientInfoById(patientID);
			Future<Event> actualEvent = calendarEventHandler.insertActualEvent(startTime, patient.getEmail());
			Future<Event> fakeEvent = calendarEventHandler.insertFakeEvent(startTime);
			Appointment appointment = new Appointment();
			try {
				while (!actualEvent.isDone() || !fakeEvent.isDone()) {

					Thread.sleep(10); // 10-millisecond pause between each check

				}
				appointment.setActualCalEventID(actualEvent.get().getId());
				appointment.setFakeCalEventID(fakeEvent.get().getId());
			} catch (InterruptedException | ExecutionException e) {
				LOGGER.error("unable to insert calander event", e);
			}

			appointment.setAppointmentInsertedTime(new DateTime(DateTimeZone.forID("America/New_York")));
			appointment.setAppointmentPatient(patient);
			appointment.setAppointmentRequest(appointmentRequest);
			if (!note.isEmpty()) {
				appointment.setNote(note);
			} else {
				appointment.setNote(appointmentRequest.getNote());
			} ;
			appointment.setExpectedAmount(expectedAmount);
			appointment.setAppointmentStartTime(startTime);
			appointment.setStatus(AppointmentStatus.CONFIRMED);
			userServiceInterface.setAppointment(appointment);

			// Prepare and send an email regarding the acceptance of appointment
			// request.
			Map<String, Object> emailMap = new HashMap<String, Object>();
			emailMap.put("user", patient.getFirstName() + " " + patient.getLastName());
			emailMap.put("appointment", appointment);

			String body = emailSender.prepareBody(EmailTemplate.APPOINTMENT_CONFIRMED, emailMap);
			emailStructure.setBody(body);
			emailStructure.setSenderEmail(encryptableProps.getProperty("email.id"));
			emailStructure.setSubject("Appointment Confirmation");
			emailStructure.addRecipient(patient.getEmail());
			emailSender.sendEmail(emailStructure);

			map.put("Success", "Success");
		} else {
			map.put("error", "Invalid patient ID or appointRequestID");
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/update/{patientID}/{appointmentRequestID}", method = RequestMethod.POST, params = {
			"status=DECLINE"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> declineAppointmentRequest(@PathVariable("patientID") long patientID,
			@PathVariable("appointmentRequestID") long appointmentRequestID,
			@RequestParam(name = "msg", defaultValue = "The appointment slot you selected is not available.Please create a new appointment request") String msg) {
		Map<String, Object> map = new HashMap<>();
		AppointmentRequest appointmentRequest = userServiceInterface.getAppointmentRequestByIDandPatientID(appointmentRequestID, patientID);
		if (appointmentRequest != null) {
			appointmentRequest.setStatus(AppointmentRequestStatus.DECLINED);
			map.put("Success", "Success");
			// Prepare and send an email regarding the decline of appointment
			// request.
			Patient patient = userServiceInterface.getBasicPatientDetails(patientID);
			Map<String, Object> emailMap = new HashMap<String, Object>();
			emailMap.put("user", patient.getFirstName() + " " + patient.getLastName());
			emailMap.put("appointmentRequest", appointmentRequest);

			String body = emailSender.prepareBody(EmailTemplate.APPOINTMENT_DECLINED, emailMap);
			emailStructure.setBody(body);
			emailStructure.setSenderEmail(encryptableProps.getProperty("email.id"));
			emailStructure.setSubject("Appointment Request Declined");
			emailStructure.addRecipient(patient.getEmail());
			emailSender.sendEmail(emailStructure);

			map.put("Success", "Success");

		} else {
			map.put("error", "Invalid patient ID or appointRequestID");
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

	}

}
