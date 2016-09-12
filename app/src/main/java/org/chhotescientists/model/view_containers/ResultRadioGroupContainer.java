package org.chhotescientists.model.view_containers;

import android.view.View;
import android.widget.RadioGroup;

import com.kpit.chhotescientists.R;

/**
 * See parent class ResultViewContainer for documentation.
 */
public class ResultRadioGroupContainer extends ResultViewContainer {

    private RadioGroup radioGroup;

    public ResultRadioGroupContainer(RadioGroup radioGroup) {
        this.radioGroup = radioGroup;
    }

    @Override
    public String getStringResult() {
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if (checkedId == R.id.radio_button_yes) {
            return "Yes";
        } else if (checkedId == R.id.radio_button_no) {
            return "No";
        } else {
            return null;
        }
    }

    public View getView() {
        return this.radioGroup;
    }
}
