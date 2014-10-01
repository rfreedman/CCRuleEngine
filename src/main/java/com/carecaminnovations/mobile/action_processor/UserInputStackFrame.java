package com.carecaminnovations.mobile.action_processor;

/**
 * A stack frame that indicates the the evaluation engine should pause for user input.
 * The evaluation engine must save it's state and then stop evaluation.
 * When the user eventually supplies input, the evaluation context must be restored if necessary,
 * and then the input must be applied to the last input action on the stack, and then evaluation continues.
 */
public abstract class UserInputStackFrame extends StackFrame {

    private boolean inputRequested = false;

    public UserInputStackFrame() {
        super(null);
    }

    public boolean isInputRequested() {
        return inputRequested;
    }

    public void setInputRequested(boolean inputRequested) {
        this.inputRequested = inputRequested;
    }
}
