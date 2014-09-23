package com.carecaminnovations.mobile.model;

public enum QuestionType {
	
	TEXT_INPUT,
	NUMERIC_INPUT,
	CHECKBOX,
	RADIO_BUTTON,
	SELECT;
	
	private static final QuestionType values[] = values();
	
	public static QuestionType getQuestionTypeById(int id) {
		if (id < 1 || id > values.length) {
			throw new IllegalArgumentException("Invalid question type id");
		}
		return values[id - 1];
	}
}
