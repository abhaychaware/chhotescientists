package com.kpit.chhotescientists.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.custom.FeedImageView;
import com.kpit.chhotescientists.pojo.ExperimentVO;
import com.kpit.chhotescientists.util.AppController;

import java.util.List;

public class CustomRecycleExperimentAdapter extends RecyclerView.Adapter<CustomRecycleExperimentAdapter.MyViewHolder> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<ExperimentVO> feeditems;

    public CustomRecycleExperimentAdapter(List<ExperimentVO> feeditems) {
        this.feeditems = feeditems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_experiment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ExperimentVO item = feeditems.get(position);
        holder.txtHeading.setText(item.getExpname());
        holder.txtDescription.setText(item.getExpdescriptionShort());
        holder.imgView.setImageUrl(item.getExpimage(), imageLoader);
    }

    @Override
    public int getItemCount() {
        return feeditems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeading, txtDescription;
        public FeedImageView imgView;

        public MyViewHolder(View view) {
            super(view);

            txtHeading = (TextView) view.findViewById(R.id.exp_heading);
            txtDescription = (TextView) view.findViewById(R.id.exp_description);
            imgView = (FeedImageView) view.findViewById(R.id.exp_image);
        }
    }
}
