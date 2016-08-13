package com.dentist.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Embeddable
public class EmergencyContact implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3266512225087243884L;

	@Column(length = 600)
	@Type(type = "encryptedString")
	private String name;
	@Column(length = 600)
	@Type(type = "encryptedString")
	private String phoneNumber;
	@Column(length = 255)
	@Type(type = "encryptedString")
	private String relation;

	public EmergencyContact() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String number) {
		this.phoneNumber = number;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

}
