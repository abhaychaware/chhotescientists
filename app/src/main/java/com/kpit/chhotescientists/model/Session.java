package com.kpit.chhotescientists.model;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by grahamearley on 7/19/16.
 */
public class Session {
    public String date;
    public String location;
    public List<SessionEvent> events;
    public String theme;
    public String expectedStudentCount;
    public String scheduleId;

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
}
