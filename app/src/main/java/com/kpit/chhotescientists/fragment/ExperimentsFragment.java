package com.kpit.chhotescientists.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.activity.ExperimentViewActivity;
import com.kpit.chhotescientists.activity.MainActivity;
import com.kpit.chhotescientists.adapter.CustomRecycleCategoryAdapter;
import com.kpit.chhotescientists.common.MyPreferences;
import com.kpit.chhotescientists.custom.DividerItemDecoration;
import com.kpit.chhotescientists.pojo.CategoryVO;
import com.kpit.chhotescientists.util.AppController;
import com.kpit.chhotescientists.util.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vb on 4/11/2016.
 */
@SuppressWarnings("ALL")
public class ExperimentsFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener {

    View v;
    MyPreferences myPreferences;
    CustomRecycleCategoryAdapter mAdapter;
    //GridView listView;
    SwipeRefreshLayout mSwipeLayout;
    ProgressBar pBar;
    private ArrayList<CategoryVO> feedItems;
    private RecyclerView recyclerView;
    TextView txtNoNWMessage;
    private GridLayoutManager lLayout;
    private MyPreferences mPreferences;

    public ExperimentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = new MyPreferences(this.getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_experiments, container, false);

        //listView = (GridView) v.findViewById(R.id.gridView);
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

        lLayout = new GridLayoutManager(getActivity(), 3);

        loadUpdates();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                CategoryVO item = feedItems.get(position);

                Intent i = new Intent(getActivity(),
                        ExperimentViewActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        getActivity(), R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();

                i.putExtra("categoryObj", item);

                ActivityCompat.startActivity(getActivity(), i, bundle);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    //api call to get updates
    public void loadUpdates() {

        recyclerView.setVisibility(View.VISIBLE);
        pBar.setVisibility(View.INVISIBLE);
        mSwipeLayout.setRefreshing(false);
        //txtNoNWMessage.setText("No data to display.");
        txtNoNWMessage.setVisibility(View.GONE);

        feedItems = new ArrayList<CategoryVO>();
        mAdapter = new CustomRecycleCategoryAdapter(feedItems);

/*
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
  */

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (!mSwipeLayout.isRefreshing())
            pBar.setVisibility(View.VISIBLE);
        Log.i(getTag(),"Fetching from preferences :"+mPreferences.getThemes());

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {

            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, getString(R.string.get_themes), null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // VolleyLog.d(TAG,
                    // "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                        mPreferences.setThemes(response.toString());
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
        else if(mPreferences.getThemes()!="") {
            JSONObject themes;
            try {
                Log.i(getTag(),"Fetching from preferences :"+mPreferences.getThemes());
                themes = new JSONObject(mPreferences.getThemes());
                parseJsonFeed(themes);
            } catch (JSONException e) {
                e.printStackTrace();
                recyclerView.setVisibility(View.INVISIBLE);
                pBar.setVisibility(View.INVISIBLE);
                mSwipeLayout.setRefreshing(false);
                txtNoNWMessage.setText("No data to display.");
                txtNoNWMessage.setVisibility(View.VISIBLE);
            }
        }
        else {

            //listView.setVisibility(View.INVISIBLE);
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

                        CategoryVO item = new CategoryVO();

                        item.setCatID(feedObj.getString("cat_id"));
                        item.setCatName(feedObj.getString("cat_name"));
                        item.setCatDesc(feedObj.getString("cat_description"));
                        item.setCatImage(feedObj.getString("cat_image"));
                        item.setCatNotificationCount(feedObj.getString("cat_no_of_experiments"));

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
        private ExperimentsFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ExperimentsFragment.ClickListener clickListener) {
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
