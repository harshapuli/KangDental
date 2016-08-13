package com.dentist.webapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dentist.domain.ReceivedMessage;
import com.dentist.service.UserServiceInterface;



@RestController
@EnableAsync
@Transactional
@RequestMapping("/receivedmessages")
public class ReceivedMessageController {

	private static final Logger LOGGER = Logger.getLogger(ReceivedMessageController.class);
	@Autowired
	private UserServiceInterface userServiceInterface;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{messageID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReceivedMessage> getReceivedMessageByID(@PathVariable("messageID") long messageID) {
		LOGGER.info("processing get request to /receivedmessages/{messageID}");
		ReceivedMessage receivedMessage = userServiceInterface.getReceivedMessageByID(messageID);
		return new ResponseEntity<ReceivedMessage>(receivedMessage, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')") // change to ROLE_ADMIN
	@RequestMapping(value = "/patient/{patientID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ReceivedMessage>> getReceivedMessagesByPatientID(@PathVariable("patientID") long patientID) {
		LOGGER.info("processing get request to /receivedmessages/patient/{patientID}");
		List<ReceivedMessage> receivedMessages = userServiceInterface.getReceivedMessagesByPatientID(patientID);
		return new ResponseEntity<List<ReceivedMessage>>(receivedMessages, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')") // change to ROLE_ADMIN
	@RequestMapping(value = "/allmessagestopatients", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getAllSentMessages() {
		LOGGER.info("processing get request to /sentmessages/allmessagestopatients");
		Map<String, Object> map = new HashMap<>();
		List<ReceivedMessage> receivedMessages = userServiceInterface.getAllReceivedMessages();
		map.put("data", receivedMessages);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

}
