package org.chhotescientists.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kpit.chhotescientists.R;
import org.chhotescientists.common.MyPreferences;
import org.chhotescientists.fragment.SessionFragment;
import org.chhotescientists.fragment.ExperimentsFragment;
import org.chhotescientists.fragment.UpdateFragment;
import org.chhotescientists.util.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MyPreferences myPreferences;
    TextView navUsername, navEmail;
    //tabs
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instance
        myPreferences = new MyPreferences(this);

        //single sign on flag
        myPreferences.setFirstTimeLaunch(false);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        navUsername = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_username);
        navEmail = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_email);

        navUsername.setText(myPreferences.getUserFullname());
        navEmail.setText(myPreferences.getUserEmail());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        */


        //tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {


        } else {
            snackDisplay();
        }

    }

    //function to handle snackbar
    public void snackDisplay() {
        Snackbar snackbar = Snackbar
                .make(viewPager, getString(R.string.offline_mode), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {

                            setupViewPager(viewPager);


                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    getString(R.string.no_internet_msg),
                                    Toast.LENGTH_LONG).show();
                            snackDisplay();
                        }

                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new UpdateFragment(), "Updates");
        adapter.addFragment(new ExperimentsFragment(), "Experiments");
        adapter.addFragment(new SessionFragment(), "Sessions");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_calender) {
            Intent i = new Intent(MainActivity.this, CalenderViewActivity.class);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                    MainActivity.this, R.anim.in_right_animation,
                    R.anim.out_left_animation).toBundle();
            ActivityCompat.startActivity(MainActivity.this, i, bundle);

        } else if (id == R.id.nav_profile) {
            //redirection to view my profile
            Intent i = new Intent(MainActivity.this, MyProfileActivity.class);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                    MainActivity.this, R.anim.in_right_animation,
                    R.anim.out_left_animation).toBundle();
            ActivityCompat.startActivity(MainActivity.this, i, bundle);

        } else if (id == R.id.nav_feedback) {
            //redirecting to feedback activity
            Intent i = new Intent(MainActivity.this, FeedbackActivity.class);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                    MainActivity.this, R.anim.in_right_animation,
                    R.anim.out_left_animation).toBundle();
            ActivityCompat.startActivity(MainActivity.this, i, bundle);

        } else if (id == R.id.nav_credits) {
            //redirecting to feedback activity
            Intent i = new Intent(MainActivity.this, CreditsActivity.class);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                    MainActivity.this, R.anim.in_right_animation,
                    R.anim.out_left_animation).toBundle();
            ActivityCompat.startActivity(MainActivity.this, i, bundle);

        } else if (id == R.id.nav_logout) {


            //single sign on flag
            myPreferences.setFirstTimeLaunch(true);
            Toast.makeText(MainActivity.this,
                    "Logout Successful.", Toast.LENGTH_SHORT)
                    .show();
            MainActivity.this.finish();
            Intent i = new Intent(MainActivity.this,
                    LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                    MainActivity.this,
                    R.anim.in_right_animation,
                    R.anim.out_left_animation).toBundle();
            ActivityCompat.startActivity(
                    MainActivity.this, i, bundle);


        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Cityfi");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux
                        + "https://play.google.com/store/apps/details?id=com.kpit.chhotescientists \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Choose One ,"));
            } catch (Exception e) { // e.toString();
            }
        } else if (id == R.id.nav_send) {
            goToMyApp(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToMyApp(boolean googlePlay) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((googlePlay ? "market://details?id="
                            : "amzn://apps/android?p=") + getPackageName())));
        } catch (ActivityNotFoundException e1) {
            try {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse((googlePlay ? "https://play.google.com/store/apps/details?id=com.kpit.chhotescientists"
                                : "https://play.google.com/store/apps/details?id=com.kpit.chhotescientists")
                                + getPackageName())));
            } catch (ActivityNotFoundException e2) {
                Toast.makeText(this,
                        "You don't have any app that can open this link",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
        private final List<String> mFragmentTitleList = new ArrayList<String>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}
