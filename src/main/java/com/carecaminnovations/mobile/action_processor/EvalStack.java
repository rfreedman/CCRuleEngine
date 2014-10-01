package com.carecaminnovations.mobile.action_processor;

import java.util.ArrayDeque;

/**
 * An evaluation stack for the eval engine - a stack of StackFrame instances
 */
public class EvalStack extends ArrayDeque<StackFrame> {

    public static final StackFrame EMPTY_STACK_FRAME = new EmptyStackFrame();

    public int getSize() {
        return size();
    }

    /**
     * Pops the stack, but unlike the underlying ArrayDeque, returns EMPTY_STACK_FRAME instead of throwing an Exception when the stack is empty
     */
    @Override
    public StackFrame pop() {

        StackFrame frame = EMPTY_STACK_FRAME;

        if(!this.isEmpty()) {
            frame = super.pop();
        }

        return frame;
    }

    public StackFrame peek() {
        StackFrame frame = EMPTY_STACK_FRAME;

        if(!this.isEmpty()) {
            frame = super.peek();
        }

        return frame;
    }
}
