package com.kpit.chhotescientists.model;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by grahamearley on 7/19/16.
 */
public class Session {
    public String date;
    public String location;
    public List<SessionEvent> events;
}
