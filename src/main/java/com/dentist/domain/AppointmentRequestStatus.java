package com.dentist.domain;


/* Never change the order of elements after going into production */
public enum AppointmentRequestStatus {

	WAITING_FOR_APPROVAL("Waiting For Approval"), CANCELLED("Cancelled"), ACCEPTED("Accepted"), DECLINED("Declined");

	private String status;

	AppointmentRequestStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
