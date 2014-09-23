package com.carecaminnovations.mobile.model;

import java.util.Date;

public class Consent {
	private int consentId;
	public int getConsentId() {
		return consentId;
	}
	public void setConsentId(int consentId) {
		this.consentId = consentId;
	}
	public String getConsentLanguage() {
		return consentLanguage;
	}
	public void setConsentLanguage(String consentLanguage) {
		this.consentLanguage = consentLanguage;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public Date getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	private String title;
	private String consentLanguage;
	private int frequency;
	private int status;
	private int type;
	private boolean accepted;
	private int userId;
	private int personId;
	private Date createdDt;

	
}
