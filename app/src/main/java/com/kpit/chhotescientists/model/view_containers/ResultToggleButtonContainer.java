package com.kpit.chhotescientists.model.view_containers;

import android.view.View;
import android.widget.ToggleButton;

/**
 * See parent class ResultViewContainer for documentation.
 */
public class ResultToggleButtonContainer extends ResultViewContainer {

    private ToggleButton toggleButton;

    public ResultToggleButtonContainer(ToggleButton toggleButton) {
        this.toggleButton = toggleButton;
    }

    @Override
    public String getStringResult() {
        return Boolean.toString(toggleButton.isChecked());
    }

    public View getView() {
        return this.toggleButton;
    }
}
