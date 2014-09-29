package com.carecaminnovations.mobile.model;

public class Action {

	private int type;
	private int questionSetId;
    private int messageSetId;
    private int ruleSetId;


	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

    public int getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(int questionSetId) {
        this.questionSetId = questionSetId;
    }

    public int getRuleSetId() {
        return ruleSetId;
    }

    public void setRuleSetId(int ruleSetId) {
        this.ruleSetId = ruleSetId;
    }

    public int getMessageSetId() {
		return messageSetId;
	}
	public void setMessageSetId(int messageSetId) {
		this.messageSetId = messageSetId;
	}

    @Override
    public String toString() {
        return "Action{" +
            "type=" + type +
            ", questionSetId=" + questionSetId +
            ", messageSetId=" + messageSetId +
            ", ruleSetId=" + ruleSetId +
            '}';
    }
}
