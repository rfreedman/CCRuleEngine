package com.carecaminnovations.mobile.model;

import java.util.Date;


public class Notification {
	private int notificationId;
	private int personId;
	private int activityId;
	private String uaJson;
	private Date lastUpdatedDt;
	
	public int getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public String getUaJson() {
		return uaJson;
	}
	public void setUaJson(String uaJson) {
		this.uaJson = uaJson;
	}
	public Date getLastUpdatedDt() {
		return lastUpdatedDt;
	}
	public void setLastUpdatedDt(Date lastUpdatedDt) {
		this.lastUpdatedDt = lastUpdatedDt;
	}
}
