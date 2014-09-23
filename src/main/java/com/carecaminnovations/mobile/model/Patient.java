package com.carecaminnovations.mobile.model;

import java.util.List;

public class Patient {
	
	private int personId;
	private List<Metrics> metrics;
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public List<Metrics> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<Metrics> metrics) {
		this.metrics = metrics;
	}
	
}
