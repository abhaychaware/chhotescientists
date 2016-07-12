package com.kpit.chhotescientists.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.custom.FeedImageView;
import com.kpit.chhotescientists.pojo.CategoryVO;
import com.kpit.chhotescientists.util.AppController;

import java.util.List;

public class CustomRecycleCategoryAdapter extends RecyclerView.Adapter<CustomRecycleCategoryAdapter.MyViewHolder> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<CategoryVO> feeditems;

    public CustomRecycleCategoryAdapter(List<CategoryVO> feeditems) {
        this.feeditems = feeditems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryVO item = feeditems.get(position);
        holder.txtHeading.setText(item.getCatName());
        holder.txtcount.setText(item.getCatNotificationCount());
        holder.imgView.setImageUrl(item.getCatImage(), imageLoader);
    }

    @Override
    public int getItemCount() {
        return feeditems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeading, txtcount;
        public NetworkImageView imgView;

        public MyViewHolder(View view) {
            super(view);

            txtHeading = (TextView) view.findViewById(R.id.catname);
            txtcount = (TextView) view.findViewById(R.id.count);
            imgView = (NetworkImageView) view.findViewById(R.id.imageView);
        }
    }
}
