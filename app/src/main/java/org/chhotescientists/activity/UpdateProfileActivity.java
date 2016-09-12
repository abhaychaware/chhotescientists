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
import android.view.Menu;
import android.view.MenuItem;
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
import org.chhotescientists.common.MyPreferences;
import org.chhotescientists.util.AppController;
import org.chhotescientists.util.ConnectionDetector;

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

import java.util.ArrayList;

/**
 * Created by Dnyanesh on 4/17/2016.
 */
@SuppressWarnings("ALL")
public class UpdateProfileActivity extends AppCompatActivity {

    MyPreferences myPreferences;

    EditText edtFullname, edtMobile, edtEmail;
    Button btnUpdate;
    Spinner edtCenter;
    AutoCompleteTextView edtResidence;
    ProgressBar pBar;
    String strCSCenter = "";
    JSONArray resAuth;
    JSONArray user;
    private ArrayList<String> feedItems;
    private ArrayList<String> feedItemsCenters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //enable back button on action bar
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myPreferences = new MyPreferences(this);

        btnUpdate = (Button) findViewById(R.id.btn_update_profile);
        edtFullname = (EditText) findViewById(R.id.input_fullname);
        edtMobile = (EditText) findViewById(R.id.input_mobile);
        edtEmail = (EditText) findViewById(R.id.input_email);
        edtResidence = (AutoCompleteTextView) findViewById(R.id.autoc_area_of_residence);
        edtCenter = (Spinner) findViewById(R.id.spn_cs_center);
        pBar = (ProgressBar) findViewById(R.id.progressBar2);
        edtFullname.setText(myPreferences.getUserFullname());
        edtMobile.setText(myPreferences.getUserMobile());
        edtEmail.setText(myPreferences.getUserEmail());
        //edtResidence.setText(myPreferences.getUserResidence());
        //edtCenter.setText(myPreferences.getUserCenter());


        //load location from server
        if (ConnectionDetector
                .isConnectingToInternet(UpdateProfileActivity.this)) {

            loadLocations();

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, feedItems);

            edtResidence.setAdapter(adapter);

        } else {
            Toast.makeText(
                    UpdateProfileActivity.this,
                    getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
        }
//init spinner with data
        initSpinnerCSCenters();


        //onclick listeners

        //setting on click listener
        edtCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectionDetector
                        .isConnectingToInternet(UpdateProfileActivity.this)) {

                    new ExecuteProfileUpdate().execute();

                } else {
                    Toast.makeText(
                            UpdateProfileActivity.this,
                            getString(R.string.no_internet_msg),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    //initialise spinner with data
    public void initSpinnerCSCenters() {

        if (ConnectionDetector
                .isConnectingToInternet(UpdateProfileActivity.this)) {

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
            edtCenter.setAdapter(spinnerArrayAdapter);
        } else {
            Toast.makeText(
                    UpdateProfileActivity.this,
                    getString(R.string.no_internet_msg),
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.change_password, menu);
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

            case R.id.action_change_password: {
                Intent i = new Intent(UpdateProfileActivity.this, ChangePasswordActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        UpdateProfileActivity.this, R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();
                ActivityCompat.startActivity(UpdateProfileActivity.this, i, bundle);
            }
            break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


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
                UpdateProfileActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //pBar.setVisibility(View.GONE);

                    }
                });
            }
        }).start();

    }

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
                UpdateProfileActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        pBar.setVisibility(View.GONE);


                    }
                });
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_left_animation,
                R.anim.out_right_animation);

    }

    private void SaveUserDataProfile(JSONObject response) {
        try {

            JSONArray feedArray = response.getJSONArray("user");
            Log.e("Saving User Profile", response.toString());

            JSONObject feedObj = (JSONObject) feedArray.get(0);

            myPreferences.setUserResidence(feedObj.getString("user_area_of_residence"));
            myPreferences.setUserCenter(feedObj.getString("user_nearest_cs_center"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ExecuteProfileUpdate extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UpdateProfileActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // Creating service handler class instance
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(getString(R.string.update_profile_url));
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
            queryJson.put("fname", edtFullname.getText().toString().trim());
            dataJsonArray.put(queryJson);
            textDataToSend.put("data", dataJsonArray);
            Log.e("JSON Sent", textDataToSend.toString());

            return textDataToSend;

        }


        protected void onPostExecute(String response) {
            Log.e("Server Response", response + "--");

            int flag = 0;
            JSONObject jsonObj , feedObj= null;
            try {
                jsonObj = new JSONObject(response);
                resAuth = jsonObj.getJSONArray("data");
                feedObj = resAuth.getJSONObject(0);
                flag = feedObj.getString("status").equalsIgnoreCase("success")?1:0;
                Log.e("response status flag", String.valueOf(flag));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (flag != 0) {

                pDialog.dismiss();
                // TODO update preferences with new profile
                //SaveUserDataProfile(jsonObj);

                Toast.makeText(UpdateProfileActivity.this,
                        "Profile Update Successful.", Toast.LENGTH_SHORT)
                        .show();

            } else {
                pDialog.dismiss();
                Toast.makeText(UpdateProfileActivity.this,
                        "Profile Update Unsuccessful, Sorry for inconvience.", Toast.LENGTH_SHORT)
                        .show();

            }

        }

    }

}