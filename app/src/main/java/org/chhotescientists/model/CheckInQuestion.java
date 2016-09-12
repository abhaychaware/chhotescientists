package org.chhotescientists.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import org.chhotescientists.interfaces.ResultViewContainerReceiver;
import org.chhotescientists.model.view_containers.ResultViewContainer;

/**
 * A model to be constructed (with GSON) from the server response.
 *  The model also contains a method for getting the question's
 *  input view (such as an EditText or a photo upload button).
 *
 *  See subclasses!
 */
public abstract class CheckInQuestion implements Parcelable {
    public String question;
    String questionId;

    protected ResultViewContainerReceiver viewResultReceiver;

    public CheckInQuestion(String question) {
        this.question = question;
    }

    public CheckInQuestion(Parcel in) {
        question = in.readString();
        questionId = in.readString();
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
        dest.writeString(this.questionId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setViewResultReceiver(ResultViewContainerReceiver viewResultReceiver) {
        this.viewResultReceiver = viewResultReceiver;
    }

    public abstract String getQuestionType();
}
