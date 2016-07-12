package com.kpit.chhotescientists.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.adapter.CustomRecycleExperimentAdapter;
import com.kpit.chhotescientists.custom.DividerItemDecoration;
import com.kpit.chhotescientists.pojo.CategoryVO;
import com.kpit.chhotescientists.pojo.ExperimentVO;
import com.kpit.chhotescientists.util.AppController;
import com.kpit.chhotescientists.util.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VB on 4/19/2016.
 */
@SuppressWarnings("ALL")
public class ExperimentViewActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    String rcvdCatID, rcvdCatName;
    View v;
    SwipeRefreshLayout mSwipeLayout;
    ProgressBar pBar;
    TextView txtNoNWMessage;
    private List<ExperimentVO> feedItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomRecycleExperimentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_view);

        //enable back button on action bar
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pBar = (ProgressBar) findViewById(R.id.progressBar2);
        txtNoNWMessage = (TextView) findViewById(R.id.textView);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(ExperimentViewActivity.this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // 1. get passed intent
        Intent intent = getIntent();

        // 2. get person object from intent
        CategoryVO item = (CategoryVO) intent.getSerializableExtra("categoryObj");

        rcvdCatID = item.getCatID();

        setTitle(item.getCatName());

        //passing on experiment detail scrrenn
        rcvdCatName = item.getCatName();

        loadUpdates();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(ExperimentViewActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ExperimentVO item = feedItems.get(position);

                Intent i = new Intent(ExperimentViewActivity.this,
                        ExperimentDetailActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        ExperimentViewActivity.this, R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();

                i.putExtra("expObj", item);
                i.putExtra("catName", rcvdCatName);
                ActivityCompat.startActivity(ExperimentViewActivity.this, i, bundle);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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

    //api call to get updates
    private void loadUpdates() {

        recyclerView.setVisibility(View.VISIBLE);
        pBar.setVisibility(View.INVISIBLE);
        mSwipeLayout.setRefreshing(false);
        //txtNoNWMessage.setText("No data to display.");
        txtNoNWMessage.setVisibility(View.GONE);

        feedItems = new ArrayList<ExperimentVO>();
        mAdapter = new CustomRecycleExperimentAdapter(feedItems);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (!mSwipeLayout.isRefreshing())
            pBar.setVisibility(View.VISIBLE);

        if (ConnectionDetector.isConnectingToInternet(this)) {

            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, getString(R.string.get_selected_experiment).concat("?exp_cat=" + rcvdCatID), null, new Response.Listener<JSONObject>() {

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

                        ExperimentVO item = new ExperimentVO();

                        item.setExpid(feedObj.getString("exp_id"));
                        item.setExpname(feedObj.getString("exp_name"));
                        item.setExpdescription(feedObj.getString("exp_description"));
                        item.setExpimage(feedObj.getString("exp_image"));
                        item.setExpcat(feedObj.getString("exp_cat"));
                        item.setExpVideoURL(feedObj.getString("exp_video_url"));
                        item.setExpPDFUrl(feedObj.getString("exp_pdf_url"));
                        feedItems.add(item);
                        //adapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ExperimentViewActivity.this.runOnUiThread(new Runnable() {

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
        private ExperimentViewActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ExperimentViewActivity.ClickListener clickListener) {
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