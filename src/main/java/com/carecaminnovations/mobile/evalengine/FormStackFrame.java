package com.carecaminnovations.mobile.evalengine;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * StackFrame for evaluation of a Form.
 * Because evaluation always begins with a Form, a FormStackFrame is always the bottom-most frame on the stack
 */
public class FormStackFrame extends StackFrame {
    private JSONObject form = null;

    private JSONArray answerActions;
    private Integer lastCompletedAnswerActionIndex;

    private JSONArray questionSetActions;
    private Integer lastCompletedQuestionSetActionIndex;

    private Integer questionSetId;

    /** for GSon only */
    public FormStackFrame() {
    }

    public FormStackFrame(final JSONObject form) {
        super((JSONArray)form.get("actions"));
        this.form = form;
    }

    public void setAnswerActions(final JSONArray answerActions) {
        this.answerActions = answerActions;
    }

    public void setQuestionSetActions(final JSONArray questionSetActions) {
        this.questionSetActions = questionSetActions;
    }

    public void setQuestionSetId(Integer questionSetId) {
        this.questionSetId = questionSetId;
    }

    public Integer getQuestionSetId() {
        return this.questionSetId;
    }

    private boolean hasMoreAnswerActions() {
        boolean result = false;

        do {
            if (answerActions == null) {
                // we have no actions
                break;
            }

            if (lastCompletedAnswerActionIndex == null) {
                result = true; // we haven actions, but haven't completed any
                break;
            }

            if (lastCompletedAnswerActionIndex.intValue() != answerActions.size() - 1) {
                result = true; // the last completed action is not the final action in the list
                break;
            }

        } while (false);

        return result;
    }

    private boolean hasMoreQuestionSetActions() {
        boolean result = false;

        do {
            if (questionSetActions == null) {
                // we have no actions
                break;
            }

            if (lastCompletedQuestionSetActionIndex == null) {
                result = true; // we haven actions, but haven't completed any
                break;
            }

            if (lastCompletedQuestionSetActionIndex.intValue() != questionSetActions.size() - 1) {
                result = true; // the last completed action is not the final action in the list
                break;
            }

        } while (false);

        return result;
    }

    @Override
    public boolean hasMoreActions() {
        boolean result;

        do {
            if(hasMoreAnswerActions()) {
                result = true;
                break;
            }

            if(hasMoreQuestionSetActions()) {
                result = true;
                break;
            }

            result = super.hasMoreActions();

        } while(false);

        return result;
    }

    @Override
    public JSONObject getFirstUncompletedAction() {

        JSONObject result;

        do {
            if(hasMoreAnswerActions()) {
                result = getFirstUncompletedAnswerAction();
                break;
            }

            if(hasMoreQuestionSetActions()) {
                result = getFirstUncompletedQuestionSetAction();
                break;
            }

            result = super.getFirstUncompletedAction();

        } while(false);

        return result;
    }

    private JSONObject getFirstUncompletedAnswerAction() {
        JSONObject result = null;

        do {
            if(answerActions == null) {
                break; // we have no actions
            }

            if(lastCompletedAnswerActionIndex == null) {
                result = (JSONObject) answerActions.get(0); // none completed, so return the first one
            }

            int uncompletedIndex = lastCompletedAnswerActionIndex == null ? 0 : lastCompletedAnswerActionIndex + 1;
            if(uncompletedIndex > answerActions.size() - 1) {
                break; // the last one is completed, there are no more
            }

            result = (JSONObject) answerActions.get(uncompletedIndex);

        } while(false);

        return result;
    }

    private JSONObject getFirstUncompletedQuestionSetAction() {
        JSONObject result = null;

        do {
            if(questionSetActions == null) {
                break; // we have no actions
            }

            if(lastCompletedQuestionSetActionIndex == null) {
                result = (JSONObject) questionSetActions.get(0); // none completed, so return the first one
            }

            int uncompletedIndex = lastCompletedQuestionSetActionIndex == null ? 0 : lastCompletedQuestionSetActionIndex + 1;
            if(uncompletedIndex > questionSetActions.size() - 1) {
                break; // the last one is completed, there are no more
            }

            result = (JSONObject) questionSetActions.get(uncompletedIndex);

        } while(false);

        return result;
    }



    @Override
    public void markCurrentActionCompleted() {

        do {
            if(hasMoreAnswerActions()) {
                markCurrentAnswerActionComplete();
                break;
            }

            if(hasMoreQuestionSetActions()) {
                marksCurrentQuestionSetActionComplete();
                break;
            }

            super.markCurrentActionCompleted();

        } while(false);


    }

    public void markCurrentAnswerActionComplete() {

        if(allActionsCompleted()) {
            throw new IllegalStateException("markCurrentAnswerActionComplete - no 'current' action to complete");
        }

        if(lastCompletedAnswerActionIndex == null) {
            lastCompletedAnswerActionIndex = new Integer(0);
        } else {
            lastCompletedAnswerActionIndex = new Integer(lastCompletedAnswerActionIndex.intValue() + 1);
        }

    }

    public void marksCurrentQuestionSetActionComplete() {

        if(allActionsCompleted()) {
            throw new IllegalStateException("marksCurrentQuestionSetActionComplete - no 'current' action to complete");
        }

        if(lastCompletedQuestionSetActionIndex == null) {
            lastCompletedQuestionSetActionIndex = new Integer(0);
        } else {
            lastCompletedQuestionSetActionIndex = new Integer(lastCompletedQuestionSetActionIndex.intValue() + 1);
        }

    }


    @Override
    public boolean allActionsCompleted() {
        boolean result = false;

        do {
            if(hasMoreAnswerActions()) {
                break;
            }

            if(hasMoreQuestionSetActions()) {
                break;
            }

            result = super.allActionsCompleted();

        } while(false);

        return result;
    }

    @Override
    public StackFrame accept(StackFrameEvaluator evaluator) {
        return evaluator.evaluate(this);
    }

    public JSONObject getForm() {
        return form;
    }


    @Override
    public String toString() {
        return "FormStackFrame{" +
            "form=" + form +
            ", answerActions=" + answerActions +
            ", lastCompletedAnswerActionIndex=" + lastCompletedAnswerActionIndex +
            ", questionSetActions=" + questionSetActions +
            ", lastCompletedQuestionSetActionIndex=" + lastCompletedQuestionSetActionIndex +
            ", questionSetId=" + questionSetId +
            "} ";
    }
}
