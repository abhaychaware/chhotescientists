package com.kpit.chhotescientists.model;

import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.widget.RatingBar;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInStarRating extends CheckInQuestion {
    public CheckInStarRating(String question) {
        super(question);
    }

    public CheckInStarRating(Parcel in) {
        super(in);
    }

    @Override
    public View getQuestionView(Context context) {
        RatingBar ratingBar = new RatingBar(context);
        ratingBar.setNumStars(5);
        return ratingBar;
    }

    public static final Creator<CheckInStarRating> CREATOR = new Creator<CheckInStarRating>() {
        @Override
        public CheckInStarRating createFromParcel(Parcel in) {
            return new CheckInStarRating(in);
        }

        @Override
        public CheckInStarRating[] newArray(int size) {
            return new CheckInStarRating[size];
        }
    };
}
