package com.carecaminnovations.mobile.evalengine

import spock.lang.Specification

class StackSpec extends Specification {

    def "stack should pop in reverse order of push"() {
        setup:
        def stack = new EvalStack();
        assert stack.empty

        when:
        stack.push(new TestStackFrame("1"))
        stack.push(new TestStackFrame("2"))
        stack.push(new TestStackFrame("3"))

        then:
        stack.empty == false
        stack.size == 3
        stack.pop().name == "3"
        stack.pop().name == "2"
        stack.pop().name == "1"
        assert stack.empty
    }

    def "stack should not throw when popping empty"() {
        setup:
        def stack = new EvalStack();
        assert stack.empty

        expect:
        stack.pop() == stack.EMPTY_STACK_FRAME;
    }

    def "stack should return EMPTY_STACK_FRAME when peeking empty"() {
        setup:
        def stack = new EvalStack();
        assert stack.empty

        expect:
        stack.peek() == stack.EMPTY_STACK_FRAME;
    }
}


class TestStackFrame extends StackFrame {

    private String name;

    public TestStackFrame(String name) {
        super(null);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    StackFrame accept(StackFrameEvaluator visitor) {
        return null
    }
}
