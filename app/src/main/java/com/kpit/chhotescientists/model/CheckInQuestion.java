package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.kpit.chhotescientists.interfaces.ResultViewContainerReceiver;
import com.kpit.chhotescientists.model.result_views.ResultViewContainer;

/**
 * Created by grahamearley on 7/16/16.
 */
public abstract class CheckInQuestion implements Parcelable {

    @SerializedName("question_id")
    String questionId;

    @SerializedName("question_type")
    String questionType;

    public String question;
    protected ResultViewContainerReceiver viewResultReceiver;

    public CheckInQuestion(String question) {
        this.question = question;
    }

    public CheckInQuestion(Parcel in) {
        this(in.readString());
    }

    public abstract ResultViewContainer getQuestionViewContainer(Activity activity);

    public String getQuestionText() {
        return this.question;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setViewResultReceiver(ResultViewContainerReceiver viewResultReceiver) {
        this.viewResultReceiver = viewResultReceiver;
    }

    public String getQuestionType() {
        return questionType;
    }
}
