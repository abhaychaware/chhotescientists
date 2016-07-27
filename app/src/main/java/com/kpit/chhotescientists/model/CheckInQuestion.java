package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.kpit.chhotescientists.interfaces.ViewResultReceiver;

/**
 * Created by grahamearley on 7/16/16.
 */
public abstract class CheckInQuestion implements Parcelable {
    public String question;
    protected ViewResultReceiver viewResultReceiver;

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

    public void setViewResultReceiver(ViewResultReceiver viewResultReceiver) {
        this.viewResultReceiver = viewResultReceiver;
    }
}
