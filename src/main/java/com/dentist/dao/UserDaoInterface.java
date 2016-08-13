package com.dentist.dao;


import java.util.List;

import org.hibernate.Session;

import com.dentist.domain.Appointment;
import com.dentist.domain.AppointmentRequest;
import com.dentist.domain.Insurance;
import com.dentist.domain.Patient;
import com.dentist.domain.PatientTeethStatus;
import com.dentist.domain.ReceivedDocument;
import com.dentist.domain.ReceivedMessage;
import com.dentist.domain.SentDocument;
import com.dentist.domain.SentMessage;
import com.dentist.domain.Teeth;
import com.dentist.domain.Treatment;
import com.dentist.domain.UserAuthentication;

public interface UserDaoInterface {
	public Session getHibernateSession();

	public Object mergeEntity(Object entity);

	/* DAO methods on UserAuthentication.class */
	public void setUserAuthenticationInfo(UserAuthentication userAuthentication);

	public void updateUserAuthenticationInfo(UserAuthentication userAuthentication);

	public UserAuthentication getUserAuthenticationInfoById(long id);

	public UserAuthentication getUserAuthenticationInfoByEmail(String email);

	/* DAO methods on Patient.class */
	public void setPatient(Patient patient);

	public void updatePatient(Patient patient);

	public Patient getPatientInfoById(long patientID);

	public Patient getBasicPatientDetails(long patientID);

	public Patient getPatientInfoByEmail(String patientEmail);

	public List<Patient> getAllPatients();

	/* DAO methods on AppointmentRequest.class */
	public void setAppointmentRequest(AppointmentRequest request);

	public void updateAppointmentRequest(AppointmentRequest request);

	public AppointmentRequest getAppointmentRequestByID(long appointmentRequestID);

	public AppointmentRequest getAppointmentRequestByIDandPatientID(long appointmentRequestID, long patientID);

	public List<AppointmentRequest> getAppointmentRequestsByPatientID(long patientID);

	public List<AppointmentRequest> getAllAppointmentRequests();

	public List<AppointmentRequest> getAllPendingAppointmentRequests();

	/* DAO methods on Appointment.class */
	public void setAppointment(Appointment appointment);

	public void updateAppointment(Appointment appointment);

	public Appointment getAppointmentByID(long appointmentID);

	public Appointment getAppointmentByIDandPatientID(long appointmentID, long patientID);

	public List<Appointment> getAppointmentsByPatientID(long patientID);

	public List<Appointment> getAllAppointments();

	/* DAO methods on Insurance.class */
	public void setInsurance(Insurance insurance);

	public void updateInsurance(Insurance insurance);

	public Insurance getInsuranceByID(long insuranceID);

	public Insurance getInsuranceByIDandPatientID(long insuranceID, long patientID);

	public List<Insurance> getInsurancesByPatientID(long patientID);

	/* DAO methods on SentMessage.class */
	public void setSentMessage(SentMessage sentMessage);

	public void updateSentMessage(SentMessage sentMessage);

	public SentMessage getSentMessageByID(long sentMessageID);

	public List<SentMessage> getSentMessagesByPatientID(long patientID);

	public List<SentMessage> getAllSentMessages();

	/* DAO methods on ReceivedMessage.class */
	public void setReceivedMessage(ReceivedMessage receivedMessage);

	public void updateReceivedMessage(ReceivedMessage receivedMessage);

	public ReceivedMessage getReceivedMessageByID(long receivedMessageID);

	public List<ReceivedMessage> getReceivedMessagesByPatientID(long patientID);

	public List<ReceivedMessage> getAllReceivedMessages();

	/* DAO methods on SentDocument.class */
	public void setSentDocument(SentDocument sentDocument);

	public void updateSentDocument(SentDocument sentDocument);

	public SentDocument getSentDocumentByID(long sentDocumentID);

	public SentDocument getSentDocumentByIDandPatientID(long sentDocumentID, long patientID);

	public List<SentDocument> getSentDocumentsByPatientID(long patientID);

	public List<SentDocument> getAllSentDocuments();

	/* DAO methods on ReceivedDocument.class */
	public void setReceivedDocument(ReceivedDocument receivedDocument);

	public void updateReceivedDocument(ReceivedDocument receivedDocument);

	public ReceivedDocument getReceivedDocumentByID(long receivedDocumentID);

	public ReceivedDocument getReceivedDocumentByIDandPatientID(long receivedDocumentID, long patientID);

	public List<ReceivedDocument> getReceivedDocumentsByPatientID(long patientID);

	public List<ReceivedDocument> getAllreceivedDocuments();

	/* DAO methods on Treatment.class */
	public void setTreatment(Treatment treatment);

	public void updateTreatment(Treatment treatment);

	public Treatment getTreatmentByID(long treatmentID);

	public Treatment getTreatmentByIDandPatientID(long treatmentID, long patientID);

	public List<Treatment> getTreatmentsByPatientID(long userID);

	public List<Treatment> getTreatmentsByPatientIDandTeethID(long patientID, int teethID);

	/* DAO methods on PatientTeethStatus.class */
	public void setPatientTeethStatus(PatientTeethStatus patientTeethStatus);

	public void updatePatientTeethStatus(PatientTeethStatus patientTeethStatus);

	public PatientTeethStatus getPatientTeethStatusByPatientIDandTeethID(long patientID, int teethID);

	public List<PatientTeethStatus> getPatientTeethStatusByPatientID(long patientID);

	/* DAO methods on Teeth.class */
	public Teeth getTeethByID(int teethID);

}
