package com.carecaminnovations.mobile.evalengine

import com.carecaminnovations.mobile.json.JsonRepository
import com.carecaminnovations.mobile.json.JsonRepositoryImpl
import com.carecaminnovations.mobile.model.ResultSet
import com.carecaminnovations.mobile.model.Results
import org.apache.commons.io.IOUtils
import spock.lang.Specification


class EvaluationEngineSpec extends Specification {

    EvaluationEngine evaluationEngine;

    def setup() {
        // these would typically be downloaded by the mobile app via the api

        String activitiesJson = IOUtils.toString(getClass().getResourceAsStream("/activities.json"), "UTF-8")
        String formsJson = IOUtils.toString(getClass().getResourceAsStream("/form.json"), "UTF-8")
        String questionSetsJson = IOUtils.toString(getClass().getResourceAsStream("/questionset.json"), "UTF-8")
        String ruleSetJson     = IOUtils.toString(getClass().getResourceAsStream("/ruleset-prioritized.json"), "UTF-8")
        String chartJson       = IOUtils.toString(getClass().getResourceAsStream("/chart.json"), "UTF-8")
        String messagesSetJson = IOUtils.toString(getClass().getResourceAsStream("/messageset.json"), "UTF-8")
        String tipSetsJson     = IOUtils.toString(getClass().getResourceAsStream("/tipset.json"), "UTF-8")

        evaluationEngine = new EvaluationEngine()
        JsonRepository jsonProcessor = new JsonRepositoryImpl(activitiesJson, formsJson, questionSetsJson, ruleSetJson, chartJson, messagesSetJson, tipSetsJson)
        evaluationEngine.setJsonRepository(jsonProcessor)
        /*
        evaluationEngine.setStateRepository(new EvaluationEngineStateRepository() {

            @Override
            void saveState(EvaluationEngineState state) {
            }

            @Override
            EvaluationEngineState loadState(int activityId, int stepId) {
                return null;
            }

            @Override
            void removeState(int activityId, int stepId) {
            }
        });
        */
        evaluationEngine.setStateRepository(new JSONEvalEngineStateRepo());
    }


    def "EvaluationEngine should process stack frames iteratively and terminate"() {
        given:
        int activityId = 410
        int stepId = 492
        evaluationEngine.evaluateForm(activityId, stepId)

        // simulate wait for user input
        for(int i = 0; i < 5; i++) {
            println(i)
            sleep(1000);
        }


        setup() // again
        evaluationEngine.evaluateForm(activityId, stepId)

        ResultSet resultSet = new ResultSet();
        List<ResultSet> resultSets = new ArrayList<ResultSet>();
        resultSets.add(resultSet);
        Results results = new Results();
        results.setResults(resultSets);

        evaluationEngine.applyUserInput(results);

        expect:
        evaluationEngine.stateRepository != null
    }

}
