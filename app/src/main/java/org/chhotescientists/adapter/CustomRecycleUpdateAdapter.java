package org.chhotescientists.adapter;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.kpit.chhotescientists.R;
import org.chhotescientists.common.MyCache;
import org.chhotescientists.common.Utility;
import org.chhotescientists.custom.FeedImageView;
import org.chhotescientists.pojo.UpdatesVO;
import org.chhotescientists.util.AppController;

import java.util.List;

public class CustomRecycleUpdateAdapter extends RecyclerView.Adapter<CustomRecycleUpdateAdapter.MyViewHolder> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<UpdatesVO> feeditems;
    MyCache myCache;

    public CustomRecycleUpdateAdapter(List<UpdatesVO> feeditems) {
        this.feeditems = feeditems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_update, parent, false);
        myCache = MyCache.getInstance(parent.getContext());
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UpdatesVO item = feeditems.get(position);
        holder.txtHeading.setText(item.getUpdateHeading());
        holder.txtCategory.setText(item.getUpdateDate());
        holder.txtDescription.setText(item.getUpdateDescription());
        Bitmap bm = (Bitmap) myCache.get(item.getUpdateImage());
        Log.i(getClass().getName(), "Received from Cache : " + bm);
        if ( null != bm ) {
            holder.imgView.setImageBitmap(bm);
        }
        holder.imgView.setImageUrl(item.getUpdateImage(), imageLoader);
        new ImageDownloaderTask().execute(item.getUpdateImage());
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

    private class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String path;

        @Override
        protected Bitmap doInBackground(String... params) {
            path = params[0];
            Bitmap bm = Utility.getScaledBitmapFromUrl(path, 200, 200);
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            if(bm!=null) {
                Log.i(getClass().getName(), "Cache = " + myCache);
                myCache.put(path, bm);
            }
        }
    }

}