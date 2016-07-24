package com.kpit.chhotescientists.model;

import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.widget.RatingBar;

/**
 * Created by grahamearley on 7/16/16.
 */
public class StarRatingQuestion extends CheckInQuestion {
    public StarRatingQuestion(String question) {
        super(question);
    }

    public StarRatingQuestion(Parcel in) {
        super(in);
    }

    @Override
    public View getQuestionView(Context context) {
        RatingBar ratingBar = new RatingBar(context);
        ratingBar.setNumStars(5);
        return ratingBar;
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
