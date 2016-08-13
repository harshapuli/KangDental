package com.dentist.mail;

public enum EmailTemplate {
	WELCOME_EMAIL("welcomeemail.vm"), VERIFY_ACCOUNT_EMAIL("verifyaccountemail.vm"), LAST_LOGIN_EMAIL("lastloginemail.vm"), FORGOT_PASSWORD(
			"forgotpassword.vm"), PASSWORD_CHANGED("passwordchanged.vm"), NEW_MESSAGE_NOTIFICATION("newmessage.vm"), NEW_DOCUMENT_NOTIFICATION(
					"newdocument.vm"), APPOINTMENTREQUEST_CREATED("appointmentrequestcreated.vm"), APPOINTMENT_REMIENDER(
							"appointmentreminder.vm"), APPOINTMENT_CONFIRMED("appointmentconfirmed.vm"), APPOINTMENT_DECLINED(
									"appointmentdeclined.vm"), APPOINTMENT_CANCELLED("appointmentcancelled.vm"), CONTACT_US("contactus.vm");

	private String name;

	EmailTemplate(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
