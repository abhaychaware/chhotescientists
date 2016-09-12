package org.chhotescientists.model.view_containers;

import android.view.View;
import android.widget.RatingBar;

/**
 * See parent class ResultViewContainer for documentation.
 */
public class ResultStarRatingContainer extends ResultViewContainer {

    private RatingBar ratingBar;

    public ResultStarRatingContainer(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    @Override
    public String getStringResult() {
        return Integer.toString((int) ratingBar.getRating());
    }

    public View getView() {
        return this.ratingBar;
    }
}
