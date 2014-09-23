package com.carecaminnovations.mobile.model;

/** Bean for managing form post data. */
public class FormPostData {
	private int stepId;
	private int formId;
	private int personProtocolId;
	private int questionId;
	private String questionText;
	private String meausureId;
	private String answerText;
	
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	public int getFormId() {
		return formId;
	}
	public void setFormId(int formId) {
		this.formId = formId;
	}
	public int getPersonProtocolId() {
		return personProtocolId;
	}
	public void setPersonProtocolId(int personProtocolId) {
		this.personProtocolId = personProtocolId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getMeausureId() {
		return meausureId;
	}
	public void setMeausureId(String meausureId) {
		this.meausureId = meausureId;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
}
