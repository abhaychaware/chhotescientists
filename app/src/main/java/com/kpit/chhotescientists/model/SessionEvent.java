package com.kpit.chhotescientists.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grahamearley on 7/19/16.
 */
public class SessionEvent implements Parcelable {
    public String title;
    public List<CheckInQuestion> questions;
    public String eventTypeId;
    private String scheduleId;

    protected SessionEvent(Parcel in) {
        title = in.readString();
        questions = new ArrayList<>();

        // Write the stored list to this.questions:
        in.readList(questions, CheckInQuestion.class.getClassLoader());
        eventTypeId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeList(questions);
        dest.writeString(eventTypeId);
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

    public String getId() {
        return eventTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleId() {
        return this.scheduleId;
    }
}
