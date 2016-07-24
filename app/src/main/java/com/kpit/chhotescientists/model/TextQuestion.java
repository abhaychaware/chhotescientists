package com.kpit.chhotescientists.model;

import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.widget.EditText;

/**
 * Created by grahamearley on 7/16/16.
 */
public class TextQuestion extends CheckInQuestion {

    public TextQuestion(String question) {
        super(question);
    }

    public TextQuestion(Parcel in) {
        super(in);
    }

    @Override
    public View getQuestionView(Context context) {
        return new EditText(context);
    }

    public static final Creator<TextQuestion> CREATOR = new Creator<TextQuestion>() {
        @Override
        public TextQuestion createFromParcel(Parcel in) {
            return new TextQuestion(in);
        }

        @Override
        public TextQuestion[] newArray(int size) {
            return new TextQuestion[size];
        }
    };
}
