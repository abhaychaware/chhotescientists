package org.chhotescientists.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.chhotescientists.adapter.SessionAdapter;
import org.chhotescientists.common.MyPreferences;
import org.chhotescientists.external.RuntimeTypeAdapterFactory;
import org.chhotescientists.model.BooleanQuestion;
import org.chhotescientists.model.NumberQuestion;
import org.chhotescientists.model.CheckInQuestion;
import org.chhotescientists.model.PhotoUploadQuestion;
import org.chhotescientists.model.StarRatingQuestion;
import org.chhotescientists.model.Session;
import org.chhotescientists.model.VideoUploadQuestion;
import org.chhotescientists.util.AppController;
import org.chhotescientists.util.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The third tab in the main activity -- for displaying
 *  a list of the user's Sessions/Schedules.
 */
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

        progressBar = v.findViewById(R.id.progressBar2);
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
        Log.d("SessionFragment", "Swiped to refresh!");
        loadData();
    }

    private void loadData() {

        // Make request params JSON object:
        JSONObject dataObject = new JSONObject();
        MyPreferences preferences =null;
        try {
            JSONObject parameterObject = new JSONObject();
            preferences = new MyPreferences(getContext());
            parameterObject.put("user_id", preferences.getUserId());
            JSONArray dataArray = new JSONArray();
            dataArray.put(parameterObject);
            dataObject.put("data", dataArray);
            Log.d("SessionFragment", "Requesting schedule with this data: " + dataObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {

            if(preferences!=null&&preferences.isLoggedIn()) {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, getString(R.string.get_schedules), dataObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response != null) {
                                    parseEventsJsonIntoAdapter(response);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: handle errors more gracefully.
                        Log.d("SessionFragment", "Error with schedules: " + error.toString());
                        Toast.makeText(getContext(), "Sorry! There was an error getting the schedules.", Toast.LENGTH_LONG).show();
                    }
                });

                AppController.getInstance().addToRequestQueue(jsonObjReq);
            }
            else{
                recyclerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                swipeLayout.setRefreshing(false);
                loadErrorTextView.setText(R.string.must_be_logged_in);
                loadErrorTextView.setVisibility(View.VISIBLE);
            }
        }else {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            swipeLayout.setRefreshing(false);
            loadErrorTextView.setText("No data to display.");
            loadErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    private void parseEventsJsonIntoAdapter(JSONObject response) {
        Log.d("SessionFragment:", "Parsing response: " + response.toString());

        // Set up a RunTimeTypeAdapter, which will decide what subclass of CheckInQuestion the
        //  object should be instantiated to (based on the "question_type" field):
        final RuntimeTypeAdapterFactory<CheckInQuestion> questionFactory = RuntimeTypeAdapterFactory
                .of(CheckInQuestion.class, "question_type")
                .registerSubtype(BooleanQuestion.class, BooleanQuestion.QUESTION_TYPE)
                .registerSubtype(StarRatingQuestion.class, StarRatingQuestion.QUESTION_TYPE)
                .registerSubtype(PhotoUploadQuestion.class, PhotoUploadQuestion.QUESTION_TYPE)
                .registerSubtype(VideoUploadQuestion.class, VideoUploadQuestion.QUESTION_TYPE) // todo: implement video type (right now it's a dupe of photo type)
                .registerSubtype(NumberQuestion.class, NumberQuestion.QUESTION_TYPE);

        ArrayList<Session> sessions = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) // map the server's variable_names to camelCase
                .registerTypeAdapterFactory(questionFactory).create();

        JSONArray sessionArray = response.optJSONArray("data");

        // Parse the sessions/schedules in the response:
        if (sessionArray != null) {
            for (int i = 0; i < sessionArray.length(); i++) {
                try {
                    JSONObject sessionObject = sessionArray.getJSONObject(i);
                    Session session = gson.fromJson(sessionObject.toString(), Session.class);

                    // Make sure a session's events know their parent session's ID:
                    session.passScheduleIdToEvents();

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

        SessionAdapter sessionAdapter = new SessionAdapter(sessions, getActivity());
        recyclerView.setAdapter(sessionAdapter);

        progressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
    }
}
