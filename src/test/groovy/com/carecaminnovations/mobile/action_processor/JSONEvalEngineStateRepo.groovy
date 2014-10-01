package com.carecaminnovations.mobile.action_processor

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class JSONEvalEngineStateRepo implements EvaluationEngineStateRepository {
    private static final Logger logger = LoggerFactory.getLogger(JSONEvalEngineStateRepo.class)

    private Gson gson;

    public JSONEvalEngineStateRepo() {

        gson = new GsonBuilder()
            .registerTypeAdapter(StackFrame.class, new GsonTypeAdapter<StackFrame>())
            .create();
    }


    @Override
    void saveState(final EvaluationEngineState state) {
        File file = getPersistenceFile(state)

        logger.debug("saving state to file: " + file.absolutePath)

        if(!file.exists()) {
            file.createNewFile()
        }

        Writer writer = new FileWriter(file)
        gson.toJson(state, writer);
        writer.flush();

        file.deleteOnExit();
    }

    @Override
    EvaluationEngineState loadState(int activityId, int stepId) {
        EvaluationEngineState result = null;

        File file = getPersistenceFile(activityId, stepId)

        logger.debug("attempting to load state from " + file.absolutePath)

        if(file.exists()) {
            result = gson.fromJson(new FileReader(file), EvaluationEngineState.class)
        }

        return result;
    }

    @Override
    void removeState(int activityId, int stepId) {
        File file = getPersistenceFile(activityId, stepId)

        logger.debug("deleting state file: " + file.absolutePath)

        if(file.exists()) {
            file.delete()
        }
    }

    private File getPersistenceFile(final EvaluationEngineState state) {
        return getPersistenceFile(state.getTopLevelActivityId(), state.getTopLevelStepId())
    }

    private File getPersistenceFile(final int activityId, final int stepId) {
        return new File(constructFileName(activityId, stepId))
    }

    private String constructFileName(final int activityId, final int stepId) {
       return "state_" + activityId + "_" + stepId + ".json";
    }
}
