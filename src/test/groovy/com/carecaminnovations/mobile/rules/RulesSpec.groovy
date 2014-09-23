package com.carecaminnovations.mobile.rules

import com.carecaminnovations.mobile.json.JsonRepository
import com.carecaminnovations.mobile.json.JsonRepositoryImpl
import com.carecaminnovations.mobile.model.Button
import org.apache.commons.io.IOUtils
import spock.lang.Specification
import spock.lang.Unroll


class RulesSpec extends Specification {
    RulesEngine rulesEngine;

    def setup() {
        // these would typically be downloaded by the mobile app via the api
        String ruleSetJson     = IOUtils.toString(getClass().getResourceAsStream("/ruleset-prioritized.json"), "UTF-8");
        String chartJson       = IOUtils.toString(getClass().getResourceAsStream("/chart.json"), "UTF-8");
        String messagesSetJson = IOUtils.toString(getClass().getResourceAsStream("/messageset.json"), "UTF-8");
        String tipSetsJson     = IOUtils.toString(getClass().getResourceAsStream("/tipset.json"), "UTF-8");

        JsonRepository jsonProcessor = new JsonRepositoryImpl(ruleSetJson, chartJson, messagesSetJson, tipSetsJson);
        rulesEngine = new RulesEngineImpl(jsonProcessor);
    }

    def cleanup() {
        rulesEngine = null;
    }


    @Unroll("Evaluate rules for #comment : #firstAnswer, #secondAnswer")
    def "Rule Evaluation should return proper Action with multiple answers"() {
        given:
        Map<Integer, Number> answers = new HashMap<Integer, Number>();
        answers.put(firstQuestionNumber, firstAnswer);

        if(secondQuestionNumber != null) {
            answers.put(secondQuestionNumber, secondAnswer)
        } else {
            System.out.println("skipping null");
        }

        def evaluationResult = rulesEngine.evaluateRules(rulesetId, answers)

        expect:
        evaluationResult != null
        evaluationResult != EvaluationResult.EMPTY;
        evaluationResult.action != null
        evaluationResult.action.type == actionType
        evaluationResult.action.messageSetId == actionMessageSetId
        evaluationResult.message != null
        evaluationResult.message.messageId > 0
        evaluationResult.message.text.isEmpty() == false
        if(evaluationResult.message.tipSetId > 0) {
            evaluationResult.tip != null
            evaluationResult.tip.tipId > 0
            evaluationResult.tip.text.isEmpty() == false
        }
        evaluationResult.buttons != null
        evaluationResult.buttons.size() > 0
        for(Button button : evaluationResult.buttons) {
            button.buttonId > 0;
            button.text.isEmpty() == false
        }

        where:
        rulesetId | firstQuestionNumber | firstAnswer | secondQuestionNumber | secondAnswer | actionType | actionMessageSetId | comment
        2         | 1                   | 120         | 2                    | 81           | 1          | 4                  | 'bp systolic normal'
        2         | 1                   | 120         | 2                    | 125          | 1          | 6                  | 'bp diastolic alert high'
        1         | 3                   |  80         | null                 | null         | 1          | 1                  | 'blood glucose normal'
        1         | 3                   |  20         | null                 | null         | 1          | 3                  | 'blood glucose low'
        1         | 3                   | 400         | null                 | null         | 1          | 2                  | 'blood glucose high'
        2         | 1                   | 120         | null                 | null         | 1          | 4                  | 'bp systolic normal'
        2         | 1                   | 139         | null                 | null         | 1          | 5                  | 'bp systolic high'
        2         | 1                   | 139.00      | null                 | null         | 1          | 5                  | 'bp systolic high'
        2         | 1                   | 118         | null                 | null         | 1          | 5                  | 'bp systolic low'
        2         | 1                   | 139.01      | null                 | null         | 1          | 5                  | 'bp systolic high'
        2         | 1                   | 140         | null                 | null         | 1          | 5                  | 'bp systolic high'
        2         | 1                   |  50         | null                 | null         | 1          | 7                  | 'bp systolic very low'
    }
}
