package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.os.Parcel;
import android.widget.ToggleButton;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.model.view_containers.ResultToggleButtonContainer;
import com.kpit.chhotescientists.model.view_containers.ResultViewContainer;

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
        ToggleButton toggleButton = new ToggleButton(activity);
        toggleButton.setTextOff(activity.getString(R.string.no));
        toggleButton.setTextOn(activity.getString(R.string.yes));
        toggleButton.setChecked(false);
        return new ResultToggleButtonContainer(toggleButton);
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
