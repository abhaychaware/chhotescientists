package com.kpit.chhotescientists.model;

import com.google.gson.annotations.SerializedName;

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
}
