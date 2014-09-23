package com.carecaminnovations.mobile.model;

import java.util.List;

public class Question {
	private int questionId;
	private String measureId;
	private int questionTypeId;
	private String questionText;
	private List<AnswerItem> answers;
	public List<AnswerItem> getAnswers() {
		return answers;
	}
	public void setAnswers(List<AnswerItem> answers) {
		this.answers = answers;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	public int getQuestionTypeId() {
		return questionTypeId;
	}
	public void setQuestionTypeId(int questionTypeId) {
		this.questionTypeId = questionTypeId;
	}
	public QuestionType getQuestionType() {
		return QuestionType.getQuestionTypeById(questionTypeId);
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
}
