package com.carecaminnovations.mobile.evalengine;

/**
 * Visitor interface to facilitate double-dispatch pattern
 */
public interface StackFrameEvaluator {
    public StackFrame evaluate(FormStackFrame frame);
    public StackFrame evaluate(MessageStackFrame frame);
    public StackFrame evaluate(UserInputStackFrame frame);
    public StackFrame evaluate(RuleActionsStackFrame frame);
}
