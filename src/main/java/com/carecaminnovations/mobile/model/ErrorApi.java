package com.carecaminnovations.mobile.model;

public class ErrorApi {
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getTimeUTC() {
		return timeUTC;
	}
	public void setTimeUTC(String timeUTC) {
		this.timeUTC = timeUTC;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getAllXml() {
		return allXml;
	}
	public void setAllXml(String allXml) {
		this.allXml = allXml;
	}
	private String application;
	private String host;
	private String type;
	private String source;
	private String message;
	private String user;
	private int statusCode;
	private String timeUTC;
	private int sequence;
	private String allXml;

}
