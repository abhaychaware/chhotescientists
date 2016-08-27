package com.kpit.chhotescientists.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.kpit.chhotescientists.activity.SessionCheckInActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by grahamearley on 7/19/16.
 */
public class Session implements Parcelable {
    public String date;
    public String location;
    public List<SessionEvent> events;
    public String theme;
    public String expectedStudentCount;
    public String scheduleId;

    public Session(Parcel in) {
        this.date = in.readString();
        this.location = in.readString();

        // Write the stored list to this.events:
        this.events = new ArrayList<>();
        in.readList(this.events, SessionEvent.class.getClassLoader());

        this.theme = in.readString();
        this.expectedStudentCount = in.readString();
        this.scheduleId = in.readString();
    }

    public void passScheduleIdToEvents() {
        if (events != null) {
            for (SessionEvent event : events) {
                event.setScheduleId(scheduleId);
            }
        }
    }

    public String getDateString() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            Date parsedDate = sdf.parse(this.date);
            DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

            // Format according to device's locale:
            return dateFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return this.date;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(location);
        dest.writeList(events);
        dest.writeString(theme);
        dest.writeString(expectedStudentCount);
        dest.writeString(scheduleId);
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };
}
