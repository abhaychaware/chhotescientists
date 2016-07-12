package com.kpit.chhotescientists.activity;

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
import com.kpit.chhotescientists.common.MyPreferences;
import com.kpit.chhotescientists.util.ConnectionDetector;
import com.kpit.chhotescientists.validation.Universal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

                        new ExecuteUserForgotPasswordTask().execute(getString(R.string.forgot_password_url));

                    } else {
                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                getString(R.string.no_internet_msg),
                                Toast.LENGTH_SHORT).show();
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

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);

                List parameters = new ArrayList(2);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("user_mobile", edtUsername.getText().toString().trim());
                jsonObject.put("user_email", edtUsername.getText().toString().trim());

                parameters.add(new BasicNameValuePair("data", jsonObject
                        .toString()));
                Log.e("JSON Sent", jsonObject.toString());
                httpPost.setEntity(new UrlEncodedFormEntity(parameters));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                String responce = EntityUtils
                        .toString(httpResponse.getEntity());
                Log.e("Server JOSN", responce);
                return responce;
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

        protected void onPostExecute(String response) {
            Log.e("Server Response", response + "--");

            int flag = 0;
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(response);
                resAuth = jsonObj.getJSONArray("data");
                JSONObject feedObj = resAuth.getJSONObject(0);
                flag = feedObj.getInt("success");

                Log.e("log in flag ", String.valueOf(flag));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (flag != 0) {

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