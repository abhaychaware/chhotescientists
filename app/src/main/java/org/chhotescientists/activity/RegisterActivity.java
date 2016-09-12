package org.chhotescientists.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kpit.chhotescientists.R;
import org.chhotescientists.util.AppController;
import org.chhotescientists.util.ConnectionDetector;
import org.chhotescientists.validation.Universal;

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
public class RegisterActivity extends AppCompatActivity {

    AutoCompleteTextView autocResidence;
    EditText edtFullname, edtMobile, edtEmail, edtPassword, edtCpassword;
    Button registerButtonAction;
    Spinner csCenters;
    ProgressBar pBar;
    String strCSCenter = "";
    private ArrayList<String> feedItems;
    private ArrayList<String> feedItemsCenters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edtFullname = (EditText) findViewById(R.id.input_fullname);
        edtEmail = (EditText) findViewById(R.id.input_email);
        edtMobile = (EditText) findViewById(R.id.input_mobile);
        edtPassword = (EditText) findViewById(R.id.input_password);
        edtCpassword = (EditText) findViewById(R.id.input_cpassword);
        registerButtonAction = (Button) findViewById(R.id.btnRegister);
        pBar = (ProgressBar) findViewById(R.id.progressBar2);
        autocResidence = (AutoCompleteTextView) findViewById(R.id.autoc_area_of_residence);
        csCenters = (Spinner) findViewById(R.id.spn_cs_center);

        //load location from server
        if (ConnectionDetector
                .isConnectingToInternet(RegisterActivity.this)) {

            loadLocations();

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, feedItems);

            autocResidence.setAdapter(adapter);

        } else {
            Toast.makeText(
                    RegisterActivity.this,
                    getString(R.string.no_internet_msg), Toast.LENGTH_SHORT).show();
        }

        //init spinner with data
        initSpinnerCSCenters();


        //onclick listeners

        //setting on click listener
        csCenters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItemText = (String) parent.getItemAtPosition(position);

                if (position > 0) {
                    // Notify the selected item text
                    TextView tv = (TextView) findViewById(R.id.spnTxt);
                    tv.setTextColor(Color.BLACK);
                    strCSCenter = selectedItemText;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //on click listenere
        registerButtonAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (isValidation()) {

                    if (ConnectionDetector
                            .isConnectingToInternet(RegisterActivity.this)) {

                        new ExecuteUserRegistrationTask().execute(getString(R.string.register_user));


                    } else {
                        Toast.makeText(
                                RegisterActivity.this,
                                getString(R.string.no_internet_msg),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
            }

        });


    }

    //functions

    //initialise spinner with data
    public void initSpinnerCSCenters() {

        if (ConnectionDetector
                .isConnectingToInternet(RegisterActivity.this)) {

            loadCenters();


            // Initializing an ArrayAdapter
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    this, R.layout.spinner_item_cs_center, feedItemsCenters) {


                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };

            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_cs_center);
            csCenters.setAdapter(spinnerArrayAdapter);
        } else {
            Toast.makeText(
                    RegisterActivity.this,
                    getString(R.string.no_internet_msg),
                    Toast.LENGTH_SHORT).show();
        }


    }

    //registration validation
    public boolean isValidation() {

        if (!Universal.isEmpty(edtFullname)) {
            edtFullname.setError("Please Enter Fullname");
            return false;
        }

        if (!Universal.isValidEmail(edtEmail.getText()
                .toString().trim())) {
            edtEmail.setError("Please Enter Valid Email Id");
            return false;
        }

        if (!Universal.isValidMobile(edtMobile.getText()
                .toString().trim())) {
            edtMobile.setError("Please Enter Valid Mobile No");
            return false;
        }

        if (!Universal.isEmptyACT(autocResidence)) {
            autocResidence.setError("Please Select Area of Residence");

            return false;
        }

        if (strCSCenter.equals("")) {

            TextView errorText = (TextView) csCenters.getSelectedView();
            errorText.setError("Please select nearest Chhote Scientists center");
            return false;
        }

        //password valiadtion
        if (!Universal.isEmpty(edtPassword)) {
            edtPassword.setError("Please Enter Password");
            return false;
        }

        if (!Universal.isEmpty(edtCpassword)) {
            edtCpassword.setError("Please Enter Password");
            return false;
        }

        if (!edtPassword.getText().toString().trim().equals(edtCpassword.getText().toString().trim())) {
            edtPassword.setError("Password Mismatch");
            edtCpassword.setError("Password Mismatch");
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

    //api call to get updates
    private void loadLocations() {

        feedItems = new ArrayList<String>();
        // making fresh volley request and getting json

        pBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, getString(R.string.get_locations_url), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // VolleyLog.d(TAG,
                // "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " +
                // error.getMessage());

            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(final JSONObject response) {
        Log.e("###", response.toString());
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    JSONArray feedArray = response.getJSONArray("data");

                    for (int i = 0; i < feedArray.length(); i++) {

                        JSONObject feedObj = (JSONObject) feedArray.get(i);

                        feedItems.add(feedObj.getString("location_name"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RegisterActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                    }
                });
            }
        }).start();

    }

    //api call to get updates
    private void loadCenters() {

        feedItemsCenters = new ArrayList<String>();
        // making fresh volley request and getting json
        feedItemsCenters.add(getString(R.string.select_center_msg));

        pBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, getString(R.string.get_centers_url), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // VolleyLog.d(TAG,
                // "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeedCenters(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " +
                // error.getMessage());

            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeedCenters(final JSONObject response) {
        Log.e("###", response.toString());
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    JSONArray feedArray = response.getJSONArray("data");

                    for (int i = 0; i < feedArray.length(); i++) {

                        JSONObject feedObj = (JSONObject) feedArray.get(i);

                        feedItemsCenters.add(feedObj.getString("center_name"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RegisterActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        pBar.setVisibility(View.GONE);


                    }
                });
            }
        }).start();

    }

    //api call to server
    private class ExecuteUserRegistrationTask extends
            AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(
                    RegisterActivity.this);
            mProgressDialog.setMessage("Registering...");
            mProgressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);

                List parameters = new ArrayList(2);
                JSONObject jsonObject = new JSONObject();

                //json packet

                jsonObject.put("user_fullname", edtFullname.getText().toString().trim());
                jsonObject.put("user_email", edtEmail.getText().toString().trim());
                jsonObject.put("user_mobile", edtMobile.getText().toString().trim());
                jsonObject.put("user_area_of_residence", autocResidence.getText().toString().trim());
                jsonObject.put("user_nearest_cs_center", strCSCenter);
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

        protected void onPostExecute(String data) {
            Log.e("Server Response", data + "--");
            mProgressDialog.dismiss();
            String id = "0";
            if (data != null) {
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(data);
                    JSONObject jsonObj1 = jsonObj.getJSONObject("data");
                    id = jsonObj1.getString("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v("Last ID", id);
                if (id.equals("0")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,
                            "User Already Registered.", Toast.LENGTH_SHORT)
                            .show();

                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Registration Succseefully.", Toast.LENGTH_SHORT)
                            .show();
                    RegisterActivity.this.finish();
                    Intent i = new Intent(RegisterActivity.this,
                            LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                            RegisterActivity.this,
                            R.anim.in_right_animation,
                            R.anim.out_left_animation).toBundle();
                    ActivityCompat.startActivity(
                            RegisterActivity.this, i, bundle);

                }
            } else {
                Toast.makeText(RegisterActivity.this,
                        "Please try after sometime.", Toast.LENGTH_SHORT)
                        .show();

            }
        }
    }
}
