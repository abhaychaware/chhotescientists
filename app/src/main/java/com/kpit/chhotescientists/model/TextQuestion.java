package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.widget.EditText;

import com.kpit.chhotescientists.model.result_views.ResultEditTextContainer;
import com.kpit.chhotescientists.model.result_views.ResultViewContainer;

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
    public ResultViewContainer getQuestionViewContainer(Activity activity) {
        EditText editText = new EditText(activity);
        return new ResultEditTextContainer(editText);
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
