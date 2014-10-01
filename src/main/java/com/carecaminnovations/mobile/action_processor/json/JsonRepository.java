package com.carecaminnovations.mobile.action_processor.json;

import com.carecaminnovations.mobile.action_processor.rules.EvaluationResult;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.io.IOException;

public interface JsonRepository {

    public static enum JSONDocType {
        ACTIVITIES("activities"),
        FORMS("form"),
        QUESTIONSETS("questionset"),
        RULESETS("rules"),
        CHART("chart"),
        MESSAGESETS("messagesets"),
        TIPSETS("tipsets");

        private String alias;
        private JSONDocType(String alias) {
            this.alias = alias;
        }

        public String getAlias() {
            return alias;
        }
    };

    /**
     * Adds a json document to the repository.
     * The JSONDocType enum specifies the keys for the 'standard' documents,
     * but additional documents may be added, and then referred to in the rules.
     * Documents may be replaced by simply putting them with the same key as the previous version
     * @param documentType
     * @param value
     */
    public void putJsonDocument(final JSONDocType documentType, final String value);

    /**
     * Adds a json document to the repository.
     * The JSONDocType enum specifies the keys for the 'standard' documents,
     * but additional documents may be added, and then referred to in the rules.
     * Documents may be replaced by simply putting them with the same key as the previous version
     * @param key
     * @param value
     */
    public void putJsonDocument(final String key, final String value);

    /**
     * Retrieves JSON representing a RuleSet
     * @param ruleSetId the id of the RuleSet
     */
    public JSONArray lookupRuleSet(final int ruleSetId);


    public JSONObject lookupActivity(final int activityId);

    public JSONObject lookupForm(final int formId);

    public JSONObject lookupQuestionSet(final int questionSetId);

    /**
     * Retrieves JSON representing a MessageSet
     * @param messageSetId the id of the MessageSet
     */
    public JSONObject lookupMessageSet(final int messageSetId);

    /**
     * Retrieves JSON representing a TipSet
     * @param tipSetId the id of the TipSet
     */
    public JSONObject lookupTipSet(final int tipSetId);

    /**
     * Looks up a numeric value in a Json document using JsonPath, in a manner similar to xpath
     * @param rulePath A String containing the json document name and the path of the value in the document
     * @return the numeric value, or null if the path is not found.
     * @throws IOException if the document is not found
     */
    public Number lookupNumericValue(final String rulePath) throws IOException;

    /**
     * Given a matched Rule, creates an EvaluationResult
     * @param rule A JSONObject representing the Rule that matched
     * @return an EvaluationResult containing the objects indicated by the Rule
     */
    public EvaluationResult createEvaluationResult(final JSONObject rule);
}
