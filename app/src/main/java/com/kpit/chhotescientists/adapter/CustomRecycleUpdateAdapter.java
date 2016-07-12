package com.kpit.chhotescientists.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.custom.FeedImageView;
import com.kpit.chhotescientists.pojo.UpdatesVO;
import com.kpit.chhotescientists.util.AppController;

import java.util.List;

public class CustomRecycleUpdateAdapter extends RecyclerView.Adapter<CustomRecycleUpdateAdapter.MyViewHolder> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<UpdatesVO> feeditems;

    public CustomRecycleUpdateAdapter(List<UpdatesVO> feeditems) {
        this.feeditems = feeditems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_update, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UpdatesVO item = feeditems.get(position);
        holder.txtHeading.setText(item.getUpdateHeading());
        holder.txtCategory.setText(item.getUpdateDate());
        holder.txtDescription.setText(item.getUpdateDescription());
        holder.imgView.setImageUrl(item.getUpdateImage(), imageLoader);
    }

    @Override
    public int getItemCount() {
        return feeditems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeading, txtCategory, txtDescription;
        public FeedImageView imgView;

        public MyViewHolder(View view) {
            super(view);

            txtHeading = (TextView) view.findViewById(R.id.update_heading);
            txtCategory = (TextView) view.findViewById(R.id.update_category);
            txtDescription = (TextView) view.findViewById(R.id.update_description);
            imgView = (FeedImageView) view.findViewById(R.id.update_image);
        }
    }
}