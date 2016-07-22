package com.kpit.chhotescientists.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.model.CheckInQuestion;
import com.kpit.chhotescientists.model.SessionEvent;

public class SessionCheckInActivity extends AppCompatActivity {

    public static final String EVENT_KEY = "EVENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_check_in);

        SessionEvent event = getIntent().getParcelableExtra(EVENT_KEY);

        LinearLayout questionsLayout = (LinearLayout) findViewById(R.id.check_in_questions_layout);

        for (CheckInQuestion question : event.questions) {
            TextView questionTextView = new TextView(this);
            questionTextView.setText(question.getQuestionText());

            questionsLayout.addView(questionTextView);
            questionsLayout.addView(question.getQuestionView(this));
        }

    }
}
