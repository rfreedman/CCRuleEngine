package com.carecaminnovations.mobile.model;

public class ConsentResult {
	
	private int consentId;
	public int getConsentId() {
		return consentId;
	}
	public void setConsentId(int consentId) {
		this.consentId = consentId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	private String personId;
	private boolean accepted;
}
