package org.chhotescientists.model.view_containers;

import android.view.View;
import android.widget.EditText;

/**
 * See parent class ResultViewContainer for documentation.
 */
public class ResultEditTextContainer extends ResultViewContainer {

    private EditText editText;

    public ResultEditTextContainer(EditText editText) {
        this.editText = editText;
    }

    @Override
    public String getStringResult() {
        return editText.getText().toString();
    }

    public View getView() {
        return this.editText;
    }
}
