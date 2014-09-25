package com.carecaminnovations.mobile.evalengine;

import com.carecaminnovations.mobile.model.Results;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.AbstractMap;

/**
 * A frame on the evaluation stack
 */
public abstract class StackFrame {

    private JSONArray actions;
    private Integer lastCompletedActionIndex;

    /** results produced while evaluating this frame - primarily for form (question set) display, but possibly also for message (message set) display */
    private Results results;

    /** for Gson only */
    public StackFrame() {
    }

    public StackFrame(JSONArray actions) {
        this.actions = actions;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public JSONArray getActions() {
        return actions;
    }

    public void setActions(JSONArray actions) {
        this.actions = actions;
    }

    public abstract StackFrame accept(StackFrameEvaluator visitor);

    public  boolean hasMoreActions() {
        boolean result = false;

        do {
            if(actions == null) {
                result = false; // we have no actions
                break;
            }

            if(lastCompletedActionIndex == null) {
                result = true; // we have actions, but haven't completed any
                break;
            }

            if(lastCompletedActionIndex.intValue() != actions.size() - 1) {
                result = true; // the last completed action is not the final action in the list
                break;
            }

        } while(false);

        return result;
    }

    public JSONObject getFirstUncompletedAction() {
        JSONObject result = null;

        do {
            if(actions == null) {
                break; // we have no actions
            }

            if(lastCompletedActionIndex == null) {
                result = (JSONObject) actions.get(0); // none completed, so return the first one
            }

            int uncompletedIndex = lastCompletedActionIndex == null ? 0 : lastCompletedActionIndex + 1;
            if(uncompletedIndex > actions.size() - 1) {
                break; // the last one is completed, there are no more
            }

            result = (JSONObject) actions.get(uncompletedIndex);

        } while(false);

        return result;
    }

    public void markCurrentActionCompleted() {

        if(allActionsCompleted()) {
            throw new IllegalStateException("markCurrentActionCompleted - no 'current' action to complete");
        }

        if(lastCompletedActionIndex == null) {
            lastCompletedActionIndex = new Integer(0);
        } else {
            lastCompletedActionIndex = new Integer(lastCompletedActionIndex.intValue() + 1);
        }

    }

    public void setLastCompletedActionIndex(Integer lastCompletedActionIndex) {
        this.lastCompletedActionIndex = lastCompletedActionIndex;
    }

    protected Integer getLastCompletedActionIndex() {
        return lastCompletedActionIndex;
    }

    public boolean allActionsCompleted() {
        return !hasMoreActions();
    }

}
