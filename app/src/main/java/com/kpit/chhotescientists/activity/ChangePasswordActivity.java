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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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

                        new ExecuteChangePasswoed().execute(getString(R.string.change_password_url));

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
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);

                List parameters = new ArrayList(2);

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("uid", myPreferences.getUserId());

                jsonObject.put("cpassword", cpassword.getText().toString().trim());

                jsonObject.put("npassword", npassword.getText().toString().trim());


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

        @Override
        protected void onPostExecute(String data) {
            Log.e("Server Response", data + "--");
            pDialog.dismiss();
            int statusflag = 0;
            JSONObject jsonObj;
            try {
                jsonObj = new JSONObject(data);
                //JSONObject jsonObj1 = jsonObj.getJSONObject("data");
                statusflag = Integer.parseInt(jsonObj.getString("success"));
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
