package com.kpit.chhotescientists.fragment;


import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.activity.MainActivity;
import com.kpit.chhotescientists.adapter.SessionAdapter;
import com.kpit.chhotescientists.external.RuntimeTypeAdapterFactory;
import com.kpit.chhotescientists.model.BooleanQuestion;
import com.kpit.chhotescientists.model.NumberQuestion;
import com.kpit.chhotescientists.model.CheckInQuestion;
import com.kpit.chhotescientists.model.PhotoUploadQuestion;
import com.kpit.chhotescientists.model.StarRatingQuestion;
import com.kpit.chhotescientists.model.Session;
import com.kpit.chhotescientists.model.VideoUploadQuestion;
import com.kpit.chhotescientists.util.AppController;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class SessionFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener {

    private View progressBar;
    private TextView loadErrorTextView;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;

    public SessionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_session, container, false);

        //listView = (ListView) v.findViewById(R.id.listView);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar2);
        loadErrorTextView = (TextView) v.findViewById(R.id.load_error_text_view);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.check_in_swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView = (RecyclerView) v.findViewById(R.id.check_in_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        progressBar = v.findViewById(R.id.check_in_progress_bar);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        loadData();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void loadData() {
        String JSON_URL_TEMPORARY = "https://gist.githubusercontent.com/grahamearley/cae5c0934e0d385a4cf6c2749b1af132/raw/1ae7b4a1b1f9b3ccf7d56fe02cfc2e1290af664a/chhote_events_dummy.json";
        // Request data from Volley
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, JSON_URL_TEMPORARY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // VolleyLog.d(TAG,
                // "Response: " + response.toString());
                if (response != null) {
                    parseEventsJsonIntoAdapter(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: handle errors
                Toast.makeText(getContext(), "Whoops!", Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void parseEventsJsonIntoAdapter(JSONObject response) {
        Log.d("SessionFragment:", "Parsing response: " + response.toString());

        final RuntimeTypeAdapterFactory<CheckInQuestion> questionFactory = RuntimeTypeAdapterFactory
                .of(CheckInQuestion.class, "question_type")
                .registerSubtype(BooleanQuestion.class, BooleanQuestion.QUESTION_TYPE)
                .registerSubtype(StarRatingQuestion.class, StarRatingQuestion.QUESTION_TYPE)
                .registerSubtype(PhotoUploadQuestion.class, PhotoUploadQuestion.QUESTION_TYPE)
                .registerSubtype(VideoUploadQuestion.class, VideoUploadQuestion.QUESTION_TYPE) // todo: implement video type (right now it's a dupe of photo type)
                .registerSubtype(NumberQuestion.class, NumberQuestion.QUESTION_TYPE);

        ArrayList<Session> sessions = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapterFactory(questionFactory).create();

        JSONArray sessionArray = response.optJSONArray("data");
        if (sessionArray != null) {
            for (int i = 0; i < sessionArray.length(); i++) {
                try {
                    JSONObject sessionObject = sessionArray.getJSONObject(i);
                    Session session = gson.fromJson(sessionObject.toString(), Session.class);
                    sessions.add(session);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonParseException jsonParseException) {
                    // Occurs if the response contains an unregistered subtype
                    // todo: show error -- the response will not be complete!
                    jsonParseException.printStackTrace();
                }
            }
        }

        SessionAdapter sessionAdapter = new SessionAdapter(sessions, getContext());
        recyclerView.setAdapter(sessionAdapter);

        progressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
    }
}
