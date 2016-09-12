package org.chhotescientists.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.kpit.chhotescientists.R;
import org.chhotescientists.common.MyCache;
import org.chhotescientists.custom.FeedImageView;
import org.chhotescientists.pojo.UpdatesVO;
import org.chhotescientists.util.AppController;

/**
 * Created by vb on 4/15/2016.
 */
public class UpdateDetailActivity extends AppCompatActivity {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    MyCache myCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_detail);

        getSupportActionBar().show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myCache = MyCache.getInstance(this);
        // 1. get passed intent
        Intent intent = getIntent();

        // 2. get person object from intent
        final UpdatesVO item = (UpdatesVO) intent.getSerializableExtra("updateObj");

        TextView txtHeading = (TextView) findViewById(R.id.update_heading);
        TextView txtCategory = (TextView) findViewById(R.id.textView4);
        TextView txtDescription = (TextView) findViewById(R.id.update_description);

        FeedImageView imgView = (FeedImageView) findViewById(R.id.update_image);

        txtHeading.setText(item.getUpdateHeading());
        setTitle(item.getUpdateHeading());
        txtCategory.setText(item.getUpdateDate());
        txtDescription.setText(item.getUpdateDescription());

        if (item.getUpdateImage().isEmpty()) {
            imgView.setVisibility(View.GONE);
        } else {
            Bitmap bm = (Bitmap) myCache.get(item.getUpdateImage());
            Log.i(getClass().getName(), "Received from Cache : " + bm);
            if ( null != bm ) {
                imgView.setImageBitmap(bm);
            }
            imgView.setImageUrl(item.getUpdateImage(), imageLoader);
            imgView.setVisibility(View.VISIBLE);
            imgView.setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
            });
        }

        imgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // String value = hiddenValue.getText().toString().trim();
                // Toast.makeText(getApplicationContext(),
                // "HEllo  :  " + value, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(UpdateDetailActivity.this,
                        org.chhotescientists.util.ActivityImage.class);
                Bundle bundle1 = ActivityOptionsCompat.makeCustomAnimation(
                        UpdateDetailActivity.this,
                        R.anim.in_right_animation, R.anim.out_left_animation)
                        .toBundle();
                intent.putExtra("title", item.getUpdateHeading());
                intent.putExtra("image", item.getUpdateImage());
                ActivityCompat.startActivity(UpdateDetailActivity.this,
                        intent, bundle1);
            }
        });


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
}