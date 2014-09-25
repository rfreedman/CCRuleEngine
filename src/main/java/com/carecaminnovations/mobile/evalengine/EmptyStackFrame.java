package com.carecaminnovations.mobile.evalengine;

import net.minidev.json.JSONArray;

/**
 * Indicates that the stack is empty
 * Only ever returned from stack's peek and pop methods when the stack is empty,
 * never actually put on the stack
 */
public class EmptyStackFrame extends StackFrame {
    public EmptyStackFrame() {
        super(null);
    }

    @Override
    public String toString() {
        return "EmptyStackFrame";
    }

    @Override
    public StackFrame accept(StackFrameEvaluator visitor) {
        return this;
    }
}
