package com.kpit.chhotescientists.model;

import android.content.Context;
import android.os.Parcel;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInNumberQuestion extends CheckInQuestion {
    public CheckInNumberQuestion(String question) {
        super(question);
    }

    public CheckInNumberQuestion(Parcel in) {
        super(in);
    }

    @Override
    public View getQuestionView(Context context) {
        EditText numberInput = new EditText(context);
        numberInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        numberInput.setHint("Enter number here.");
        return numberInput;
    }

    public static final Creator<CheckInNumberQuestion> CREATOR = new Creator<CheckInNumberQuestion>() {
        @Override
        public CheckInNumberQuestion createFromParcel(Parcel in) {
            return new CheckInNumberQuestion(in);
        }

        @Override
        public CheckInNumberQuestion[] newArray(int size) {
            return new CheckInNumberQuestion[size];
        }
    };
}
