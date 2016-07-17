package com.kpit.chhotescientists.model;

import android.content.Context;
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

    @Override
    public View getQuestionView(Context context) {
        EditText numberInput = new EditText(context);
        numberInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        numberInput.setMinimumWidth(100);
        return numberInput;
    }
}
