package com.carecaminnovations.mobile.rules;

import com.carecaminnovations.mobile.model.Action;
import com.carecaminnovations.mobile.model.Button;
import com.carecaminnovations.mobile.model.Message;
import com.carecaminnovations.mobile.model.Tip;

import java.util.List;

public class EvaluationResult {
    public static final EmptyEvaluationResult EMPTY = new EmptyEvaluationResult();

    public final Action action;
    public final Message message;
    public final Tip tip;
    public final List<Button> buttons;

    public EvaluationResult(Action action, Message message, Tip tip, List<Button> buttons) {
        this.action = action;
        this.message = message;
        this.tip = tip;
        this.buttons = buttons;
    }

    @Override
    public String toString() {
        return "EvaluationResult{" +
            "action=" + action +
            ", message=" + message +
            ", tip=" + tip +
            ", buttons=" + buttons +
            '}';
    }

    private static class EmptyEvaluationResult extends EvaluationResult {
        private EmptyEvaluationResult() {
            super(null, null, null, null);
        }
    }
}
