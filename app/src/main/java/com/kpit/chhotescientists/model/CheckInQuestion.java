package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by grahamearley on 7/16/16.
 */
public abstract class CheckInQuestion implements Parcelable {
    public String question;

    public CheckInQuestion(String question) {
        this.question = question;
    }

    public CheckInQuestion(Parcel in) {
        this(in.readString());
    }

    public abstract View getQuestionView(Activity activity);

    public String getQuestionText() {
        return this.question;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
