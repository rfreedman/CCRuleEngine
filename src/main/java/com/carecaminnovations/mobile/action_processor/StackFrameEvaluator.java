package com.carecaminnovations.mobile.action_processor;

/**
 * Visitor interface to facilitate double-dispatch pattern
 */
public interface StackFrameEvaluator {
    public StackFrame evaluate(FormStackFrame frame);
    public StackFrame evaluate(QuestionSetStackFrame frame);
    public StackFrame evaluate(MessageSetStackFrame frame);
    public StackFrame evaluate(UserInputStackFrame frame);
    public StackFrame evaluate(RuleActionsStackFrame frame);
}
