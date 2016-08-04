package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.widget.RatingBar;

import com.kpit.chhotescientists.model.result_views.ResultStarRatingContainer;
import com.kpit.chhotescientists.model.result_views.ResultViewContainer;

/**
 * See parent CheckInQuestion for documentation.
 */
public class StarRatingQuestion extends CheckInQuestion {
    public static final String QUESTION_TYPE = "star_rating";

    public StarRatingQuestion(String question) {
        super(question);
    }

    public StarRatingQuestion(Parcel in) {
        super(in);
    }

    @Override
    public ResultViewContainer getQuestionViewContainer(Activity activity) {
        RatingBar ratingBar = new RatingBar(activity);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);
        return new ResultStarRatingContainer(ratingBar);
    }

    @Override
    public String getQuestionType() {
        return QUESTION_TYPE;
    }

    public static final Creator<StarRatingQuestion> CREATOR = new Creator<StarRatingQuestion>() {
        @Override
        public StarRatingQuestion createFromParcel(Parcel in) {
            return new StarRatingQuestion(in);
        }

        @Override
        public StarRatingQuestion[] newArray(int size) {
            return new StarRatingQuestion[size];
        }
    };
}
