package com.dentist.domain;


import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Scope("prototype")
@Embeddable
public class TeethStatusPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1955230820541889286L;
	@Transient
	private long patientID;
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "patientID", nullable = false, updatable = false)
	private Patient patient;
	@OneToOne
	@JoinColumn(name = "teethID", nullable = false, updatable = false)
	private Teeth teeth;

	public TeethStatusPK() {

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((patient == null) ? 0 : patient.hashCode());
		result = prime * result + ((teeth == null) ? 0 : teeth.hashCode());
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
		TeethStatusPK other = (TeethStatusPK) obj;
		if (patient == null) {
			if (other.patient != null)
				return false;
		} else if (!patient.equals(other.patient))
			return false;
		if (teeth == null) {
			if (other.teeth != null)
				return false;
		} else if (!teeth.equals(other.teeth))
			return false;
		return true;
	}

}
