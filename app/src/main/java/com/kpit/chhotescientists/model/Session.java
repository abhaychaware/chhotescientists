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
    String theme;

    @SerializedName("expected_student_count")
    String expectedStudentCount;

    @SerializedName("schedule_id")
    String scheduleId;
}
