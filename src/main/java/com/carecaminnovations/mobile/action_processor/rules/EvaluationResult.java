package com.carecaminnovations.mobile.action_processor.rules;

import com.carecaminnovations.mobile.model.Action;

import java.util.List;

public class EvaluationResult {
    public static final EmptyEvaluationResult EMPTY = new EmptyEvaluationResult();

    public final Integer ruleId;
    public final List<Action> actions;

    public EvaluationResult(final Integer ruleId, List<Action> actions) {
        this.ruleId = ruleId;
        this.actions = actions;
    }


    private static class EmptyEvaluationResult extends EvaluationResult {
        private EmptyEvaluationResult() {
            super(null, null);
        }
    }

    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder(
            "EvaluationResult{" +
            "ruleId=" + ruleId +
            ", actions=["
        );

        if(actions == null) {
            buf.append("null");
        } else {
            for(Action action : actions) {
                buf.append("\n")
                   .append(action.toString());
            }
        }


        buf.append("]}");

        return buf.toString();
    }
}
