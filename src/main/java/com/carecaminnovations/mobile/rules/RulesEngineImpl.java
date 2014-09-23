package com.carecaminnovations.mobile.rules;

import com.carecaminnovations.mobile.json.JsonRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class RulesEngineImpl implements RulesEngine {
    private static final Logger logger = LoggerFactory.getLogger(RulesEngineImpl.class);

    private final JsonRepository jsonRepository;

    public RulesEngineImpl(JsonRepository jsonRepository) {
        this.jsonRepository = jsonRepository;
    }

    public EvaluationResult evaluateRules(final int ruleSetId, final Map<Integer, Number> answers) throws IOException {

        final JSONArray ruleSet = jsonRepository.lookupRuleSet(ruleSetId);

        assert(ruleSet.size() == 1);

        final JSONObject ruleSetMap = (JSONObject) ruleSet.get(0);
        final JSONArray rules = (JSONArray) ruleSetMap.get("rule");

        final String ruleSetComment = (String) ruleSetMap.get("comment");
        logger.debug("evaluating rules for: " + ruleSetComment);

        JSONObject matchingRule = null;

        for(Object rule : rules) {
            if(ruleMatches((JSONObject) rule, answers)) {
                matchingRule = (JSONObject) rule;
                break;
            }
        }

        final EvaluationResult evaluationResult = jsonRepository.createEvaluationResult(matchingRule);

        return evaluationResult;
    }

    private boolean ruleMatches(JSONObject rule, final Map<Integer, Number> answers) throws IOException {
        final Integer ruleType = (Integer) rule.get("type");

        // TODO: for now, only range comparison (type 2) is supported...this will have to be expanded to other comparison types

        boolean result;

        switch(ruleType) {
            case 2:
                result = isValueInRuleRange(rule, answers);
                break;

            default:
                result = false;
        }

        return result;
    }

    private boolean isValueInRuleRange(JSONObject rule, final Map<Integer, Number> answers) throws IOException {
        boolean result = false;

        try {

            final String startValuePath = (String) rule.get("start");
            final String endValuePath = (String) rule.get("end");
            final String ruleComment = (String) rule.get("comment");

            final Integer questionId = (Integer) rule.get("questionId");
            final Number value = answers.get(questionId);

            if(value != null) {
                logger.debug("evaluating rule: " + ruleComment);

                final Number startValue = jsonRepository.lookupNumericValue(startValuePath);
                final Number endValue = jsonRepository.lookupNumericValue(endValuePath);

                final BigDecimal decimalStartValue = new BigDecimal(startValue.toString());
                final BigDecimal decimalEndValue = new BigDecimal(endValue.toString());

                logger.debug("checking " + value + "  in range " + startValue + " : " + endValue + "....");
                final BigDecimal decimalValue = new BigDecimal(value.toString());

                if ((decimalValue.compareTo(decimalStartValue) >= 0 && decimalValue.compareTo(decimalEndValue) <= 0)) {
                    result = true;
                }

                final StringBuilder msg = new StringBuilder("rule: '")
                    .append(ruleComment)
                    .append("' ")
                    .append((result ? " matches" : " does not match"))
                    .append(" for value ").append(value)
                    .append(" in range ")
                    .append(decimalStartValue)
                    .append(" - ")
                    .append(decimalEndValue);

                logger.debug(msg.toString());
            } else {
                logger.debug("not evaluating rule: " + ruleComment + " because value is null");
            }

        } catch(Exception ex) {
            logger.error("failed to evaluate 'value in range'", ex);
        }

        return result;
    }
}
