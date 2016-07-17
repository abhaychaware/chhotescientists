package com.kpit.chhotescientists.model;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInStarRating extends CheckInQuestion {
    public CheckInStarRating(String question) {
        super(question);
    }

    @Override
    public View getQuestionView(Context context) {
        RatingBar ratingBar = new RatingBar(context);
        ratingBar.setNumStars(5);
        return ratingBar;
    }
}
