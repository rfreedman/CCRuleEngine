package com.carecaminnovations.mobile.json;


import com.carecaminnovations.mobile.model.Action;
import com.carecaminnovations.mobile.model.Button;
import com.carecaminnovations.mobile.model.Message;
import com.carecaminnovations.mobile.model.Tip;
import com.carecaminnovations.mobile.rules.EvaluationResult;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class JsonRepositoryImpl implements JsonRepository {

    static final Logger logger = LoggerFactory.getLogger(JsonRepositoryImpl.class);

    private static final Random rand = new Random();

    private final Map<String, String> jsonDocs = new HashMap<String, String>();

    /**
     * @param ruleSetsJson Text of the ruleSets JSON document
     * @param chartJson Text of the chart JSON document
     * @param messageSetsJson Text of the messageSets JSON document
     * @param tipSetsJson Text of the tipSets JSON document
     */
    public JsonRepositoryImpl(final String activitiesJson, final String formsJson, final String questionSetsJson, final String ruleSetsJson, final String chartJson, final String messageSetsJson, final String tipSetsJson) {

        // These are the 'primary' json document types.
        // The rules typically refer only to the "chart" data, but if other data is needed,
        // a document can be added with another key, and referred to by that key in a rule
        putJsonDocument(JSONDocType.ACTIVITIES.getAlias(), activitiesJson);
        putJsonDocument(JSONDocType.FORMS.getAlias(), formsJson);
        putJsonDocument(JSONDocType.QUESTIONSETS.getAlias(), questionSetsJson);


        putJsonDocument(JSONDocType.RULESETS.getAlias(), ruleSetsJson);
        putJsonDocument(JSONDocType.CHART.getAlias(), chartJson);
        putJsonDocument(JSONDocType.MESSAGESETS.getAlias(), messageSetsJson);
        putJsonDocument(JSONDocType.TIPSETS.getAlias(), tipSetsJson);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putJsonDocument(final JSONDocType documentType, final String value) {
        jsonDocs.put(documentType.getAlias(), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putJsonDocument(final String key, final String value) {
        jsonDocs.put(key, value);
    }

    @Override
    public JSONObject lookupActivity(final int activityId) {
        final String activitiesJson = jsonDocs.get(JSONDocType.ACTIVITIES.getAlias());

        JSONArray activities =  JsonPath.read(activitiesJson, "$..response[?(@.activityId == " + activityId + ")]");

        return (activities == null || activities.size() == 0) ? null : (JSONObject) activities.get(0);
    }

    public JSONObject lookupForm(final int formId) {
        final String formsJson = jsonDocs.get(JSONDocType.FORMS.getAlias());
        JSONArray forms = JsonPath.read(formsJson, "$..response[?(@.formId == " + formId + ")]");

        return (forms == null || forms.size() == 0) ? null : (JSONObject) forms.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray lookupRuleSet(final int ruleSetId) {
        final String ruleSetsJson = jsonDocs.get(JSONDocType.RULESETS.getAlias());
        return JsonPath.read(ruleSetsJson, "$..resultSets[?(@.ruleSetId == " + ruleSetId + ")]");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject lookupQuestionSet(final int questionSetId) {
        final String questionSetsJson = jsonDocs.get(JSONDocType.QUESTIONSETS.getAlias());
        JSONArray questionSets =  JsonPath.read(questionSetsJson, "$..response[?(@.questionSetId == " + questionSetId + ")]");

        return (questionSets == null || questionSets.size() == 0) ? null : (JSONObject) questionSets.get(0);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject lookupMessageSet(final int messageSetId) {
        final String messageSetsJson = jsonDocs.get(JSONDocType.MESSAGESETS.getAlias());
        return JsonPath.read(messageSetsJson, "$..messageSets[?(@.messageSetId == " + messageSetId + ")][0]");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject lookupTipSet(final int tipSetId) {
        JSONObject result = null;

        final String tipSetsJson = jsonDocs.get(JSONDocType.TIPSETS.getAlias());
        try {
            result = JsonPath.read(tipSetsJson, "$..tipSets[?(@.tipSetId == " + tipSetId + ")][0]");
        } catch(RuntimeException ex) {
            logger.error("failed to execute JsonPath", ex);
            throw new RuntimeException(ex);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number lookupNumericValue(final String rulePath) throws IOException {
        final String jsonName = getJsonDocumentName(rulePath);
        final String jsonPath = getJsonPath(rulePath);

        Number result = null;

        final String jsonDoc = jsonDocs.get(jsonName);

        if(jsonDoc == null) {
            throw new IllegalStateException("json document '" + jsonName + "' does not exist in the rules engine");
        }

        result = lookupNumericValue(jsonDoc, jsonPath);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EvaluationResult createEvaluationResult(final JSONObject rule) {
        EvaluationResult result = EvaluationResult.EMPTY;

        if(rule != null) {

            final JSONObject jsonAction = (JSONObject) rule.get("action");
            final Action action = new Action();
            action.setType((Integer) jsonAction.get("type"));
            action.setMessageSetId((Integer) jsonAction.get("messageSetId"));


            final JSONObject jsonMessageSet = lookupMessageSet(action.getMessageSetId());
            final Message message = createMessage(jsonMessageSet);
            final Tip tip = createTip(message.getTipSetId());
            final List<Button> buttons = createButtons(jsonMessageSet);

            result = new  EvaluationResult(action, message, tip, buttons);

            logger.debug("messageSet = " + jsonMessageSet);
        }

        return result;
    }


    /**
     * Determines the document name from a JsonPath expression
     * @param path The path to be inspected
     * @return The document name
     */
    private String getJsonDocumentName(final String path) {
        final int dotPos = path.indexOf('.');
        final String jsonName = path.substring(0, dotPos);
        return jsonName;
    }

    /**
     * Determines the path (minus the document name) from a JsonPath expression
     * @param path The path to be inspected
     * @return The path, without the document name
     */
    private String getJsonPath(final String path) {
        final int dotPos = path.indexOf('.');
        final String jsonPath = path.substring(dotPos + 1);
        return jsonPath;
    }

    /**
     * Looks up a numeric value in a JSON document, using the specified path expression
     * @param json The text of the JSON document
     * @param path The path expression representing the location of the numeric value in the document
     * @return The numeric value found in the document at the specified path
     * @throws IOException
     */
    private Number lookupNumericValue(final String json, final String path) throws IOException {
        final String jsonPathQuery = convertPath(path);

        logger.debug("json path query = " + jsonPathQuery);

        Number result = null;
        final List<Number> values = JsonPath.read(json, jsonPathQuery);
        if(values != null || values.size() > 0) {
            result = values.get(0);
        }

        return result;
    }

    /**
     * Converts the current (as of 2014-09-09) CareCam path syntax "[x operator y]" to JsonPath syntax "[?(@.x operator y]"
     * and converts the operators (e.g. "=" -> "==").
     * @param originalSpec The CareCam path expression
     * @return A JsonPath expression
     */
    private String convertPath(final String originalSpec) {

        final String pattern = "\\[(.*?)\\]";

        String convertedSpec =
            originalSpec.replaceAll(pattern, "[?(@.$1)]") // find occurrences of [x operator y] and replace with [?(@.x operator y]
                        .replaceAll("=", "==");           // replace operator '=' with '=='

        return String.format("$..%s", convertedSpec);     // prepend the document-global search operator
    }

    /**
     * Creates a Message model object from a randomly-chosen message in a JSON representation of a MessageSet
     * @param jsonMessageSet A JSONObject representing the MessageSet
     * @return A Message model object representing one of the Messages in the MessageSet
     */
    private Message createMessage(final JSONObject jsonMessageSet) {

        final JSONArray messages = (JSONArray) jsonMessageSet.get("message");
        final JSONObject jsonMessage = getRandomJsonObject(messages);

        final Message message = new Message();
        message.setMessageId((Integer) jsonMessage.get("messageId"));
        message.setText((String) jsonMessage.get("text"));

        final Integer tipSetId = (Integer) jsonMessage.get("tipSetId");
        if (tipSetId != null) {
            message.setTipSetId(tipSetId);
        }

        return message;
    }

    /**
     * Creates a Tip model object from a randomly-chosen tip in a JSON representation of a TipSet
     * @param tipSetId The id of the TipSet, to be retrieved from the repository's "tip sets" JSON document
     * @return A Tip model object representing one of the Tips in the TipSet
     */
    private Tip createTip(final int tipSetId) {

        Tip tip = null;

        if (tipSetId > 0) {
            final JSONObject jsonTipSet = lookupTipSet(tipSetId);
            final JSONArray jsonTips = (JSONArray) jsonTipSet.get("tip");

            final JSONObject jsonTip = getRandomJsonObject(jsonTips);

            tip = new Tip();
            tip.setTipId((Integer) jsonTip.get("tipId"));
            tip.setText((String) jsonTip.get("text"));
        }

        return tip;
    }

    /**
     * Creates a List of Button model objects from the 'button' data
     * contained in a JSONObject representing a MessageSet
     * @param jsonMessageSet A JSONObject representing the MessageSet
     * @return A List&lt;Button&gt; representing the buttons in the MessageSet
     */
    private List<Button> createButtons(final JSONObject jsonMessageSet) {
        final List<Button> buttons = new ArrayList<Button>();

        final JSONArray jsonButtons = (JSONArray) jsonMessageSet.get("button");
        for (Object buttonObject : jsonButtons) {
            final JSONObject jsonButton = (JSONObject) buttonObject;
            final Button button = new Button();
            button.setButtonId((Integer) jsonButton.get("buttonId"));
            button.setText((String) jsonButton.get("text"));
            buttons.add(button);
        }

        return buttons;
    }

    /**
     * Get a random entry from a JSONArray of JSONObjects
     * @param jsonArray
     * @return
     */
    private JSONObject getRandomJsonObject(final JSONArray jsonArray) {
        return (JSONObject) jsonArray.get(randInt(0, jsonArray.size() - 1));
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    private static int randInt(final int min, final int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }
}
