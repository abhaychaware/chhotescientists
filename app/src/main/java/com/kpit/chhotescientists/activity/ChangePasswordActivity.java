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
 * Created by dnyaneshwarkanpurne on 3/17/16.
 */
@SuppressWarnings("ALL")
public class ChangePasswordActivity extends AppCompatActivity {
    Button btnChangePassword;
    ProgressDialog pDialog;
    MyPreferences myPreferences;
    EditText cpassword, npassword, ncpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().show();

        myPreferences = new MyPreferences(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cpassword = (EditText) findViewById(R.id.input_currentpassword);
        npassword = (EditText) findViewById(R.id.input_newpassword);
        ncpassword = (EditText) findViewById(R.id.input_confirmpassword);


        btnChangePassword = (Button) findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ChnagePasswordActivity.this, "Change Password btn hit triggered", Toast.LENGTH_SHORT).show();

                if (isValidation()) {
                    if (ConnectionDetector.isConnectingToInternet(ChangePasswordActivity.this)) {

                        new ExecuteChangePasswoed().execute();

                    } else {
                        Toast.makeText(ChangePasswordActivity.this,
                                getString(R.string.no_internet_msg),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {

                }

            }
        });

    }

    public boolean isValidation() {

        if (!Universal.isEmpty(cpassword)) {
            cpassword.setError(getString(R.string.val_enter_current_pass));
            return false;
        }

        //password valiadtion
        if (!Universal.isEmpty(npassword)) {
            npassword.setError(getString(R.string.val_enter_new_pass));
            return false;
        }

        if (!Universal.isEmpty(ncpassword)) {
            ncpassword.setError(getString(R.string.val_enter_confirm_pass));
            return false;
        }
        if (!npassword.getText().toString().trim().equals(ncpassword.getText().toString().trim())) {
            npassword.setError("Password Mismatch");
            ncpassword.setError("Password Mismatch");
            return false;
        }


        return true;
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

    private class ExecuteChangePasswoed extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ChangePasswordActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // Creating service handler class instance
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(getString(R.string.change_password_url));
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
            queryJson.put("current_password", cpassword.getText().toString().trim());
            queryJson.put("new_password", npassword.getText().toString().trim());
            dataJsonArray.put(queryJson);
            textDataToSend.put("data", dataJsonArray);
            Log.e("JSON Sent", textDataToSend.toString());

            return textDataToSend;

        }

        @Override
        protected void onPostExecute(String data) {
            Log.e("Server Response", data + "--");
            pDialog.dismiss();
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

                Toast.makeText(ChangePasswordActivity.this,
                        "Password Updated Successfully.", Toast.LENGTH_SHORT)
                        .show();
                /*ChnagePasswordActivity.this.finish();
                Intent i = new Intent(ChnagePasswordActivity.this,
                        LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        ChnagePasswordActivity.this,
                        R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();
                ActivityCompat.startActivity(
                        ChnagePasswordActivity.this, i, bundle);*/


                onBackPressed();
            } else {
                //pDialog.dismiss();
                Toast.makeText(ChangePasswordActivity.this,
                        "Password Update Unsuccessful, Sorry for inconvience.", Toast.LENGTH_SHORT)
                        .show();

            }


        }
    }
}
