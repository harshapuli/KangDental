package com.dentist.domain;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Scope("prototype")
@Entity
@Table(name = "treatments")
public class Treatment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3362189095145865756L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long treatmentID;
	@Transient
	private long patientID;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "patientID", nullable = false, updatable = false)
	private Patient patient;
	@OneToOne
	@JoinColumn(name = "teethID", nullable = false, updatable = false)
	private Teeth teeth;
	@Column(nullable = false, length = 6000)
	@Type(type = "encryptedString")
	private String note;
	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime treatmentInsertedTime;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate treatmentDoneTime;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate treatmentExpectedTime;
	@Column
	private BigDecimal amountPaid;
	@Column
	private BigDecimal amountExpected;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TreatmentStatus status;

	public Treatment() {

	}

	public long getTreatmentID() {
		return treatmentID;
	}

	public void setTreatmentID(long treatmentID) {
		this.treatmentID = treatmentID;
	}

	@JsonGetter
	public long getPatientID() {
		if (this.patient != null) {
			this.patientID = this.patient.getUserID();
		}
		return patientID;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Teeth getTeeth() {
		return teeth;
	}

	public void setTeeth(Teeth teeth) {
		this.teeth = teeth;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public DateTime getTreatmentInsertedTime() {
		return treatmentInsertedTime;
	}

	public void setTreatmentInsertedTime(DateTime treatmentInsertedTime) {
		this.treatmentInsertedTime = treatmentInsertedTime;
	}

	public LocalDate getTreatmentDoneTime() {
		return treatmentDoneTime;
	}

	public void setTreatmentDoneTime(LocalDate treatmentDoneTime) {
		this.treatmentDoneTime = treatmentDoneTime;
	}

	public LocalDate getTreatmentExpectedTime() {
		return treatmentExpectedTime;
	}

	public void setTreatmentExpectedTime(LocalDate treatmentExpectedTime) {
		this.treatmentExpectedTime = treatmentExpectedTime;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public BigDecimal getAmountExpected() {
		return amountExpected;
	}

	public void setAmountExpected(BigDecimal amountExpected) {
		this.amountExpected = amountExpected;
	}

	public TreatmentStatus getStatus() {
		return status;
	}

	public void setStatus(TreatmentStatus status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (treatmentID ^ (treatmentID >>> 32));
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
		Treatment other = (Treatment) obj;
		if (treatmentID != other.treatmentID)
			return false;
		return true;
	}

}
