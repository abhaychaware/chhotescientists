package com.kpit.chhotescientists.model.result_views;

import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * See parent class ResultViewContainer for documentation.
 */
public class ResultEditTextContainer extends ResultViewContainer {

    private EditText editText;

    public ResultEditTextContainer(EditText editText) {
        this.editText = editText;
    }

    @Override
    public String getResult() {
        return editText.getText().toString();
    }

    public View getView() {
        return this.editText;
    }
}
