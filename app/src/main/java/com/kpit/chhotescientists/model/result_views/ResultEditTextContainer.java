package com.kpit.chhotescientists.model.result_views;

import android.view.View;
import android.widget.EditText;

/**
 * Created by grahamearley on 7/31/16.
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
