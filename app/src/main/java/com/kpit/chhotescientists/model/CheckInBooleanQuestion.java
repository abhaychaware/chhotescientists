package com.kpit.chhotescientists.model;

import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInBooleanQuestion extends CheckInQuestion {
    public CheckInBooleanQuestion(String question) {
        super(question);
    }

    public CheckInBooleanQuestion(Parcel in) {
        super(in.readString());
    }

    @Override
    public View getQuestionView(Context context) {
        return new ToggleButton(context);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
    }

    public static final Creator<CheckInBooleanQuestion> CREATOR = new Creator<CheckInBooleanQuestion>() {
        @Override
        public CheckInBooleanQuestion createFromParcel(Parcel in) {
            return new CheckInBooleanQuestion(in);
        }

        @Override
        public CheckInBooleanQuestion[] newArray(int size) {
            return new CheckInBooleanQuestion[size];
        }
    };

    // @graham Idea: Could add conditional questions here
    //  (e.g. answer yes ==> more questions appear.
    //        call method here to load further Q items)
}
