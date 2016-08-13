package com.dentist.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Scope("prototype")
@Entity
@Table(name = "patient_details")

public class Patient implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3633053765941562590L;
	@Id
	private long userID;
	@JsonIgnore
	@OneToOne
	@MapsId
	@JoinColumn(name = "userID")
	private UserAuthentication userAuth;
	@Column(nullable = false)
	@Type(type = "encryptedString")
	private String firstName;
	@Column(nullable = false)
	@Type(type = "encryptedString")
	private String lastName;
	@Column(nullable = false)
	@Type(type = "encryptedString")
	private String middleName;
	@Column(nullable = false)
	@DateTimeFormat(pattern = "MM-dd-yyyy")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate dateOfBirth;
	@Column(nullable = false, length = 255)
	@Type(type = "encryptedString")
	private String phoneNumber;
	@Column(unique = true, nullable = false)
	private String email;
	@Embedded
	private Address homeAddress;
	@Embedded
	@AttributeOverrides({@AttributeOverride(name = "name", column = @Column(name = "emergencyContactName")),
			@AttributeOverride(name = "phoneNumber", column = @Column(name = "emergencyContactNumber")),
			@AttributeOverride(name = "relation", column = @Column(name = "emergencyContactRelation"))})
	private EmergencyContact EmergencyContact;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
	private List<SentMessage> sentMessages = new ArrayList<SentMessage>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
	private List<ReceivedMessage> receivedMessages = new ArrayList<ReceivedMessage>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
	private List<SentDocument> uploadedDocs = new ArrayList<SentDocument>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
	private List<ReceivedDocument> receivedDocs = new ArrayList<ReceivedDocument>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
	private List<Treatment> treatments = new ArrayList<Treatment>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePatient")
	private List<Insurance> insurances = new ArrayList<Insurance>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "appointmentPatient")
	private List<AppointmentRequest> appointmentRequests = new ArrayList<AppointmentRequest>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "appointmentPatient")
	private List<Appointment> appointments = new ArrayList<Appointment>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "TeethStatusPK.patient")
	private List<PatientTeethStatus> patientTeeth = new ArrayList<PatientTeethStatus>();

	public Patient() {

	}

	public long getUserID() {
		if (this.userAuth != null && this.userAuth.getUserID() != 0) {
			return this.userAuth.getUserID();
		} else {
			return userID;
		}

	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public UserAuthentication getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(UserAuthentication userAuth) {
		this.userAuth = userAuth;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}

	public EmergencyContact getEmergencyContact() {
		return EmergencyContact;
	}

	public void setEmergencyContact(EmergencyContact emergencyContact) {
		EmergencyContact = emergencyContact;
	}

	public List<SentMessage> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(List<SentMessage> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public List<ReceivedMessage> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<ReceivedMessage> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}

	public List<SentDocument> getUploadedDocs() {
		return uploadedDocs;
	}

	public void setUploadedDocs(List<SentDocument> uploadedDocs) {
		this.uploadedDocs = uploadedDocs;
	}

	public List<ReceivedDocument> getReceivedDocs() {
		return receivedDocs;
	}

	public void setReceivedDocs(List<ReceivedDocument> receivedDocs) {
		this.receivedDocs = receivedDocs;
	}

	public List<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}

	public List<Insurance> getInsurances() {
		return insurances;
	}

	public void setInsurances(List<Insurance> insurances) {
		this.insurances = insurances;
	}

	public List<AppointmentRequest> getAppointmentRequests() {
		return appointmentRequests;
	}

	public void setAppointmentRequests(List<AppointmentRequest> appointmentRequests) {
		this.appointmentRequests = appointmentRequests;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public List<PatientTeethStatus> getPatientTeeth() {
		return patientTeeth;
	}

	public void setPatientTeeth(List<PatientTeethStatus> patientTeeth) {
		this.patientTeeth = patientTeeth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userID ^ (userID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		if (userID != other.userID)
			return false;
		return true;
	}

}
