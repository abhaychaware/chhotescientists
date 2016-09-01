package com.kpit.chhotescientists.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.common.MyPreferences;
import com.kpit.chhotescientists.util.ConnectionDetector;
import com.kpit.chhotescientists.validation.Universal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vb on 4/11/2016.
 */
@SuppressWarnings("ALL")
public class FeedbackActivity extends AppCompatActivity {

    EditText edtFullname, edtEmail, edtMobile, edtQuery;
    MyPreferences myPreferences;
    ProgressDialog pDialog;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        myPreferences = new MyPreferences(this);
        //enable back button on action bar
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtFullname = (EditText) findViewById(R.id.input_fullname);
        edtEmail = (EditText) findViewById(R.id.input_email);
        edtMobile = (EditText) findViewById(R.id.input_mobile);
        edtQuery = (EditText) findViewById(R.id.input_message);

        edtFullname.setText(myPreferences.getUserFullname());
        edtEmail.setText(myPreferences.getUserEmail());
        edtMobile.setText(myPreferences.getUserMobile());

        btnSend = (Button) findViewById(R.id.btn_send_contact);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ChnagePasswordActivity.this, "Change Password btn hit triggered", Toast.LENGTH_SHORT).show();

                if (isValidation()) {
                    if (ConnectionDetector.isConnectingToInternet(FeedbackActivity.this)) {

                        new ExecuteSendFeedback().execute(getString(R.string.send_contact_feedback));

                    } else {
                        Toast.makeText(FeedbackActivity.this,
                                getString(R.string.no_internet_msg),
                                Toast.LENGTH_LONG).show();
                    }
                } else {

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_left_animation,
                R.anim.out_right_animation);

    }

    public boolean isValidation() {

        if (!Universal.isEmpty(edtQuery)) {
            edtQuery.setError(getString(R.string.val_enter_feedback_query));
            return false;
        }

        return true;
    }

    private class ExecuteSendFeedback extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FeedbackActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // Creating service handler class instance
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(getString(R.string.send_contact_feedback));
                StringEntity se = new StringEntity(assembleTextJson().toString());
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());
                Log.e("Server JOSN", result);
                return result;
            } catch (JSONException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
                return null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
        private JSONObject assembleTextJson() throws JSONException {
            // This nested structure is based on the endpoint spec.
            //   Could probably use optimization at some point. TODO

            JSONObject textDataToSend = new JSONObject();
            JSONArray dataJsonArray = new JSONArray();
            JSONObject queryJson = new JSONObject();
            queryJson.put("user_id", myPreferences.getUserId());
            queryJson.put("query", edtQuery.getText().toString().trim());

            dataJsonArray.put(queryJson);
            textDataToSend.put("data", dataJsonArray);
            Log.e("JSON Sent", textDataToSend.toString());

            return textDataToSend;

        }
        @Override
        protected void onPostExecute(String data) {
            Log.e("Server Response", data + "--");

            int statusflag = 0;
            JSONArray resAuth;
            JSONObject jsonObj , feedObj= null;
            try {
                jsonObj = new JSONObject(data);
                resAuth = jsonObj.getJSONArray("data");
                feedObj = resAuth.getJSONObject(0);
                statusflag = feedObj.getString("status").equalsIgnoreCase("success")?1:0;
                Log.e("response status flag", String.valueOf(statusflag));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (statusflag == 1) {

                Toast.makeText(FeedbackActivity.this,
                        "Feedback Submitted Successfully.", Toast.LENGTH_SHORT)
                        .show();


                onBackPressed();
            } else {
                //pDialog.dismiss();
                Toast.makeText(FeedbackActivity.this,
                        "Unable to submit feedback, Sorry for inconvience.", Toast.LENGTH_SHORT)
                        .show();

            }


        }
    }
}