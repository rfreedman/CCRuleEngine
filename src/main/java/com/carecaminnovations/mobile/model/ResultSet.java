package com.carecaminnovations.mobile.model;

public class ResultSet {
	private int stepId;
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
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
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
	public int getAnswerId() {
		return answerId;
	}
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	
	public String getCompletedDt() {
		return completedDt;
	}
	public void setCompletedDt(String completedDt) {
		this.completedDt = completedDt;
	}

	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}



	private int formId;
	private int ownerId;
	private int personProtocolId;
	private String measureId;
	private int questionId;
	private String questionText;
	private int answerId;
	private String answerText;
	private String completedDt;

	
	public ResultSet(){}
}
