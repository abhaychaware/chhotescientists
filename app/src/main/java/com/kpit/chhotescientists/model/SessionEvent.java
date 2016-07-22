package com.kpit.chhotescientists.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by grahamearley on 7/19/16.
 */
public class SessionEvent implements Parcelable {
    public String title;
    public List<CheckInQuestion> questions;

    protected SessionEvent(Parcel in) {
        title = in.readString();

        // Write the stored list to this.questions:
        in.readList(questions, List.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeList(questions);
    }

    public static final Creator<SessionEvent> CREATOR = new Creator<SessionEvent>() {
        @Override
        public SessionEvent createFromParcel(Parcel in) {
            return new SessionEvent(in);
        }

        @Override
        public SessionEvent[] newArray(int size) {
            return new SessionEvent[size];
        }
    };
}
