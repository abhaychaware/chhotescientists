package com.kpit.chhotescientists.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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
 * Created by vb on 4/11/2016.
 */
@SuppressWarnings("ALL")
public class LoginActivity extends AppCompatActivity {

    //Declaration
    Button btnlogin;
    TextView txtSignup;
    TextView txtForgotPassword;
    EditText edtUsername, edtPassword;
    String username, password;
    JSONArray resAuth;
    private MyPreferences mPreferences;

    //SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPreferences = new MyPreferences(this);

        edtUsername = (EditText) findViewById(R.id.input_username);
        edtPassword = (EditText) findViewById(R.id.input_password);
        txtSignup = (TextView) findViewById(R.id.linkRegister);
        txtForgotPassword = (TextView) findViewById(R.id.textView7);
        btnlogin = (Button) findViewById(R.id.btn_login);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isValidation()) {

                    if (ConnectionDetector
                            .isConnectingToInternet(LoginActivity.this)) {

                        new ExecuteUserLoginTask().execute(getString(R.string.login_user));

                    } else {
                        Toast.makeText(
                                LoginActivity.this,
                                getString(R.string.no_internet_msg),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {

                }


            }


        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        LoginActivity.this, R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();
                ActivityCompat.startActivity(LoginActivity.this, i, bundle);

            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        LoginActivity.this, R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();
                ActivityCompat.startActivity(LoginActivity.this, i, bundle);
            }
        });
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void SaveUserDataProfile(JSONObject response) {
        try {

            JSONArray feedArray = response.getJSONArray("user");
            Log.e("Saving User Profile", response.toString());

            JSONObject feedObj = (JSONObject) feedArray.get(0);

            mPreferences.setUserId(feedObj.getString("user_id"));
            mPreferences.setUserFullname(feedObj.getString("user_fullname"));
            mPreferences.setUserMobile(feedObj.getString("user_mobile"));
            mPreferences.setUserEmail(feedObj.getString("user_email"));
            mPreferences.setUserResidence(feedObj.getString("user_area_of_residence"));
            mPreferences.setUserCenter(feedObj.getString("user_nearest_cs_center"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //registration validation
    public boolean isValidation() {

        if (!Universal.isEmpty(edtUsername)) {
            edtUsername.setError(getString(R.string.login_enter_username));
            return false;
        }

        //password valiadtion
        if (!Universal.isEmpty(edtPassword)) {
            edtPassword.setError(getString(R.string.login_enter_password));
            return false;
        }

        return true;
    }

    private class ExecuteUserLoginTask extends
            AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(
                    LoginActivity.this);
            mProgressDialog.setMessage(getString(R.string.login_loading));
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
                jsonObject.put("user_password", edtPassword.getText().toString().trim());

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


                LoginActivity.this.finish();
                Intent i = new Intent(LoginActivity.this,
                        MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        LoginActivity.this,
                        R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();
                ActivityCompat.startActivity(
                        LoginActivity.this, i, bundle);

                //saving data to prefrences.
                SaveUserDataProfile(jsonObj);

                Toast.makeText(LoginActivity.this,
                        getString(R.string.notify_welcome), Toast.LENGTH_SHORT)
                        .show();

            } else {
                mProgressDialog.dismiss();
                Toast.makeText(LoginActivity.this,
                        getString(R.string.login_unsusccess), Toast.LENGTH_SHORT)
                        .show();

            }

        }

    }
}