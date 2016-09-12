package org.chhotescientists.model;

import android.app.Activity;
import android.os.Parcel;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kpit.chhotescientists.R;
import org.chhotescientists.model.view_containers.ResultRadioGroupContainer;
import org.chhotescientists.model.view_containers.ResultViewContainer;

/**
 * See parent CheckInQuestion for documentation.
 */
public class BooleanQuestion extends CheckInQuestion {
    public static final String QUESTION_TYPE = "boolean";

    public BooleanQuestion(String question) {
        super(question);
    }

    public BooleanQuestion(Parcel in) {
        super(in);
    }

    @Override
    public ResultViewContainer getQuestionViewContainer(Activity activity) {
        RadioGroup radioGroup = new RadioGroup(activity);

        RadioButton yesButton = new RadioButton(activity);
        yesButton.setId(R.id.radio_button_yes);
        RadioButton noButton = new RadioButton(activity);
        noButton.setId(R.id.radio_button_no);

        yesButton.setText(activity.getString(R.string.yes));
        noButton.setText(activity.getString(R.string.no));

        radioGroup.addView(yesButton);
        radioGroup.addView(noButton);

        return new ResultRadioGroupContainer(radioGroup);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String getQuestionType() {
        return QUESTION_TYPE;
    }

    public static final Creator<BooleanQuestion> CREATOR = new Creator<BooleanQuestion>() {
        @Override
        public BooleanQuestion createFromParcel(Parcel in) {
            return new BooleanQuestion(in);
        }

        @Override
        public BooleanQuestion[] newArray(int size) {
            return new BooleanQuestion[size];
        }
    };

    // @graham Idea: Could add conditional questions here
    //  (e.g. answer yes ==> more questions appear.
    //        call method here to load further Q items)
}
