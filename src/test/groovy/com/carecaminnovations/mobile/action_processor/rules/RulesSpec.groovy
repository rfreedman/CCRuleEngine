package com.carecaminnovations.mobile.action_processor.rules

import com.carecaminnovations.mobile.action_processor.json.JsonRepository
import com.carecaminnovations.mobile.action_processor.json.JsonRepositoryImpl
import org.apache.commons.io.IOUtils
import spock.lang.Specification
import spock.lang.Unroll


class RulesSpec extends Specification {
    RulesEngine rulesEngine;

    def setup() {
        // these would typically be downloaded by the mobile app via the api

        String activitiesJson = IOUtils.toString(getClass().getResourceAsStream("/activities.json"), "UTF-8");
        String formsJson = IOUtils.toString(getClass().getResourceAsStream("/form.json"), "UTF-8");
        String questionSetsJson = IOUtils.toString(getClass().getResourceAsStream("/questionset.json"), "UTF-8");
        String ruleSetJson     = IOUtils.toString(getClass().getResourceAsStream("/rulesets.json"), "UTF-8");
        String chartJson       = IOUtils.toString(getClass().getResourceAsStream("/chart.json"), "UTF-8");
        String messagesSetJson = IOUtils.toString(getClass().getResourceAsStream("/messageset.json"), "UTF-8");
        String tipSetsJson     = IOUtils.toString(getClass().getResourceAsStream("/tipset.json"), "UTF-8");

        JsonRepository jsonProcessor = new JsonRepositoryImpl(activitiesJson, formsJson, questionSetsJson, ruleSetJson, chartJson, messagesSetJson, tipSetsJson);
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
        evaluationResult.actions != null
        evaluationResult.actions.get(0).type == actionType
        evaluationResult.actions.get(0).messageSetId == actionMessageSetId

        where:
        rulesetId | firstQuestionNumber | firstAnswer | secondQuestionNumber | secondAnswer | actionType | actionMessageSetId | comment
        2         | 1                   | 120         | 2                    | 81           | 2          | 4                  | 'bp systolic normal'
        2         | 1                   | 120         | 2                    | 130          | 2          | 6                  | 'bp diastolic alert high'
        1         | 3                   |  80         | null                 | null         | 2          | 1                  | 'blood glucose normal'
        1         | 3                   |  20         | null                 | null         | 2          | 3                  | 'blood glucose low'
        1         | 3                   | 400         | null                 | null         | 2          | 2                  | 'blood glucose high'
        2         | 1                   | 120         | null                 | null         | 2          | 4                  | 'bp systolic normal'
        2         | 1                   | 139         | null                 | null         | 2          | 5                  | 'bp systolic high'
        2         | 1                   | 139.00      | null                 | null         | 2          | 5                  | 'bp systolic high'
        2         | 1                   | 118         | null                 | null         | 2          | 5                  | 'bp systolic low'
        2         | 1                   | 139.01      | null                 | null         | 2          | 5                  | 'bp systolic high'
        2         | 1                   | 140         | null                 | null         | 2          | 5                  | 'bp systolic high'
        2         | 1                   |  50         | null                 | null         | 2          | 7                  | 'bp systolic very low'
    }
}
