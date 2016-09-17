package org.chhotescientists.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.chhotescientists.common.MyCache;
import org.chhotescientists.common.Utility;
import org.chhotescientists.pojo.ExperimentVO;
import org.chhotescientists.util.ConnectionDetector;

/**
 * Created by VB on 4/19/2016.
 */
public class ExperimentDetailActivity extends AppCompatActivity {
    String rcvdCatID, rcvdCatName, rcvdExpName, rcvdVideoURL, rcvdPdfURL;

    TextView txtExpname, txtExpCat, txtExpDesc, txtExpRef;
    Button btnPlayVideo, btnViewPDF;
    LinearLayout myGallery;
    MyCache myCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_detail);
        myCache = MyCache.getInstance(getApplicationContext());
        //enable back button on action bar
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtExpname = (TextView) findViewById(R.id.textView2);
        txtExpCat = (TextView) findViewById(R.id.textView6);
        txtExpDesc = (TextView) findViewById(R.id.textView9);
        txtExpRef = (TextView) findViewById(R.id.textView11);
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
                Bitmap bm = (Bitmap) myCache.get(path);
                if ( null != bm ) {
                    myGallery.addView(insertPhoto(path, bm));
                }
                else {
                    Toast.makeText(
                            ExperimentDetailActivity.this,
                            getString(R.string.no_internet_msg),
                            Toast.LENGTH_LONG).show();
                }
            }
        }

        txtExpname.setText(item.getExpname());
        txtExpCat.setText(rcvdCatName);
        txtExpDesc.setText(Html.fromHtml(item.getExpdescription()));

        txtExpRef.setText(Html.fromHtml("<b>Board : </b>" + item.getExpboard() + "<br><b>Standard : </b>" + item.getExpstandard() + "<br><b>Reference : </b>" + item.getExptextbookreference()));

        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rcvdVideoURL==null || rcvdVideoURL.equals(""))
                {
                    Toast.makeText(
                            ExperimentDetailActivity.this,
                            getString(R.string.no_video),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(ExperimentDetailActivity.this, PlayVideoActivity.class);
                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                            ExperimentDetailActivity.this, R.anim.in_right_animation,
                            R.anim.out_left_animation).toBundle();

                    i.putExtra("expName", rcvdExpName);
                    i.putExtra("viewURL", rcvdVideoURL);
                    i.putExtra("flag", "1");
                    ActivityCompat.startActivity(ExperimentDetailActivity.this, i, bundle);
                }
            }
        });
        btnViewPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rcvdPdfURL==null || rcvdPdfURL.equals(""))
                {
                    Toast.makeText(
                            ExperimentDetailActivity.this,
                            getString(R.string.no_pdf),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(ExperimentDetailActivity.this, PlayVideoActivity.class);
                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                            ExperimentDetailActivity.this, R.anim.in_right_animation,
                            R.anim.out_left_animation).toBundle();

                    i.putExtra("expName", rcvdExpName);
                    i.putExtra("viewURL", rcvdPdfURL);
                    i.putExtra("flag", "2");
                    ActivityCompat.startActivity(ExperimentDetailActivity.this, i, bundle);
                }
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
                        org.chhotescientists.util.ActivityImage.class);
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
                Log.i(getLocalClassName(),"Cache = "+myCache);
                myCache.put(path, bm);
                myGallery.addView(insertPhoto(path, bm));

            }
        }
    }
}