package com.kpit.chhotescientists.model;

import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.widget.EditText;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInTextQuestion extends CheckInQuestion {

    public CheckInTextQuestion(String question) {
        super(question);
    }

    public CheckInTextQuestion(Parcel in) {
        super(in);
    }

    @Override
    public View getQuestionView(Context context) {
        return new EditText(context);
    }

    public static final Creator<CheckInTextQuestion> CREATOR = new Creator<CheckInTextQuestion>() {
        @Override
        public CheckInTextQuestion createFromParcel(Parcel in) {
            return new CheckInTextQuestion(in);
        }

        @Override
        public CheckInTextQuestion[] newArray(int size) {
            return new CheckInTextQuestion[size];
        }
    };
}
