package org.chhotescientists.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kpit.chhotescientists.R;
import org.chhotescientists.common.MyPreferences;
import org.chhotescientists.pojo.VersionCheckVO;
import org.chhotescientists.util.AppController;
import org.chhotescientists.util.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dnyaneshwar on 4/11/2016.
 */
public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static final long SPLASH_TIME_OUT = 3000;
    private MyPreferences mPreferences;
    private List<VersionCheckVO> feedItems = new ArrayList<>();
    ProgressBar pBar;
    int serverVersionCode;
    int verCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pBar = (ProgressBar) findViewById(R.id.progressBar);
        mPreferences = new MyPreferences(this);


        PackageInfo pInfo = null;

        try {

            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version = pInfo.versionName;

        verCode = pInfo.versionCode;

        Log.e("Version", version);
        Log.e("Version Code", String.valueOf(verCode));

/*
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (mPreferences.isFirstTimeLaunch()) {

                    Log.e("Sigle Sign on", String.valueOf(mPreferences.isFirstTimeLaunch()));

                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                            SplashActivity.this, R.anim.in_right_animation,
                            R.anim.out_left_animation).toBundle();
                    ActivityCompat.startActivity(SplashActivity.this, i, bundle);

                } else {

                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                            SplashActivity.this, R.anim.in_right_animation,
                            R.anim.out_left_animation).toBundle();
                    ActivityCompat.startActivity(SplashActivity.this, i, bundle);

                }
                finish();
            }
        }, SPLASH_TIME_OUT);
*/
        /*
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        Log.e("Date", formattedDate);
        */

        vesionCheck();


    }

    //api call to get updates
    private void vesionCheck() {

        feedItems = new ArrayList<VersionCheckVO>();
        // making fresh volley request and getting json

        pBar.setVisibility(View.VISIBLE);
        if (ConnectionDetector
                .isConnectingToInternet(SplashActivity.this)) {
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, getString(R.string.version_check), null, new Response.Listener<JSONObject>() {

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
        } else if (mPreferences.isFirstTimeLaunch()){
            Toast.makeText(
                    SplashActivity.this,
                    getString(R.string.need_internet_msg), Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(
                    SplashActivity.this,
                    getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
            launchApplication();
        }
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

                        serverVersionCode = Integer.parseInt(feedObj.getString("version"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SplashActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (verCode == serverVersionCode) {
                            if (mPreferences.isFirstTimeLaunch()) {

                                Log.e("Sigle Sign on", String.valueOf(mPreferences.isFirstTimeLaunch()));

                                SplashActivity.this.finish();
                                Intent i = new Intent(SplashActivity.this,
                                        LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                                        SplashActivity.this,
                                        R.anim.in_right_animation,
                                        R.anim.out_left_animation).toBundle();
                                ActivityCompat.startActivity(
                                        SplashActivity.this, i, bundle);

                            } else {

                                launchApplication();
                            }
                        } else {


                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                            builder.setMessage("Version Outdated,Update Your App")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //finish();
                                            //System.exit(0);
                                            goToMyApp(true);
                                            //finish();
                                            //System.exit(0);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }

                    }
                });
            }
        }).start();

    }

    private void launchApplication() {
        SplashActivity.this.finish();
        Intent i = new Intent(SplashActivity.this,
                MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                SplashActivity.this,
                R.anim.in_right_animation,
                R.anim.out_left_animation).toBundle();
        ActivityCompat.startActivity(
                SplashActivity.this, i, bundle);
    }

    public void goToMyApp(boolean googlePlay) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((googlePlay ? "market://details?id="
                            : "amzn://apps/android?p=") + getPackageName())));
        } catch (ActivityNotFoundException e1) {
            try {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse((googlePlay ? "https://play.google.com/store/apps/details?id=com.kpit.chhotescientists"
                                : "https://play.google.com/store/apps/details?id=com.kpit.chhotescientists")
                                + getPackageName())));
            } catch (ActivityNotFoundException e2) {
                Toast.makeText(this,
                        "You don't have any app that can open this link",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_left_animation,
                R.anim.out_right_animation);

        finish();
        System.exit(0);
    }
}
