package com.carecaminnovations.mobile.model;

public class DailySummary {
	private int metricId;
	private int personId;
	private int personProtocolId;
	private int type;
	private String name;
	private int value;
	private String unit;
	private String createdDt;
	private String lastUpdatedDt;
	private MeasureDefinition measureDefinition;
	
	public String getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(String createdDt) {
		this.createdDt = createdDt;
	}
	public String getLastUpdatedDt() {
		return lastUpdatedDt;
	}
	public void setLastUpdatedDt(String lastUpdatedDt) {
		this.lastUpdatedDt = lastUpdatedDt;
	}
	public int getMetricId() {
		return metricId;
	}
	public void setMetricId(int metricId) {
		this.metricId = metricId;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public int getPersonProtocolId() {
		return personProtocolId;
	}
	public void setPersonProtocolId(int personProtocolId) {
		this.personProtocolId = personProtocolId;
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
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}
	public void setMeasureDefinition(MeasureDefinition measureDefinition) {
		this.measureDefinition = measureDefinition;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
	
	
}
