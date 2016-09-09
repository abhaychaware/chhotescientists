package com.kpit.chhotescientists.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.util.ConnectionDetector;

/**
 * Created by VB on 4/20/2016.
 */
@SuppressWarnings("ALL")
public class PlayVideoActivity extends AppCompatActivity {
    private WebView web;
    String rcvExpName, rcvdURL, rcvdFlag;
    WebView wv1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        //enable back button on action bar
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 1. get passed intent
        Intent intent = getIntent();

        rcvExpName = intent.getExtras().getString("expName");
        rcvdURL = intent.getExtras().getString("viewURL");
        rcvdFlag = intent.getExtras().getString("flag");

        setTitle(rcvExpName);

        //Get webview
        web = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        //startWebView("http://docs.google.com/gview?embedded=true&url="+"http://hotelpodlipou.sk/uploads/files/sample.pdf");
        //startWebView("https://www.youtube.com/watch?v=ySKG-2vmw5w");

        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);

        if (ConnectionDetector
                .isConnectingToInternet(PlayVideoActivity.this)) {

            if (rcvdFlag.equals("1")) {
                web.loadUrl(rcvdURL);
            } else if (rcvdFlag.equals("2")) {
                web.loadUrl("http://docs.google.com/gview?embedded=true&url=" + rcvdURL);
            } else {
                Toast.makeText(PlayVideoActivity.this, "Unable to load url", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(
                    PlayVideoActivity.this,
                    getString(R.string.no_internet_msg),
                    Toast.LENGTH_LONG).show();
        }


    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
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