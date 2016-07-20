package com.kpit.chhotescientists.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInItem {
    // TODO: Use GSON. @Expose, etc.

    // TODO: DELETE! Replace with SessionEvent!
    List<CheckInQuestion> questions;

    public CheckInItem() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(CheckInQuestion question) {
        this.questions.add(question);
    }

    public List<CheckInQuestion> getQuestions() {
        return this.questions;
    }
}
