package com.kpit.chhotescientists.fragment;


import android.os.Bundle;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.adapter.SessionAdapter;
import com.kpit.chhotescientists.external.RuntimeTypeAdapterFactory;
import com.kpit.chhotescientists.model.CheckInBooleanQuestion;
import com.kpit.chhotescientists.model.CheckInNumberQuestion;
import com.kpit.chhotescientists.model.CheckInQuestion;
import com.kpit.chhotescientists.model.CheckInStarRating;
import com.kpit.chhotescientists.model.Session;
import com.kpit.chhotescientists.util.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        String JSON_URL_TEMPORARY = "https://gist.githubusercontent.com/grahamearley/50acbec5adf235303222da123189149b/raw/cf0859bb4a603415d21b2facae773be290bb53ea/chhote_scientists_demo_data.json";
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
                .registerSubtype(CheckInBooleanQuestion.class, "boolean")
                .registerSubtype(CheckInStarRating.class, "star_rating")
                .registerSubtype(CheckInNumberQuestion.class, "number");

        ArrayList<Session> sessions = new ArrayList<>();
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(questionFactory).create();

        JSONArray sessionArray = response.optJSONArray("data");
        if (sessionArray != null) {
            for (int i = 0; i < sessionArray.length(); i++) {
                try {
                    JSONObject sessionObject = sessionArray.getJSONObject(i);
                    Session session = gson.fromJson(sessionObject.toString(), Session.class);
                    sessions.add(session);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        SessionAdapter sessionAdapter = new SessionAdapter(sessions, getContext());
        recyclerView.setAdapter(sessionAdapter);

        progressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
    }

//    private void loadData() {
//        // Dummy data for now. TODO @graham: hookup with backend!
//        CheckInBooleanQuestion materialsRecieved = new CheckInBooleanQuestion("Did you recieve the materials?");
//        CheckInNumberQuestion studentCountQuestion = new CheckInNumberQuestion("How many students were there?");
//        CheckInStarRating overallRating = new CheckInStarRating("How did it go?");
//        CheckInStarRating testRating = new CheckInStarRating("Rate this question:");
//
//        CheckInItem checkInItem = new CheckInItem();
//        checkInItem.addQuestion(materialsRecieved);
//        checkInItem.addQuestion(studentCountQuestion);
//        checkInItem.addQuestion(overallRating);
//        checkInItem.addQuestion(testRating);
//
//        checkInItemsList.add(checkInItem);
//
//        SessionAdapter checkInAdapter = new SessionAdapter(checkInItemsList, getContext());
//        recyclerView.setAdapter(checkInAdapter);
//
//        // TODO: handle input clearing upon recycling..
//        //      When views are recycled, you need to persist
//        //      the input data (text, star ratings, etc) or
//        //      else it will get filled in with old cards' data.
//
//        progressBar.setVisibility(View.GONE);
//        swipeLayout.setRefreshing(false);
//    }


}
