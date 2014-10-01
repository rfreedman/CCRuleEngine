package com.carecaminnovations.mobile.evalengine;


public class MessageSetUserInputStackFrame extends UserInputStackFrame {

    public final int messageSetId;

    public MessageSetUserInputStackFrame(final int messageSetId) {
        this.messageSetId = messageSetId;
    }

    public int getMessageSetId() {
        return messageSetId;
    }

    @Override
    public StackFrame accept(StackFrameEvaluator evaluator) {
        return evaluator.evaluate(this);
    }
}
