package com.kpit.chhotescientists.model;

import android.content.Context;
import android.view.View;

/**
 * Created by grahamearley on 7/16/16.
 */
public abstract class CheckInQuestion {
    public String question;

    public CheckInQuestion(String question) {
        this.question = question;
    }

    public abstract View getQuestionView(Context context);

    public String getQuestionText() {
        return this.question;
    }
}
