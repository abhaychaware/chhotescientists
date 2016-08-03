package com.kpit.chhotescientists.model.result_views;

import android.view.View;

import com.kpit.chhotescientists.model.CheckInQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by grahamearley on 7/31/16.
 */
public abstract class ResultViewContainer {

    private CheckInQuestion question;

    public abstract String getResult();

    public abstract View getView();

    public JSONArray addContentsToTextJsonArray(JSONArray dataToSend) throws JSONException {
        JSONObject viewContentsJson = getBaseJsonDataToSend();
        viewContentsJson.put("response", getResult());

        dataToSend.put(viewContentsJson);

        return dataToSend;
    }

    public JSONObject getMediaJsonToUpload(String eventTypeId, String scheduleId) throws JSONException {
        // By default, don't upload any media. Media classes will override this.
        return null;
    }

    public void setQuestion(CheckInQuestion question) {
        this.question = question;
    }

    public CheckInQuestion getQuestion() {
        return this.question;
    }

    public JSONObject getBaseJsonDataToSend() throws JSONException {
        JSONObject baseJson = new JSONObject();
        baseJson.put("question_id", getQuestion().getQuestionId());
        baseJson.put("question_type", getQuestion().getQuestionType());
        baseJson.put("question", getQuestion().getQuestion());
        return baseJson;
    }
}
