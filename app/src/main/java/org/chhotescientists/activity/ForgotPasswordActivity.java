package org.chhotescientists.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kpit.chhotescientists.R;
import org.chhotescientists.common.MyPreferences;
import org.chhotescientists.util.ConnectionDetector;
import org.chhotescientists.validation.Universal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vb on 4/15/2016.
 */
@SuppressWarnings("ALL")
public class ForgotPasswordActivity extends AppCompatActivity {

    //Declaration
    Button btnlogin;
    TextView txtSignup;
    EditText edtUsername;
    String username;
    JSONArray resAuth;
    private MyPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtUsername = (EditText) findViewById(R.id.input_mobile);

        btnlogin = (Button) findViewById(R.id.btn_login);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidation()) {

                    if (ConnectionDetector
                            .isConnectingToInternet(ForgotPasswordActivity.this)) {

                        new ExecuteUserForgotPasswordTask().execute();

                    } else {
                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                getString(R.string.no_internet_msg),
                                Toast.LENGTH_LONG).show();
                    }

                } else {

                }


            }


        });
    }

    //registration validation
    public boolean isValidation() {

        if (!Universal.isEmpty(edtUsername)) {
            edtUsername.setError("Please Enter Email/Mobile");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_left_animation,
                R.anim.out_right_animation);

    }

    private class ExecuteUserForgotPasswordTask extends
            AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(
                    ForgotPasswordActivity.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(getString(R.string.forgot_password_url));
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
            queryJson.put("user_mobile", edtUsername.getText().toString().trim());
            queryJson.put("user_name", edtUsername.getText().toString().trim());

            dataJsonArray.put(queryJson);
            textDataToSend.put("data", dataJsonArray);
            Log.e("JSON Sent", textDataToSend.toString());

            return textDataToSend;

        }
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

            if (statusflag != 0) {

                mProgressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this,
                        "Password sent on your registered email address.", Toast.LENGTH_SHORT)
                        .show();
                onBackPressed();
            } else {
                mProgressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this,
                        "Forgot Password Assistance Unsuccessful, Sorry for inconvience.", Toast.LENGTH_SHORT)
                        .show();

            }

        }

    }

}