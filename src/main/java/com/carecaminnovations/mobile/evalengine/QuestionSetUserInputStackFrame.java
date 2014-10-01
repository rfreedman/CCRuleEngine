package com.carecaminnovations.mobile.evalengine;

/**
 * Created by rfreedman on 9/29/14.
 */
public class QuestionSetUserInputStackFrame extends UserInputStackFrame {

    public final int questionSetId;

    public QuestionSetUserInputStackFrame(final int questionSetId) {
        this.questionSetId = questionSetId;
    }

    @Override
    public StackFrame accept(StackFrameEvaluator evaluator) {
        return evaluator.evaluate(this);
    }

    public int getQuestionSetId() {
        return questionSetId;
    }

    @Override
    public String toString() {
        return "QuestionSetUserInputStackFrame{" +
            "questionSetId=" + questionSetId +
            "} " + super.toString();
    }
}
