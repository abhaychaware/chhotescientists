package com.kpit.chhotescientists.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;
import com.kpit.chhotescientists.R;

public class ActivityImage extends AppCompatActivity {

    private Context mContext;
    private TouchImageView iv;
    private ProgressBar pbar;
    private ImageLoader imageLoader = AppController.getInstance()
            .getImageLoader();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        mContext = this;
        pbar = (ProgressBar) findViewById(R.id.pbarImage);
        iv = (TouchImageView) findViewById(R.id.ivImageTouch);
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("image");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        Log.e("ZoomURL", url);
        iv.setImageUrl(url, imageLoader);
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
