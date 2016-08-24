package com.kpit.chhotescientists.model.view_containers;

import android.text.TextUtils;
import android.view.View;

import com.kpit.chhotescientists.model.CheckInQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for holding onto a View and the view's "result."
 *  A result could be the text a user inputs into the view,
 *  a number from the star rating a user inputs into the view,
 *  a photo the user uploaded, etc.
 *
 *  This class gives us easy access to the View and its corresponding
 *  result in a single interface, so that we can handle all cases
 *  in the same manner in the SessionCheckInActivity.
 */
public abstract class ResultViewContainer {

    private CheckInQuestion question;

    public abstract String getStringResult();

    public abstract View getView();

    /**
     * Add this ResultViewContainer's result to a JSON array as a JSON object.
     *
     * @param dataToSend The JSON array of responses to send to the backend.
     * @return The dataToSend array, but with this ResultViewContainer's response added on.
     * @throws JSONException
     */
    public JSONArray addContentsToTextJsonArray(JSONArray dataToSend) throws JSONException {
        String stringResult = getStringResult();

        // Only send the response if it's not empty / nonzero
        if (!TextUtils.isEmpty(stringResult) && !stringResult.equals("0")) {
            JSONObject viewContentsJson = getBaseJsonDataToSend();
            viewContentsJson.put("response", getStringResult());
            dataToSend.put(viewContentsJson);
        }

        return dataToSend;
    }

    public List<JSONObject> getMediaJsonsToUpload(String eventTypeId, String scheduleId) throws JSONException {        // By default, don't upload any media. Media classes will override this.
        return new ArrayList<>();
    }

    public void setQuestion(CheckInQuestion question) {
        this.question = question;
    }

    public CheckInQuestion getQuestion() {
        return this.question;
    }

    /**
     * A helper method for setting up the base JSON object. It creates a
     *  JSON object and adds in all the fields that are always there by
     *  default.
     * @return The base of a JSON object to use in passing the data to the server.
     * @throws JSONException
     */
    public JSONObject getBaseJsonDataToSend() throws JSONException {
        JSONObject baseJson = new JSONObject();
        baseJson.put("question_id", getQuestion().getQuestionId());
        baseJson.put("question_type", getQuestion().getQuestionType());
        baseJson.put("question", getQuestion().getQuestion());
        return baseJson;
    }
}
