package com.kpit.chhotescientists.model;

import android.content.Context;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInBooleanQuestion extends CheckInQuestion {
    public CheckInBooleanQuestion(String question) {
        super(question);
    }

    @Override
    public View getQuestionView(Context context) {
        return new ToggleButton(context);
    }

    // @graham Idea: Could add conditional questions here
    //  (e.g. answer yes ==> more questions appear.
    //        call method here to load further Q items)
}
