package com.carecaminnovations.mobile.model;

import java.util.List;

public class PatientSummary {

	
	private Patient person;
	private String lastUpdatedDt;
	private String personId;
	private String thruDt;
	
	
	public Patient getPerson() {
		return person;
	}

	public void setPerson(Patient person) {
		this.person = person;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getLastUpdatedDt() {
		return lastUpdatedDt;
	}

	public void setLastUpdatedDt(String lastUpdatedDt) {
		this.lastUpdatedDt = lastUpdatedDt;
	}

	public String getThruDt() {
		return thruDt;
	}

	public void setThruDt(String thruDt) {
		this.thruDt = thruDt;
	}
}
