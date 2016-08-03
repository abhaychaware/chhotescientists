package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.kpit.chhotescientists.model.result_views.ResultEditTextContainer;
import com.kpit.chhotescientists.model.result_views.ResultViewContainer;

/**
 * See parent CheckInQuestion for documentation.
 */
public class NumberQuestion extends CheckInQuestion {
    public static final String QUESTION_TYPE = "number";

    public NumberQuestion(String question) {
        super(question);
    }

    public NumberQuestion(Parcel in) {
        super(in);
    }

    @Override
    public ResultViewContainer getQuestionViewContainer(Activity activity) {
        EditText numberInput = new EditText(activity);
        numberInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        numberInput.setHint("Enter number here.");
        return new ResultEditTextContainer(numberInput);
    }

    @Override
    public String getQuestionType() {
        return QUESTION_TYPE;
    }

    public static final Creator<NumberQuestion> CREATOR = new Creator<NumberQuestion>() {
        @Override
        public NumberQuestion createFromParcel(Parcel in) {
            return new NumberQuestion(in);
        }

        @Override
        public NumberQuestion[] newArray(int size) {
            return new NumberQuestion[size];
        }
    };
}
