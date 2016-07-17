package com.kpit.chhotescientists.model;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInTextQuestion extends CheckInQuestion {

    public CheckInTextQuestion(String question) {
        super(question);
    }

    @Override
    public View getQuestionView(Context context) {
        return new EditText(context);
    }
}
