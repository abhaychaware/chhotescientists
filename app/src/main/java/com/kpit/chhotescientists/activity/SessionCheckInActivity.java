package com.kpit.chhotescientists.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.interfaces.ResultViewContainerReceiver;
import com.kpit.chhotescientists.model.CheckInQuestion;
import com.kpit.chhotescientists.model.SessionEvent;
import com.kpit.chhotescientists.model.result_views.ResultMediaButtonContainer;
import com.kpit.chhotescientists.model.result_views.ResultViewContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SessionCheckInActivity extends AppCompatActivity implements ResultViewContainerReceiver {

    public static final String EVENT_KEY = "EVENT";
    public static final int PICK_IMAGE_REQUEST = 1;

    private List<ResultViewContainer> viewContainers;

    private ResultMediaButtonContainer mediaButtonContainerAwaitingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_check_in);

        final SessionEvent event = getIntent().getParcelableExtra(EVENT_KEY);

        viewContainers = new ArrayList<>();

        final LinearLayout questionsLayout = (LinearLayout) findViewById(R.id.check_in_questions_layout);

        for (CheckInQuestion question : event.questions) {
            TextView questionTextView = new TextView(this);
            questionTextView.setText(question.getQuestionText());
            questionsLayout.addView(questionTextView);

            ResultViewContainer viewContainer = question.getQuestionViewContainer(this);
            viewContainer.setQuestion(question);

            // Set this activity as the question's ResultViewContainerReceiver,
            //  so the question's view can send a view to this activity to be
            //  filled in by the activity upon receiving a result
            question.setViewResultReceiver(this);

            viewContainers.add(viewContainer);
            questionsLayout.addView(viewContainer.getView());
        }

        Button submitButton = new Button(this);
        submitButton.setText("Submit");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Upload text responses to questions:
                    JSONObject questionsTextJson = assembleTextJson(event);
                    Log.d("Text Response", questionsTextJson.toString());

                    // Also upload any media if possible
                    uploadMediaResponses(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        questionsLayout.addView(submitButton);
    }

    private void uploadMediaResponses(SessionEvent event) throws JSONException {
        for (ResultViewContainer container : viewContainers) {
            JSONObject mediaJsonToSend = container.getMediaJsonToUpload(event.getId(), "SCHED ID");
            if (mediaJsonToSend != null) {
                // send it.
            }
        }
    }

    private JSONObject assembleTextJson(SessionEvent event) throws JSONException {
        JSONArray questionsTextArray = new JSONArray();

        for (ResultViewContainer container : viewContainers) {
            questionsTextArray = container.addContentsToTextJsonArray(questionsTextArray);
        }

        // This nested structure is based on the endpoint spec.
        //   Could probably use optimization at some point. TODO
        JSONObject textDataToSend = new JSONObject();
        JSONArray dataJsonArray = new JSONArray();
        JSONObject sessionJson = new JSONObject();

        JSONObject eventJson = new JSONObject();
        eventJson.put("event_type_id", event.getId());
        eventJson.put("title", event.getTitle());
        eventJson.put("questions", questionsTextArray);

        JSONArray eventWrapperJson = new JSONArray();
        eventWrapperJson.put(eventJson);

        sessionJson.put("schedule_id", "TODO: IMPLEMENT THIS!");
        sessionJson.put("events", eventWrapperJson);

        dataJsonArray.put(sessionJson);
        textDataToSend.put("data", dataJsonArray);

        return textDataToSend;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                if (this.mediaButtonContainerAwaitingResult != null) {
                    this.mediaButtonContainerAwaitingResult.setImageWithFile(bitmap);
                    this.mediaButtonContainerAwaitingResult = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setMediaButtonContainerAwaitingResult(ResultMediaButtonContainer mediaButtonContainer) {
        this.mediaButtonContainerAwaitingResult = mediaButtonContainer;
    }
}
