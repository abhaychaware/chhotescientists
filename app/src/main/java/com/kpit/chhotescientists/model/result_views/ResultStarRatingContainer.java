package com.kpit.chhotescientists.model.result_views;

import android.view.View;
import android.widget.RatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by grahamearley on 7/31/16.
 */
public class ResultStarRatingContainer extends ResultViewContainer {

    private RatingBar ratingBar;

    public ResultStarRatingContainer(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    @Override
    public String getResult() {
        return Integer.toString(ratingBar.getNumStars());
    }

    public View getView() {
        return this.ratingBar;
    }
}
