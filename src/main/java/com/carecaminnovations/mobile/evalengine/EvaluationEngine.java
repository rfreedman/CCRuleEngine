package com.carecaminnovations.mobile.evalengine;



/*
Activity -> Multiple Steps, each step has at most one form
Form -> array of actions to be processed sequentially (go all the way down the tree and back for the first one, then process the second, etc.)


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
import com.carecaminnovations.mobile.model.Action;
import com.carecaminnovations.mobile.model.ResultSet;
import com.carecaminnovations.mobile.model.Results;
import com.carecaminnovations.mobile.rules.EvaluationResult;
import com.carecaminnovations.mobile.rules.RulesEngine;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.carecaminnovations.mobile.evalengine.EvalStack.EMPTY_STACK_FRAME;
import static com.carecaminnovations.mobile.model.Action.ActionType;

public class EvaluationEngine implements StackFrameEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationEngine.class);

    private EvaluationEngineStateRepository stateRepository;
    private JsonRepository jsonRepository;
    private RulesEngine rulesEngine;


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

    public void setRulesEngine(RulesEngine rulesEngine) {
        this.rulesEngine = rulesEngine;
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

        currentFrame.setResults(results);
        currentFrame.markCurrentActionCompleted();

        assert currentFrame instanceof FormStackFrame;
        populateStackFrameQuestionSetActions((FormStackFrame) currentFrame);
        populateStackFrameAnswerActions((FormStackFrame) currentFrame);

        // restart evaluation
        state.setShouldRun(true); // allow the engine to continue

        // we directly modified a frame on the stack
        // instead of going through wrapper methods,
        // so we need to save the new state
        persistState();

        evaluateTopFrame();
    }


    @Override
    public StackFrame evaluate(FormStackFrame frame) {

        StackFrame resultStackFrame = EMPTY_STACK_FRAME;

        Action currentAction = frame.getFirstUncompletedAction();
        if(currentAction != null) {
            logger.debug("evaluate - action is: " + currentAction);
            resultStackFrame = performAction(currentAction);
        }

        logger.debug("evaluate - result of action: " + currentAction + " is: " + resultStackFrame);

        return resultStackFrame;
    }

    // todo - if nothing different, combine the multiple evaluate(...Frame) methods
    public StackFrame evaluate(MessageStackFrame frame) {
        StackFrame resultStackFrame = EMPTY_STACK_FRAME;

        Action currentAction = frame.getFirstUncompletedAction();
        if(currentAction != null) {
            logger.debug("evaluate - action is: " + currentAction);
            resultStackFrame = performAction(currentAction);
        }

        logger.debug("evaluate - result of action: " + currentAction + " is: " + resultStackFrame);

        return resultStackFrame;
    }

    public StackFrame evaluate(UserInputStackFrame frame) {
        return EMPTY_STACK_FRAME;
    }

    public StackFrame evaluate(RuleActionsStackFrame frame) {
        logger.debug("evaluating RuleActionsStackFrame....");
        StackFrame resultStackFrame = EMPTY_STACK_FRAME;

        Action currentAction = frame.getFirstUncompletedAction();
        if(currentAction != null) {
            logger.debug("evaluate - action is: " + currentAction);
            resultStackFrame = performAction(currentAction);
        }

        logger.debug("evaluate - result of action: " + currentAction + " is: " + resultStackFrame);

        return resultStackFrame;
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
        logger.debug("\n===========================================");
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

            logger.debug("\n===========================================================");
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
                logger.debug("\n -----------------------------------------------------------");
                logger.debug("top frame: " + peekStack() + " - allActionsCompleted, so popping");
                popStack();
                logger.debug("after popping stack, " + getStackReport());
            }
        }

        logger.debug("evaluation engine is in stopped state, shouldRun = " + state.getShouldRun() + ", " + getStackReport());

        if(peekStack() == EMPTY_STACK_FRAME) {

            logger.debug("\n===========================================");
            logger.debug("stack is empty, so evaluation is complete");

            // send the stepResults to the database for later posting
            reportResults();

            // remove the persistent version
            stateRepository.removeState(state.getTopLevelActivityId(), state.getTopLevelStepId());
        }

        // if we get here, we've stopped without completing a flow (the stack is not empty),
        // typically because we need user input
        if(peekStack() instanceof UserInputStackFrame) {
            UserInputStackFrame userInputStackFrame = (UserInputStackFrame) peekStack();
            if(!userInputStackFrame.isInputRequested()) {
                requestUserInput(userInputStackFrame);
            }
        }
    }

    private StackFrame performAction(Action action) {
        StackFrame resultStackFrame = EMPTY_STACK_FRAME;

        logger.debug("\n -----------------------------------------------------------");
        logger.debug("before performing action: " + action + ", " + getStackReport());

        final int actionTypeCode = action.getType();
        final ActionType actionType = ActionType.fromTypeCode(actionTypeCode);
        switch(actionType) {
            case DISPLAY_QUESTION_SET:
                resultStackFrame = displayQuestionSet(action.getQuestionSetId());
                // this type of action is completed when the answers are applied
                break;

            case DISPLAY_MESSAGE_SET:
                resultStackFrame = displayMessageSet(action.getMessageSetId());
                // this type of action is completed when the answers are applied
                break;

            case EVALUATE_RULE_SET:
                try {
                    resultStackFrame = evaluateRules(action);
                } catch(Exception ex) {
                    logger.error("rule eval threw exception:", ex);
                }
                markCurrentActionCompleted();

                break;

            default:
                logger.warn("don't know what to do with action type " + actionType + ", so marking it complete");
                markCurrentActionCompleted();
                break;

        }

        logger.debug("\n -----------------------------------------------------------");
        logger.debug("after performing action: " + action + ", " + getStackReport());

        return resultStackFrame;
    }

    private StackFrame displayQuestionSet(int questionSetId) {
        logger.debug("displaying questionSet: " + questionSetId);


        // populate the current stack frame with questionSet actions, if any
        StackFrame currentFrame = peekStack();
        assert currentFrame instanceof FormStackFrame;

        ((FormStackFrame) currentFrame).setQuestionSetId(questionSetId);

        state.setShouldRun(false);

        // we directly modified a frame on the stack
        // instead of going through wrapper methods,
        // so we need to save the new state
        persistState();


        return new QuestionSetUserInputStackFrame(questionSetId);
    }


    private StackFrame displayMessageSet(int messageSetId) {
        logger.debug("displaying messageSet: " + messageSetId);


        // populate the current stack frame with messageSet actions, if any
        StackFrame currentFrame = peekStack();
        assert currentFrame instanceof FormStackFrame;

        ((FormStackFrame) currentFrame).setQuestionSetId(questionSetId);

        state.setShouldRun(false);

        // we directly modified a frame on the stack
        // instead of going through wrapper methods,
        // so we need to save the new state
        persistState();


        return new QuestionSetUserInputStackFrame(questionSetId);
    }

    private Map<Integer, ResultSet> getPreviousAnswers() {
        Map<Integer, ResultSet> previousAnswers = new HashMap<Integer, ResultSet>();

        Results lastResults = state.getLastResults();

        for(ResultSet resultSet : lastResults.getResults()) {
            previousAnswers.put(resultSet.getQuestionId(), resultSet);
        }

        return previousAnswers;
    }

    private StackFrame evaluateRules(Action action) throws Exception {
        StackFrame resultFrame = EMPTY_STACK_FRAME; // default

        Integer ruleSetId = action.getRuleSetId();

        Map<Integer, ResultSet> previousResultSet = getPreviousAnswers();

        // map the previousResultSet to previousAnswers - this is what will be sent to the rule engine
        Map<Integer, Number> previousAnswers = new HashMap<Integer, Number>();
        for(Map.Entry<Integer, ResultSet> entry : previousResultSet.entrySet()) {

            Number value = null;
            if(entry.getValue().getAnswerId() > 0) {
                value = entry.getValue().getAnswerId(); // mutiple choice - radio button, checkbox, etc.
            } else if(entry.getValue().getAnswerText() != null) {

                try {
                    value = Double.parseDouble(entry.getValue().getAnswerText());
                } catch (NumberFormatException ex) {
                    logger.error("EvaluationEngine.evaluateRules - failed to format answerText: " + entry.getValue().getAnswerText() + " as a Double for input into the rule");
                }
            }

            previousAnswers.put(entry.getKey(), value);
        }

        logger.debug("evaluating ruleSet " + ruleSetId);

        EvaluationResult evaluationResult = rulesEngine.evaluateRules(ruleSetId, previousAnswers);

        logger.debug("result of evaluating ruleSet " + ruleSetId + ": " + evaluationResult);

        // an evaluation result is now a list of actions
        if(evaluationResult != EvaluationResult.EMPTY) {
            //logger.debug("(NOT YET) creating  rule evaluation results / actions stack frame for rule evaluation result: " + evaluationResult);

            RuleActionsStackFrame stackFrame = new RuleActionsStackFrame();
            Action[] actions = new Action[evaluationResult.actions.size()];
            actions = evaluationResult.actions.toArray(actions);
            stackFrame.setRuleActions(evaluationResult.actions.toArray(actions));
            resultFrame = stackFrame;
        }

        return resultFrame;
    }


    private void markCurrentActionCompleted() {
        peekStack().markCurrentActionCompleted();
        persistState();
    }

    private StackFrame peekStack() {
        return state.getEvalStack().peek();
    }

    private StackFrame popStack() {
        StackFrame result = state.getEvalStack().pop();
        persistState();
        return result;
    }

    private void pushStack(StackFrame frame) {
        state.getEvalStack().push(frame);
        persistState();
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

    private void populateStackFrameQuestionSetActions(final FormStackFrame frame) {
        int questionSetId = frame.getQuestionSetId();
        JSONObject questionSet = getQuestionSet(questionSetId);
        frame.setQuestionSetActions((JSONArray) questionSet.get("actions"));
    }

    private void populateStackFrameAnswerActions(final FormStackFrame frame) {
        // todo - populate the currentFrame with answer actions - these need to be matched up with the answers,
        // so maybe we bundle question, answer, and actions together?
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

    private void requestUserInput(final UserInputStackFrame frame) {
        if(!frame.isInputRequested()) {
            frame.setInputRequested(true);
            persistState();
            // TODO - request user input, probably via message bus
            logger.debug("Request for user input: " + frame + ": NOT IMPLEMENTED YET");
        }
    }

    private void reportResults() {
        // todo: implement me - push data to database to be posted to api asynchronously
        logger.debug("reporting results (not implemented)");
        logger.debug("allResults = " + state.getAllResults());
    }
}

