package com.carecaminnovations.mobile.action_processor;


public interface EvaluationEngineStateRepository {
    public void saveState(final EvaluationEngineState state);
    public EvaluationEngineState loadState(final int activityId, final int stepId);
    public void removeState(final int activityId, final int stepId);
}
