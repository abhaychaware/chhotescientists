package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by grahamearley on 7/16/16.
 */
public class BooleanQuestion extends CheckInQuestion {
    public BooleanQuestion(String question) {
        super(question);
    }

    public BooleanQuestion(Parcel in) {
        super(in.readString());
    }

    @Override
    public View getQuestionView(Activity activity) {
        return new ToggleButton(activity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
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
