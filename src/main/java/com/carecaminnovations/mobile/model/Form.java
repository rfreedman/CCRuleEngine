package com.carecaminnovations.mobile.model;

import java.util.List;

public class Form {
	public int formId;
	public String name;
	public int deviceTypeId;
	public int status;
	public int getRuleSetId() {
		return ruleSetId;
	}
	public void setRuleSetId(int ruleSetId) {
		this.ruleSetId = ruleSetId;
	}
	public boolean isReportable() {
		return reportable;
	}
	public void setReportable(boolean reportable) {
		this.reportable = reportable;
	}
	public int ruleSetId;
	public boolean reportable;
	public List<Fields> fields;
	public int getFormId() {
		return formId;
	}
	public void setFormId(int formId) {
		this.formId = formId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(int deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<Fields> getFields() {
		return fields;
	}
	public void setFields(List<Fields> fields) {
		this.fields = fields;
	}
	
}
