package com.kpit.chhotescientists.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.activity.UpdateDetailActivity;
import com.kpit.chhotescientists.adapter.CheckInAdapter;
import com.kpit.chhotescientists.model.CheckInBooleanQuestion;
import com.kpit.chhotescientists.model.CheckInItem;
import com.kpit.chhotescientists.model.CheckInNumberQuestion;
import com.kpit.chhotescientists.model.CheckInStarRating;
import com.kpit.chhotescientists.pojo.UpdatesVO;

import java.util.ArrayList;

public class CheckInFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener {

    private View progressBar;
    private TextView loadErrorTextView;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;

    private ArrayList<CheckInItem> checkInItemsList;

    public CheckInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_check_in, container, false);

        checkInItemsList = new ArrayList<>();

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
        // Dummy data for now. TODO @graham: hookup with backend!
        CheckInBooleanQuestion materialsRecieved = new CheckInBooleanQuestion("Did you recieve the materials?");
        CheckInNumberQuestion studentCountQuestion = new CheckInNumberQuestion("How many students were there?");
        CheckInStarRating overallRating = new CheckInStarRating("How did it go?");
        CheckInStarRating testRating = new CheckInStarRating("Rate this question:");

        CheckInItem checkInItem = new CheckInItem();
        checkInItem.addQuestion(materialsRecieved);
        checkInItem.addQuestion(studentCountQuestion);
        checkInItem.addQuestion(overallRating);
        checkInItem.addQuestion(testRating);

        checkInItemsList.add(checkInItem);

        CheckInAdapter checkInAdapter = new CheckInAdapter(checkInItemsList, getContext());
        recyclerView.setAdapter(checkInAdapter);

        progressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
    }


}
