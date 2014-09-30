package com.carecaminnovations.mobile.model;

public class Action {

    public enum ActionType {
        UNKNOWN(-1),
        DISPLAY_QUESTION_SET(0),
        EVALUATE_RULE_SET(1),
        DISPLAY_MESSAGE_SET(2);

        private final int typeCode;

        private ActionType(int typeCode) {
            this.typeCode = typeCode;
        }

        public static ActionType fromTypeCode(final int typeCode) {
            ActionType result = UNKNOWN;

            for(ActionType actionType : ActionType.values()) {
                if(actionType.typeCode == typeCode) {
                    result = actionType;
                    break;
                }
            }

            return result;
        }
    }

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

    public void setQuestionSetId(Integer questionSetId) {
        this.questionSetId = questionSetId == null ? 0 : questionSetId.intValue();
    }

    public int getRuleSetId() {
        return ruleSetId;
    }

    public void setRuleSetId(Integer ruleSetId) {
        this.ruleSetId = ruleSetId == null ? 0 : ruleSetId.intValue();
    }

    public int getMessageSetId() {
		return messageSetId;
	}
	public void setMessageSetId(Integer messageSetId) {
		this.messageSetId = messageSetId == null ? 0 : messageSetId.intValue();
	}

    @Override
    public String toString() {
        return "Action{" +
            "type=" + type + "(" + ActionType.fromTypeCode(type).name() + ")" +
            ", questionSetId=" + questionSetId +
            ", messageSetId=" + messageSetId +
            ", ruleSetId=" + ruleSetId +
            '}';
    }
}
