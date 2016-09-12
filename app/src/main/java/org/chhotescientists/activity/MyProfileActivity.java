package org.chhotescientists.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.kpit.chhotescientists.R;
import org.chhotescientists.common.MyPreferences;

/**
 * Created by vb on 4/11/2016.
 */
public class MyProfileActivity extends AppCompatActivity {
    MyPreferences myPreferences;

    EditText edtFullname, edtMobile, edtEmail, edtResidence, edtCenter;

    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myPreferences = new MyPreferences(this);

        //enable back button on action bar
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnUpdate = (Button) findViewById(R.id.btn_update_profile);
        edtFullname = (EditText) findViewById(R.id.input_fullname);
        edtMobile = (EditText) findViewById(R.id.input_mobile);
        edtEmail = (EditText) findViewById(R.id.input_email);
        edtResidence = (EditText) findViewById(R.id.input_residence);
        edtCenter = (EditText) findViewById(R.id.input_center);

        edtFullname.setText(myPreferences.getUserFullname());
        edtMobile.setText(myPreferences.getUserMobile());
        edtEmail.setText(myPreferences.getUserEmail());
        edtResidence.setText(myPreferences.getUserResidence());
        edtCenter.setText(myPreferences.getUserCenter());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyProfileActivity.this, UpdateProfileActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        MyProfileActivity.this, R.anim.in_right_animation,
                        R.anim.out_left_animation).toBundle();
                ActivityCompat.startActivity(MyProfileActivity.this, i, bundle);
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
    protected void onResume() {
        super.onResume();

        edtResidence.setText(myPreferences.getUserResidence());
        edtCenter.setText(myPreferences.getUserCenter());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
//            case R.id.action_editprofile:
//                Log.e("TAG", "Hello");
//                setTitle("Edit Profile");
//                cv.setVisibility(View.GONE);
//                cv_edit.setVisibility(View.VISIBLE);
//                break;
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
