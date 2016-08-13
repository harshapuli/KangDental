package com.dentist.domain;



/* Never change the order of elements after going into production */
public enum TeethStatus {
	EXTRACTED("Extracted"), NORMAL("Normal");

	private String status;

	TeethStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
