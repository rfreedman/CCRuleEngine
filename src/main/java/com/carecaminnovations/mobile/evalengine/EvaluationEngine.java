package com.carecaminnovations.mobile.evalengine;



/*
Activity -> Multiple Steps, each step has at most one form
Form -> array of actions to be processed sequentially (go all the way down the tree and back for the first one, then process the second, etc.)

3 types of action
 - display questionset = 0
 - display messageset = 1
 - run a ruleset = 2


 * questionset - set of questions
 * questions have answers
 * an answer may have actions
 *
 * messageset may have an action (multiple actions?)
 *
 * a ruleset may have an action (multiple actions?)
 *
 *
 ***** Scenario *****
 * - Activity is manually selected - this is the entry point
 *  - Get the activity from the repository by id
 *  - while(steps) {
 *       Evaluate next Step
 *    }
 *
 *  - EvaluateStep: already exists
 *    -- if form step, shows form, user fills in answers and submits
 *    -- on form submit, evaluate actions array (use a queue, and pop actions as they are processed)
 *
 *  -- EvaluateActions:
 *    -- iterate actions, push onto ActionQueue
 *    -- push stack frame w/actions onto eval stack, and persist the engine state (every push/pop updates persistence? make this a "persistent engine"?)
 *    -- while(actions) {
 *       -- currentAction = actionQueue.peek
 *       -- do the action (display questionset, messageset, or run a ruleset), get back a set of results and an array of actions
 *          -- add resultset to engine's collection
  *         -- push stack frame w/ actions
  *         -- while(actions)....
  *      }
  *
  *
  *   -- post resultset (answers) only when we get back to the root(evaluation of all of the form step's actions is complete, i.e. the step is done)
 *
 */

import com.carecaminnovations.mobile.json.JsonRepository;
import com.carecaminnovations.mobile.model.Results;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.carecaminnovations.mobile.evalengine.EvalStack.EMPTY_STACK_FRAME;

public class EvaluationEngine implements StackFrameEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationEngine.class);

    private enum ActionType {
        UNKNOWN(-1), DISPLAY_QUESTION_SET(0), RUN_RULE_SET(2);

        private int code;
        private ActionType(int code) {
            this.code = code;
        }

        public static ActionType fromCode(int code) {
            ActionType result = UNKNOWN;
            for(ActionType actionType : ActionType.values()) {
                if(actionType.code == code) {
                    result = actionType;
                    break;
                }
            }
            return result;
        }
    }

    private EvaluationEngineStateRepository stateRepository;
    private JsonRepository jsonRepository;

    /**
     * The state of the evaluation engine.
     * Note that all non-transient state of the evaluation engine must be kept in this object,
     * so that it can be persisted and re-hydrated as needed to support restart capability.
     */
    private EvaluationEngineState state;

    public void setStateRepository(final EvaluationEngineStateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public void setJsonRepository(final JsonRepository jsonRepository) {
        this.jsonRepository = jsonRepository;
    }

    /** The root of the evaluation hierarchy - this happens when the user selects a step in an activity.
     *  Typically, the first evaluation will cause a form or message to be displayed.
     */
    public void evaluateForm(final int activityId, final int stepId) {

        // either recover saved state, if any, or build the starting state for the step
        initializeEngine(activityId, stepId);

        // start evaluating - invoke the iterative method
        evaluateTopFrame();
    }

    /**
     * The entity displaying the form invokes this when the user has supplied input
     * and the input has been validated.
     */
    public void applyUserInput(Results results) {
        logger.debug("resuming with user input: " + results + ", " + getStackReport());

        assert state.getShouldRun() == false;
        assert peekStack() instanceof UserInputStackFrame;

        logger.debug("popping and discarding UserInputStackFrame");
        popStack(); // discard the UserInputStackFrame
        logger.debug("after discarding UserInputStackFrame, " + getStackReport());

        // store the Results (user's answers) in the list to be reported up to the api at the end
        state.addResults(results);

        // store the Results in the current stack frame
        StackFrame currentFrame = peekStack();

        assert currentFrame != null;
        assert currentFrame != EMPTY_STACK_FRAME;
        assert currentFrame instanceof FormStackFrame; // || currentFrame instanceof MessageStackFrame

        currentFrame.markCurrentActionCompleted();

        currentFrame.setResults(results);

        // populate the current stack frame with questionSet actions, if any
        assert currentFrame instanceof FormStackFrame;
        int questionSetId = ((FormStackFrame) currentFrame).getQuestionSetId();
        JSONObject questionSet = getQuestionSet(questionSetId);
        ((FormStackFrame) currentFrame).setQuestionSetActions((JSONArray) questionSet.get("actions"));



        // todo - populate the currentFrame with answer actions - these need to be matched up with the answers,
        // so maybe we bundle question, answer, and actions together?

        // restart evaluation
        state.setShouldRun(true); // allow the engine to continue
        evaluateTopFrame();
    }


    @Override
    public StackFrame evaluate(FormStackFrame frame) {

        StackFrame resultStackFrame = EMPTY_STACK_FRAME;

        JSONObject currentAction = frame.getFirstUncompletedAction();
        if(currentAction != null) {
            resultStackFrame = performAction(currentAction);
        }

        return resultStackFrame;
    }

    public StackFrame evaluate(UserInputStackFrame frame) {
        return EMPTY_STACK_FRAME;
    }

    private String getStackReport() {
        int stackDepth = getStackDepth();

        StringBuilder buf = new StringBuilder("stack depth = ")
            .append(stackDepth);

        if(stackDepth > 0) {
            buf.append(", top frame = ")
                .append(peekStack());
        }

        return buf.toString();
    }

    private void initializeEngine(final int activityId, final int stepId) {
        logger.debug("initializing for evaluation of form for activityId: " + activityId + ", stepId: " + stepId);

        state = stateRepository.loadState(activityId, stepId);
        if(state == null) {
            state = buildInitialState(activityId, stepId);
            persistState();
        } else {
            logger.debug("loaded cached state for evaluation of form for activityId: " + activityId + ", stepId: " + stepId);
        }
    }

    private EvaluationEngineState buildInitialState(final int activityId, final int stepId) {

        logger.debug("not cached, so building Initial State for evaluation of form for activityId: " + activityId + ", stepId: " + stepId);

        EvaluationEngineState engineState = new EvaluationEngineState();
        engineState.setTopLevelActivityId(activityId);
        engineState.setTopLevelStepId(stepId);

        JSONObject activity = getActivity(activityId);
        JSONObject step = getStep(activity, stepId);

        Object formIdObj = step.get("formId");
        if(formIdObj != null) {
            int formId = Integer.parseInt(formIdObj.toString());
            StackFrame initialStackFrame = buildStackFrameForForm(formId);
            engineState.getEvalStack().push(initialStackFrame);
        }

        return engineState;
    }

    // Repeatedly evaluates the top stack frame until the stack is empty.
    // A StackFrame remains on the stack until all of its actions are completed.
    private void evaluateTopFrame() {

        while(state.getShouldRun() && !stackIsEmpty()) {

            logger.debug("evaluateTopFrame - " + getStackReport());

            StackFrame currentFrame = peekStack();

            logger.debug("evaluating top stack frame: " + currentFrame + ", " + getStackReport());

            StackFrame childFrame = currentFrame.accept(this); // this drives the evaluate(StackFrame) virtual call, new StackFrame is a result of the evaluation

            logger.debug("result of evaluating stack frame: " + currentFrame + " is: " + childFrame);

            if(childFrame != EMPTY_STACK_FRAME) {
                logger.debug("evaluateTopFrame - pushing " + childFrame);
                pushStack(childFrame);
                logger.debug("evaluateTopFrame, after pushing result of evaluation, " + getStackReport());
            }

            if(!state.getShouldRun()) {
                // we need to stop for user input
                logger.debug("stopping for user input");
                break;
            }

            if(peekStack().allActionsCompleted()) {
                logger.debug("top frame: " + peekStack() + " - allActionsCompleted, so popping");
                popStack();
                logger.debug("after popping stack, " + getStackReport());
            }
        }

        logger.debug("evaluation engine stopped, " + getStackReport());

        if(peekStack() == EMPTY_STACK_FRAME) {

            logger.debug("stack is empty, so evaluation is complete");

            // send the stepResults to the database for later posting
            reportResults();

            // remove the persistent version
            stateRepository.removeState(state.getTopLevelActivityId(), state.getTopLevelStepId());
        }
    }


    private StackFrame performAction(JSONObject action) {
        StackFrame resultStackFrame = EMPTY_STACK_FRAME;

        logger.debug("before performing action: " + action + ", " + getStackReport());

        final int actionTypeCode = ((Integer) action.get("type")).intValue();
        final ActionType actionType = ActionType.fromCode(actionTypeCode);
        switch(actionType) {
            case DISPLAY_QUESTION_SET:
                resultStackFrame = displayQuestionSet(((Integer) action.get("questionSetId")).intValue());
                // this type of action is completed when the answers are applied
                break;

            case RUN_RULE_SET:
                logger.warn("ruleSet eval not implemented yet, so marking ruleSet action complete");
                peekStack().markCurrentActionCompleted();
                break;

            default:
                logger.warn("don't know what to do with action type " + actionType + ", so marking it complete");
                peekStack().markCurrentActionCompleted();
                break;

        }

        logger.debug("after performing action: " + action + ", " + getStackReport());

        return resultStackFrame;
    }




    private StackFrame displayQuestionSet(int questionSetId) {
        logger.debug("displaying questionSet: " + questionSetId);


        // populate the current stack frame with questionSet actions, if any
        StackFrame currentFrame = peekStack();
        assert currentFrame instanceof FormStackFrame;

        JSONObject questionSet = getQuestionSet(questionSetId);
        ((FormStackFrame) currentFrame).setQuestionSetId(questionSetId);


        // todo - request display of the question set, perhaps with an android intent, or bus message
        state.setShouldRun(false);
        return new UserInputStackFrame(); // todo: params?
    }

    private StackFrame peekStack() {
        return state.getEvalStack().peek();
    }

    private StackFrame popStack() {
        return state.getEvalStack().pop();
    }

    private void pushStack(StackFrame frame) {
        state.getEvalStack().push(frame);
    }

    private StackFrame buildStackFrameForForm(final int formId) {
        JSONObject form = getForm(formId);
        return new FormStackFrame(form);
    }

    // todo - implementation
    private StackFrame buildStackFrameForQuestionSet(final JSONObject questionSet) {
        return null;
    }

    // todo - implementation
    private StackFrame buildStackFrameForMessageSet(final JSONObject messageSet) {
        return null;
    }

    // todo - implementation
    private StackFrame buildStackFrameForRuleSet(final JSONObject ruleSet) {
        return null;
    }

    private StackFrame buildStackFrameForActions(final JSONArray actions) {
        String name = "Frame " + Integer.toString(state.incrementFrameCounter());

        return null; //new StackFrame(name, actions);
    }

    private JSONObject getActivity(final int activityId) {
        return jsonRepository.lookupActivity(activityId);
    }

    // TODO - when converting to android, use android's JSONObject, with has() and getInt()
    private JSONObject getStep(final JSONObject activity, final Integer stepId) {
        JSONObject result = null;

        JSONArray steps = (JSONArray) activity.get("steps");
        for(int i = 0; i < steps.size(); i++) {
            JSONObject step = (JSONObject) steps.get(i);
            if(stepId.equals(step.get("stepId"))) {
                result = step;
                break;
            }
        }

        return result;
    }

    private JSONObject getForm(int formId) {
        return jsonRepository.lookupForm(formId);
    }

    private JSONObject getQuestionSet(int questionSetId) {
        return jsonRepository.lookupQuestionSet(questionSetId);
    }

    private boolean stackIsEmpty() {
        return (peekStack() == EMPTY_STACK_FRAME);
    }

    private int getStackDepth() {
        return state.getEvalStack().getSize();
    }

    private void persistState() {
        stateRepository.saveState(state);
    }

    private void reportResults() {
        // todo: implement me - push data to database to be posted to api asynchronously
        logger.debug("reporting results (not implemented)");
        logger.debug("allResults = " + state.getAllResults());
    }
}

