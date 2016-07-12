package com.kpit.chhotescientists.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.pojo.ExperimentVO;

/**
 * Created by VB on 4/19/2016.
 */
public class ExperimentDetailActivity extends AppCompatActivity {
    String rcvdCatID, rcvdCatName,rcvdExpName,rcvdVideoURL,rcvdPdfURL;

    TextView txtExpname, txtExpCat, txtExpDesc;
    Button btnPlayVideo, btnViewPDF;

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

        btnPlayVideo = (Button) findViewById(R.id.btn_play_video);
        btnViewPDF= (Button) findViewById(R.id.btn_view_pdf);

        // 1. get passed intent
        Intent intent = getIntent();

        rcvdCatName = intent.getExtras().getString("catName");

        // 2. get person object from intent
        ExperimentVO item = (ExperimentVO) intent.getSerializableExtra("expObj");

        setTitle(item.getExpname());
        rcvdExpName=item.getExpname();

        rcvdVideoURL=item.getExpVideoURL();
        rcvdPdfURL=item.getExpPDFUrl();

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

                i.putExtra("expName",rcvdExpName);
                i.putExtra("viewURL",rcvdVideoURL);
                i.putExtra("flag","1");


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

                i.putExtra("expName",rcvdExpName);
                i.putExtra("viewURL",rcvdPdfURL);
                i.putExtra("flag","2");
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
}