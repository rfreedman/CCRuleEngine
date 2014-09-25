package com.carecaminnovations.mobile.evalengine;

/**
 * A stack frame that indicates the the evaluation engine should pause for user input.
 * The evaluation engine must save it's state and then stop evaluation.
 * When the user eventually supplies input, the evaluation context must be restored if necessary,
 * and then the input must be applied to the last input action on the stack, and then evaluation continues.
 */
public class UserInputStackFrame extends StackFrame {

    public UserInputStackFrame() {
        super(null);
    }

    @Override
    public String toString() {
        return "UserInputStackFrame";
    }

    @Override
    public StackFrame accept(StackFrameEvaluator evaluator) {
        return evaluator.evaluate(this);
    }
}
