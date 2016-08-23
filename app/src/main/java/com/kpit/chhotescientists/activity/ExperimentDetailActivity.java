package com.kpit.chhotescientists.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.pojo.ExperimentVO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
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

/*        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        String targetPath = ExternalStorageDirectoryPath + "/test/";
        File targetDirector = new File(targetPath);
        File[] files = targetDirector.listFiles();

        if(files !=null) {
            for (File file : files) {
                myGallery.addView(insertPhoto(file.getAbsolutePath()));
            }
        }
        */

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
            myGallery.addView(insertPhoto(path));
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

    View insertPhoto(final String path) {
        //Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
        final Bitmap bm = getScaledBitmapFromUrl(path, 200, 200);
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(240, 240));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);
        imageView.setOnClickListener(new View.OnClickListener() {

            public void showZoomableImage() {
                //Dialog d = new Dialog(ExperimentDetailActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                Dialog d = new Dialog(ExperimentDetailActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
                d.getWindow().setLayout(width, height);
                d.setCancelable(true);
                WebView wv = new WebView(ExperimentDetailActivity.this);
                //wv.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                String html = "<html><body><center><img src='{IMAGE_PLACEHOLDER}'/></body></html>";
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String image = "data:image/png;base64," + imgageBase64;
                html = html.replace("{IMAGE_PLACEHOLDER}", image);
                wv.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");
                wv.getSettings().setBuiltInZoomControls(true);
                wv.getSettings().setSupportZoom(true);
                d.addContentView(wv, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                d.show();
            }

            public void showImage() {
                Dialog builder = new Dialog(ExperimentDetailActivity.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });

                ImageView imageView = new ImageView(ExperimentDetailActivity.this);
                imageView.setImageBitmap(bm);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            }

            @Override
            public void onClick(View v) {
                showZoomableImage();
            }
        });
        layout.addView(imageView);
        return layout;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    private Bitmap getScaledBitmapFromUrl(String imageUrl, int requiredWidth, int requiredHeight) {
        Bitmap bm = null;
        try {
            // TODO: 8/21/2016 Replace this with async task
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
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
}