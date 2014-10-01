package com.carecaminnovations.mobile.action_processor;

import com.carecaminnovations.mobile.model.Action;

import java.util.Arrays;

public class RuleActionsStackFrame extends StackFrame {

    private Action[] ruleActions;
    private Integer lastCompletedRuleActionIndex;

    public RuleActionsStackFrame() {
    }

    public void setRuleActions(Action[] ruleActions) {
        this.ruleActions = ruleActions;
    }

    @Override
    public StackFrame accept(StackFrameEvaluator visitor) {
        return visitor.evaluate(this);
    }

    private boolean hasMoreRuleActions() {
        boolean result = false;

        do {
            if (ruleActions == null) {
                // we have no actions
                break;
            }

            if (lastCompletedRuleActionIndex == null) {
                result = true; // we haven actions, but haven't completed any
                break;
            }

            if (lastCompletedRuleActionIndex.intValue() != ruleActions.length - 1) {
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
            if(hasMoreRuleActions()) {
                result = true;
                break;
            }

            result = super.hasMoreActions();

        } while(false);

        return result;
    }

    @Override
    public Action getFirstUncompletedAction() {

        Action result;

        do {
            if(hasMoreRuleActions()) {
                result = getFirstUncompletedRuleAction();
                break;
            }

            result = super.getFirstUncompletedAction();

        } while(false);

        return result;
    }

    private Action getFirstUncompletedRuleAction() {
        Action result = null;

        do {
            if(ruleActions == null) {
                break; // we have no actions
            }

            if(lastCompletedRuleActionIndex == null) {
                result = ruleActions[0]; // none completed, so return the first one
            }

            int uncompletedIndex = lastCompletedRuleActionIndex == null ? 0 : lastCompletedRuleActionIndex + 1;
            if(uncompletedIndex > ruleActions.length - 1) {
                break; // the last one is completed, there are no more
            }

            result = ruleActions[uncompletedIndex];

        } while(false);

        return result;
    }



    @Override
    public void markCurrentActionCompleted() {

        do {
            if(hasMoreRuleActions()) {
                markCurrentRuleActionComplete();
                break;
            }

            super.markCurrentActionCompleted();

        } while(false);


    }

    public void markCurrentRuleActionComplete() {

        if(allActionsCompleted()) {
            throw new IllegalStateException("markCurrentAnswerActionComplete - no 'current' action to complete");
        }

        if(lastCompletedRuleActionIndex == null) {
            lastCompletedRuleActionIndex = new Integer(0);
        } else {
            lastCompletedRuleActionIndex = new Integer(lastCompletedRuleActionIndex.intValue() + 1);
        }
    }

    @Override
    public boolean allActionsCompleted() {
        boolean result = false;

        do {
            if(hasMoreRuleActions()) {
                break;
            }

            result = super.allActionsCompleted();

        } while(false);

        return result;
    }

    @Override
    public String toString() {
        return "RuleActionsStackFrame{" +
            "ruleActions=" + Arrays.toString(ruleActions) +
            ", lastCompletedRuleActionIndex=" + lastCompletedRuleActionIndex +
            "} " + super.toString();
    }
}
