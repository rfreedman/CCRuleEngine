package com.carecaminnovations.mobile.action_processor;

import com.carecaminnovations.mobile.model.Results;

import java.util.ArrayList;
import java.util.List;

public class EvaluationEngineState {
    private int topLevelActivityId;
    private int topLevelStepId;
    private int frameCounter;
    private EvalStack evalStack = new EvalStack();
    private List<Results> stepResults = new ArrayList<Results>();

    private List<Results> allResults = new ArrayList<Results>();

    private boolean shouldRun = true;

    public int getTopLevelActivityId() {
        return topLevelActivityId;
    }

    public void setTopLevelActivityId(final int topLevelActivityId) {
        this.topLevelActivityId = topLevelActivityId;
    }

    public int getTopLevelStepId() {
        return topLevelStepId;
    }

    public void setTopLevelStepId(final int topLevelStepId) {
        this.topLevelStepId = topLevelStepId;
    }

    public EvalStack getEvalStack() {
        return evalStack;
    }

    public void setEvalStack(final EvalStack evalStack) {
        this.evalStack = evalStack;
    }

    public List<Results> getStepResults() {
        return stepResults;
    }

    public void setStepResults(final List<Results> stepResults) {
        this.stepResults = stepResults;
    }

    public int getFrameCounter() {
        return frameCounter;
    }

    public int incrementFrameCounter() {
        return ++frameCounter;
    }

    public List<Results> getAllResults() {
        return allResults;
    }

    public Results getLastResults() {
        return allResults.get(allResults.size() -1);
    }

    public void addResults(final Results results) {
        allResults.add(results);
    }

    public boolean getShouldRun() {
        return shouldRun;
    }

    public void setShouldRun(final boolean shouldRun) {
        this.shouldRun = shouldRun;
    }
}
