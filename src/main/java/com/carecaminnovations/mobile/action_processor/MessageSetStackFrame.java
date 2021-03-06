package com.carecaminnovations.mobile.action_processor;


import com.carecaminnovations.mobile.model.Action;
import com.google.gson.GsonBuilder;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class MessageSetStackFrame extends StackFrame {

    private int messageSetId;
    private Action[] messageSetActions;
    private Integer lastCompletedMessageSetActionIndex;

    public MessageSetStackFrame() {
    }

    public int getMessageSetId() {
        return messageSetId;
    }

    public void setMessageSetId(int messageSetId) {
        this.messageSetId = messageSetId;
    }

    @Override
    public StackFrame accept(StackFrameEvaluator visitor) {
        return visitor.evaluate(this);
    }

    private static Action[] actionsFromJSON(JSONArray actionsJson) {
        Action[] result = new Action[actionsJson.size()];
        for(int i = 0; i < actionsJson.size(); i++) {
            JSONObject actionJSon = (JSONObject) actionsJson.get(i);
            Action action = new GsonBuilder().create().fromJson(actionJSon.toString(), Action.class);
            result[i] = action;
        }
        return result;
    }

    public void setMessageSetActions(final JSONArray messageSetActions) {
        this.messageSetActions = actionsFromJSON(messageSetActions);
    }

    @Override
    public boolean hasMoreActions() {
        boolean result;

        do {
            if(hasMoreMessageSetActions()) {
                result = true;
                break;
            }

            result = super.hasMoreActions();

        } while(false);

        return result;
    }

    private boolean hasMoreMessageSetActions() {
        boolean result = false;

        do {
            if (messageSetActions == null) {
                // we have no actions
                break;
            }

            if (lastCompletedMessageSetActionIndex == null) {
                result = true; // we haven actions, but haven't completed any
                break;
            }

            if (lastCompletedMessageSetActionIndex.intValue() != messageSetActions.length - 1) {
                result = true; // the last completed action is not the final action in the list
                break;
            }

        } while (false);

        return result;
    }

    @Override
    public Action getFirstUncompletedAction() {

        Action result;

        do {
            if(hasMoreMessageSetActions()) {
                result = getFirstUncompletedAnswerAction();
                break;
            }

            result = super.getFirstUncompletedAction();

        } while(false);

        return result;
    }

    private Action getFirstUncompletedAnswerAction() {
        Action result = null;

        do {
            if(messageSetActions == null) {
                break; // we have no actions
            }

            if(lastCompletedMessageSetActionIndex == null) {
                result = messageSetActions[0]; // none completed, so return the first one
            }

            int uncompletedIndex = lastCompletedMessageSetActionIndex == null ? 0 : lastCompletedMessageSetActionIndex + 1;
            if(uncompletedIndex > messageSetActions.length - 1) {
                break; // the last one is completed, there are no more
            }

            result = messageSetActions[uncompletedIndex];

        } while(false);

        return result;
    }

    @Override
    public void markCurrentActionCompleted() {

        do {
            if(hasMoreMessageSetActions()) {
                markCurrentMessageSetActionComplete();
                break;
            }

            // we never have any actions in the base class
            //super.markCurrentActionCompleted();

        } while(false);


    }

    public void markCurrentMessageSetActionComplete() {

        if(allActionsCompleted()) {
            throw new IllegalStateException("markCurrentMessageSetActionComplete - no 'current' action to complete");
        }

        if(lastCompletedMessageSetActionIndex == null) {
            lastCompletedMessageSetActionIndex = new Integer(0);
        } else {
            lastCompletedMessageSetActionIndex = new Integer(lastCompletedMessageSetActionIndex.intValue() + 1);
        }

    }
}
