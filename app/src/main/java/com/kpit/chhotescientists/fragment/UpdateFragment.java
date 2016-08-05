package com.kpit.chhotescientists.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.activity.UpdateDetailActivity;
import com.kpit.chhotescientists.adapter.CustomRecycleUpdateAdapter;
import com.kpit.chhotescientists.common.MyPreferences;
import com.kpit.chhotescientists.custom.DividerItemDecoration;
import com.kpit.chhotescientists.pojo.UpdatesVO;
import com.kpit.chhotescientists.util.AppController;
import com.kpit.chhotescientists.util.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vb on 4/11/2016.
 */
@SuppressWarnings("ALL")
public class UpdateFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener {

    View v;
    MyPreferences myPreferences;
    //private ArrayList<UpdatesVO> feedItems;
    ListView listView;
    SwipeRefreshLayout mSwipeLayout;
    ProgressBar pBar;
    TextView txtNoNWMessage;
    private List<UpdatesVO> feedItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomRecycleUpdateAdapter mAdapter;

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_update, container, false);

        //listView = (ListView) v.findViewById(R.id.listView);
        pBar = (ProgressBar) v.findViewById(R.id.progressBar2);
        txtNoNWMessage = (TextView) v.findViewById(R.id.textView);
        mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        loadUpdates();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UpdatesVO item = feedItems.get(position);

                Intent i = new Intent(getActivity(),
                        UpdateDetailActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        getActivity(), R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();

                i.putExtra("updateObj", item);

                ActivityCompat.startActivity(getActivity(), i, bundle);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    //api call to get updates
    private void loadUpdates() {

        recyclerView.setVisibility(View.VISIBLE);
        pBar.setVisibility(View.INVISIBLE);
        mSwipeLayout.setRefreshing(false);
        //txtNoNWMessage.setText("No data to display.");
        txtNoNWMessage.setVisibility(View.GONE);

        feedItems = new ArrayList<UpdatesVO>();
        mAdapter = new CustomRecycleUpdateAdapter(feedItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (!mSwipeLayout.isRefreshing())
            pBar.setVisibility(View.VISIBLE);

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, getString(R.string.get_updates), null, new Response.Listener<JSONObject>() {

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

                    recyclerView.setVisibility(View.INVISIBLE);
                    pBar.setVisibility(View.INVISIBLE);
                    mSwipeLayout.setRefreshing(false);
                    txtNoNWMessage.setText("Error occured.Please try after sometime");
                    txtNoNWMessage.setVisibility(View.VISIBLE);
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            pBar.setVisibility(View.INVISIBLE);
            mSwipeLayout.setRefreshing(false);
            txtNoNWMessage.setText("No data to display.");
            txtNoNWMessage.setVisibility(View.VISIBLE);
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

                        UpdatesVO item = new UpdatesVO();

                        item.setUpdateID(feedObj.getString("update_id"));
                        item.setUpdateHeading(feedObj.getString("update_heading"));
                        item.setUpdateDescription(feedObj.getString("update_description"));

                        item.setUpdateImage(feedObj.getString("update_image"));

                        item.setUpdateDate(feedObj.getString("update_date"));
                        //item.setUpdateTimestamp(feedObj.getString("timestamp"));

                        feedItems.add(item);
                        //adapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        pBar.setVisibility(View.GONE);
                        mSwipeLayout.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    @Override
    public void onRefresh() {

        loadUpdates();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private UpdateFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final UpdateFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}