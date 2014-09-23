package com.carecaminnovations.mobile.model;

import java.util.Date;

public class Step {
	private int stepId;
	private int activityId;
	private int formId;
	private int type;
	private String name;
	private String description;
	private Date createdDt;
	private int lastUpdatedUserId;
	private Date lastUpdatedDt;
	private boolean stepStatus;
	private boolean dataStatus;
	private int status;
	private Form form;
	
	public Form getForm() {
		return form;
	}
	public void setForm(Form form) {
		this.form = form;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isStepStatus() {
		return stepStatus;
	}
	public void setStepStatus(boolean stepStatus) {
		this.stepStatus = stepStatus;
	}
	public boolean isDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(boolean dataStatus) {
		this.dataStatus = dataStatus;
	}
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public int getFormId() {
		return formId;
	}
	public void setFormId(int formId) {
		this.formId = formId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}
	public int getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdatedUserId(int lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public Date getLastUpdatedDt() {
		return lastUpdatedDt;
	}
	public void setLastUpdatedDt(Date lastUpdatedDt) {
		this.lastUpdatedDt = lastUpdatedDt;
	}
	public Step(){}
	
}
