package com.kpit.chhotescientists.model.result_views;

import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by grahamearley on 7/31/16.
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
