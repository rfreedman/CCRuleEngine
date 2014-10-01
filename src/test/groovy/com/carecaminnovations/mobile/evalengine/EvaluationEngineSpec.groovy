package com.carecaminnovations.mobile.evalengine

import com.carecaminnovations.mobile.json.JsonRepository
import com.carecaminnovations.mobile.json.JsonRepositoryImpl
import com.carecaminnovations.mobile.model.ResultSet
import com.carecaminnovations.mobile.model.Results
import com.carecaminnovations.mobile.rules.RulesEngineImpl
import org.apache.commons.io.IOUtils
import spock.lang.Specification


class EvaluationEngineSpec extends Specification {

    EvaluationEngine evaluationEngine;

    def setup() {
        // these would typically be downloaded by the mobile app via the api

        String activitiesJson   = IOUtils.toString(getClass().getResourceAsStream("/activities.json") as InputStream,  "UTF-8")
        String formsJson        = IOUtils.toString(getClass().getResourceAsStream("/form.json") as InputStream,        "UTF-8")
        String questionSetsJson = IOUtils.toString(getClass().getResourceAsStream("/questionset.json") as InputStream, "UTF-8")
        String ruleSetJson      = IOUtils.toString(getClass().getResourceAsStream("/rulesets.json") as InputStream,    "UTF-8")
        String chartJson        = IOUtils.toString(getClass().getResourceAsStream("/chart.json") as InputStream,       "UTF-8")
        String messagesSetJson  = IOUtils.toString(getClass().getResourceAsStream("/messageset.json") as InputStream,  "UTF-8")
        String tipSetsJson      = IOUtils.toString(getClass().getResourceAsStream("/tipset.json") as InputStream,      "UTF-8")
        JsonRepository jsonRepository = new JsonRepositoryImpl(activitiesJson, formsJson, questionSetsJson, ruleSetJson, chartJson, messagesSetJson, tipSetsJson)

        evaluationEngine = new EvaluationEngine()
        evaluationEngine.setJsonRepository(jsonRepository)
        evaluationEngine.setStateRepository(new JSONEvalEngineStateRepo());
        evaluationEngine.setRulesEngine(new RulesEngineImpl(jsonRepository))
    }


    def "EvaluationEngine should process stack frames iteratively and terminate"() {
        given:
        int activityId = 410
        int stepId = 492
        evaluationEngine.evaluateActivityStep(activityId, stepId)

        // simulate wait for user input
        for(int i = 0; i < 5; i++) {
            println(i)
            sleep(1000);
        }

        setup() // again
        evaluationEngine.evaluateActivityStep(activityId, stepId)

        List<ResultSet> resultSets = new ArrayList<ResultSet>();

        ResultSet resultSet = new ResultSet();
        resultSet.setStepId(stepId)
        resultSet.setQuestionId(3);
        resultSet.setAnswerText("85");
        resultSets.add(resultSet);


        Results results = new Results();
        results.setResults(resultSets);

        evaluationEngine.applyQuestionSetUserInput(results);




        // simulate wait for user input for message set
        for(int i = 0; i < 5; i++) {
            println(i)
            sleep(1000);
        }

        // resume
        setup()
        evaluationEngine.evaluateActivityStep(activityId, stepId);
        evaluationEngine.applyMessageSetUserInput();



        expect:
        evaluationEngine.stateRepository != null
    }

}
