package com.kpit.chhotescientists.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.pojo.ExperimentVO;
import com.kpit.chhotescientists.util.ConnectionDetector;

import java.net.URL;

/**
 * Created by VB on 4/19/2016.
 */
public class ExperimentDetailActivity extends AppCompatActivity {
    String rcvdCatID, rcvdCatName, rcvdExpName, rcvdVideoURL, rcvdPdfURL;

    TextView txtExpname, txtExpCat, txtExpDesc;
    Button btnPlayVideo, btnViewPDF;
    LinearLayout myGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_detail);

        //enable back button on action bar
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtExpname = (TextView) findViewById(R.id.textView2);
        txtExpCat = (TextView) findViewById(R.id.textView6);
        txtExpDesc = (TextView) findViewById(R.id.textView9);
        myGallery = (LinearLayout) findViewById(R.id.gallery);

        btnPlayVideo = (Button) findViewById(R.id.btn_play_video);
        btnViewPDF = (Button) findViewById(R.id.btn_view_pdf);

        // 1. get passed intent
        Intent intent = getIntent();

        rcvdCatName = intent.getExtras().getString("catName");

        // 2. get person object from intent
        ExperimentVO item = (ExperimentVO) intent.getSerializableExtra("expObj");

        setTitle(item.getExpname());
        rcvdExpName = item.getExpname();

        rcvdVideoURL = item.getExpVideoURL();
        rcvdPdfURL = item.getExpPDFUrl();
        if (item.getExpimages().length == 0) {
            myGallery.setVisibility(View.GONE);
        }
        for (String path : item.getExpimages()) {
            if (ConnectionDetector
                    .isConnectingToInternet(ExperimentDetailActivity.this)) {

                new ImageDownloaderTask().execute(path);

            } else {
                Toast.makeText(
                        ExperimentDetailActivity.this,
                        getString(R.string.no_internet_msg),
                        Toast.LENGTH_LONG).show();
            }
        }

        txtExpname.setText(item.getExpname());
        txtExpCat.setText(rcvdCatName);
        txtExpDesc.setText(Html.fromHtml(item.getExpdescription()));

        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExperimentDetailActivity.this, PlayVideoActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        ExperimentDetailActivity.this, R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();

                i.putExtra("expName", rcvdExpName);
                i.putExtra("viewURL", rcvdVideoURL);
                i.putExtra("flag", "1");


                ActivityCompat.startActivity(ExperimentDetailActivity.this, i, bundle);
            }
        });
        btnViewPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExperimentDetailActivity.this, PlayVideoActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        ExperimentDetailActivity.this, R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();

                i.putExtra("expName", rcvdExpName);
                i.putExtra("viewURL", rcvdPdfURL);
                i.putExtra("flag", "2");
                ActivityCompat.startActivity(ExperimentDetailActivity.this, i, bundle);
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

    private View insertPhoto(final String path, final Bitmap bm) {
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(240, 240));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExperimentDetailActivity.this,
                        com.kpit.chhotescientists.util.ActivityImage.class);
                Bundle bundle1 = ActivityOptionsCompat.makeCustomAnimation(
                        ExperimentDetailActivity.this,
                        R.anim.in_right_animation, R.anim.out_left_animation)
                        .toBundle();
                intent.putExtra("title", "");
                intent.putExtra("image", path);
                ActivityCompat.startActivity(ExperimentDetailActivity.this,
                        intent, bundle1);
            }
        });
        layout.addView(imageView);
        return layout;
    }



    private Bitmap getScaledBitmapFromUrl(String imageUrl, int requiredWidth, int requiredHeight) {
        Bitmap bm = null;
        try {
            URL url = new URL(imageUrl);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
            Log.i(getLocalClassName(), options.outHeight + ", " + options.outWidth);
            if (requiredWidth == 0 && requiredHeight == 0) {
                options.inSampleSize = 1;
            } else {
                options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
            }
            Log.i(getLocalClassName(), options.outHeight + ", " + options.outWidth);
            options.inJustDecodeBounds = false;
            //don't use same inputstream object as in decodestream above. It will not work because
            //decode stream edit input stream. So if you create
            //InputStream is =url.openConnection().getInputStream(); and you use this in  decodeStream
            //above and bellow it will not work!

            bm = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
        } catch (Exception re) {
            re.printStackTrace();
            return null;
        }
        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    private class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String path;

        @Override
        protected Bitmap doInBackground(String... params) {
            path = params[0];
            Bitmap bm = getScaledBitmapFromUrl(path, 200, 200);
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            if(bm!=null) {
                myGallery.addView(insertPhoto(path, bm));
            }
        }
    }
}