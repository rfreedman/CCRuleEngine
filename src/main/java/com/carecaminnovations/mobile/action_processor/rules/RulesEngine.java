package com.carecaminnovations.mobile.action_processor.rules;

import java.io.IOException;
import java.util.Map;

/**
 * Evaluates user input against Rules in a RuleSet
 */
public interface RulesEngine {
    /**
     * Evaluates an Integer answer against a RuleSet
     * @param ruleSetId The id of the RuleSet in the RuleSet JSON document
     * @param answers a Map containing the user-supplied numeric answers. The keys are question numbers, and the values are the answers.
     * @return An EvaluationResult based on the evaluation of the answer in terms of the rules
     * @throws IOException
     */
    public EvaluationResult evaluateRules(final int ruleSetId, final Map<Integer, Number> answers) throws IOException;
}
