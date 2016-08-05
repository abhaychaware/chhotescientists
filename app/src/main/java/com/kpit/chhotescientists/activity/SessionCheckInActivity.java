package com.kpit.chhotescientists.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.interfaces.ResultViewContainerReceiver;
import com.kpit.chhotescientists.model.CheckInQuestion;
import com.kpit.chhotescientists.model.SessionEvent;
import com.kpit.chhotescientists.model.result_views.ResultMediaButtonContainer;
import com.kpit.chhotescientists.model.result_views.ResultViewContainer;
import com.kpit.chhotescientists.util.AppController;
import com.kpit.chhotescientists.util.ConnectionDetector;

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

        // Each Question in the Event has a view for accepting the user's input (e.g. an EditText or a Toggle).
        //  Go through these Questions and add a TextView for the question's text, along with the Question's
        //  input view.
        for (CheckInQuestion question : event.questions) {
            TextView questionTextView = new TextView(this);
            questionTextView.setText(question.getQuestionText());
            questionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            questionsLayout.addView(questionTextView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            ResultViewContainer viewContainer = question.getQuestionViewContainer(this);
            viewContainer.setQuestion(question);

            // Set this activity as the question's ResultViewContainerReceiver,
            //  so the question's view can send a view to this activity to be
            //  filled in by the activity upon receiving a result
            question.setViewResultReceiver(this);
            // ^(used for users' media upload selections)

            viewContainers.add(viewContainer);

            LinearLayout.LayoutParams questionViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            questionViewParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.space_16dp);
            questionsLayout.addView(viewContainer.getView(), questionViewParams);
        }

        Button submitButton = new Button(this);
        submitButton.setText(getString(R.string.submit));

        if (ConnectionDetector.isConnectingToInternet(this)) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // Upload text responses to questions:
                        JSONObject questionsTextJson = assembleTextJson(event);
                        Log.d("Text Response", questionsTextJson.toString());
                        uploadTextResponses(questionsTextJson);

                        // Also upload any media if possible
                        uploadMediaResponses(event);

                        // Close the activity after sending.
                        onBackPressed();
                    } catch (JSONException e) {
                        Toast.makeText(SessionCheckInActivity.this, R.string.error_sending_response_try_again, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        } else {
            submitButton.setEnabled(false);
            submitButton.setText(R.string.must_be_connected_to_internet);
        }

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.space_32dp);
        buttonParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.space_16dp);

        questionsLayout.addView(submitButton, buttonParams);
    }

    /**
     * Send the text-based question responses to the backend.
     * @param questionsTextJson The JSON object of question responses.
     */
    private void uploadTextResponses(JSONObject questionsTextJson) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.upload_event_questionnaire), questionsTextJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SessionCheckInActivity.this, R.string.questionnaire_upoaded_successfully, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SessionCheckInActivity.this, R.string.questionnaire_upload_error, Toast.LENGTH_LONG).show();
                    }
                });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /**
     * Send a media-based question response to the backend.
     * @param questionsMediaJson The JSON object of a media response.
     */
    private void uploadMediaResponse(JSONObject questionsMediaJson) {
        Log.d("SessionCheckInActivity", "Uploading media JSON: " + questionsMediaJson.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.upload_event_media), questionsMediaJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SessionCheckInActivity.this, R.string.media_uploaded_successfully, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SessionCheckInActivity.this, R.string.error_uploading_media, Toast.LENGTH_LONG).show();
                    }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /**
     * Send all media-based question responses to the backend. Each piece of media
     *  is sent in a single response.
     * @param event The Event containing the questions, some of which may have media responses.
     * @throws JSONException if there is an issue with the JSON.
     */
    private void uploadMediaResponses(SessionEvent event) throws JSONException {
        for (ResultViewContainer container : viewContainers) {
            JSONObject mediaJsonToSend = container.getMediaJsonToUpload(event.getId(), event.getScheduleId());
            if (mediaJsonToSend != null) {
                // Send the media file(s)
                uploadMediaResponse(mediaJsonToSend);
            }
        }
    }

    /**
     * Go through all the Questions in an Event, and build a properly-formatted JSON object
     *  based on the user's responses to these Questions.
     * @param event The event containing the questions to gather answers from.
     * @return A JSON object of all the questions' user responses, properly formatted for the backend.
     * @throws JSONException if there's any error with the JSON object.
     */
    private JSONObject assembleTextJson(SessionEvent event) throws JSONException {
        // Create an empty JSON array, and build it up by passing it through
        //  all questions' view containers and adding those view containers' results
        //  to the array.
        JSONArray questionsTextArray = new JSONArray();
        for (ResultViewContainer container : viewContainers) {
            questionsTextArray = container.addContentsToTextJsonArray(questionsTextArray);
        }

        // Now build the outer layers of the JSON:

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

        sessionJson.put("schedule_id", event.getScheduleId());
        sessionJson.put("events", eventWrapperJson);

        dataJsonArray.put(sessionJson);
        textDataToSend.put("data", dataJsonArray);

        return textDataToSend;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get an image from the chooser, and add it to the corresponding media button's ImageView
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // If there is a mediaButtonContainer awaiting a result,
                //  then put this result in that container's ImageView.
                if (this.mediaButtonContainerAwaitingResult != null) {
                    this.mediaButtonContainerAwaitingResult.addImage(bitmap);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Override default behavior of the action bar's "Up Arrow"
            //  to go back to the previous page rather than resetting
            //  the activity.
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
