package com.kpit.chhotescientists.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.interfaces.ViewResultReceiver;
import com.kpit.chhotescientists.model.CheckInQuestion;
import com.kpit.chhotescientists.model.SessionEvent;

import java.io.IOException;

public class SessionCheckInActivity extends AppCompatActivity implements ViewResultReceiver {

    public static final String EVENT_KEY = "EVENT";
    public static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewAwaitingResult;

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

            View questionView = question.getQuestionView(this);

            // Set this activity as the question's ViewResultReceiver,
            //  so the question's view can send a view to this activity to be
            //  filled in by the activity upon receiving a result
            question.setViewResultReceiver(this);

            questionsLayout.addView(questionView);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                if (this.imageViewAwaitingResult != null) {
                    this.imageViewAwaitingResult.setImageBitmap(bitmap);
                    this.imageViewAwaitingResult = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setImageViewAwaitingResult(ImageView imageViewAwaitingResult) {
        this.imageViewAwaitingResult = imageViewAwaitingResult;
    }
}
