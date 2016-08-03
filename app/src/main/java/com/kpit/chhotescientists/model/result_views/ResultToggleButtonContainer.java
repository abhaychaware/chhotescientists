package com.kpit.chhotescientists.model.result_views;

import android.view.View;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * See parent class ResultViewContainer for documentation.
 */
public class ResultToggleButtonContainer extends ResultViewContainer {

    private ToggleButton toggleButton;

    public ResultToggleButtonContainer(ToggleButton toggleButton) {
        this.toggleButton = toggleButton;
    }

    @Override
    public String getResult() {
        return Boolean.toString(toggleButton.isChecked());
    }

    public View getView() {
        return this.toggleButton;
    }
}
