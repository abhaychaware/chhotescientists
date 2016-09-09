package com.kpit.chhotescientists.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;
import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.common.MyCache;

public class ActivityImage extends AppCompatActivity {

    private Context mContext;
    private TouchImageView iv;
    private ProgressBar pbar;
    private ImageLoader imageLoader = AppController.getInstance()
            .getImageLoader();
    MyCache myCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        mContext = this;
        myCache = MyCache.getInstance(getApplicationContext());
        pbar = (ProgressBar) findViewById(R.id.pbarImage);
        iv = (TouchImageView) findViewById(R.id.ivImageTouch);
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("image");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        Bitmap bitmap = (Bitmap) myCache.get(url);
        Log.i(getLocalClassName(), "Received from Cache : "+bitmap);
        if (bitmap!=null) {
            iv.setImageBitmap(bitmap);
        }
        iv.setImageUrl(url, imageLoader);

/*        if (ConnectionDetector.isConnectingToInternet(this)) {
            iv.setImageUrl(url, imageLoader);
        }else {

            Bitmap bitmap = (Bitmap) myCache.get(url);
            Log.i(getLocalClassName(), "Received from Cache : "+bitmap);
            if (bitmap!=null) {
                iv.setImageBitmap(bitmap);
                iv.refreshDrawableState();
            }
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}
