package com.kpit.chhotescientists.common;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dnyaneshwar on 11/19/15.
 */

public class MyPreferences {

    private static final String PREFERENCES_NAME = "chhotescientists_preferences";
    private static final String IS_FIRST_LAUNCH = "isFirstTimeLaunchPref";
    private static final String IS_LOGGED_IN = "isLoggedInPref";
    private static final String USER_ID = "user_id";
    private static final String USER_FULLNAME = "user_fullname";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_MOBILE = "user_mobile";
    private static final String USER_RESIDENCE = "user_area_of_residence";
    private static final String USER_CENTER = "user_nearest_cs_center";
    private static final String USER_NAME = "userNamePref";
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public MyPreferences(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTimeLaunch) {
        editor.putBoolean(IS_FIRST_LAUNCH, isFirstTimeLaunch);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    //getters and setters method

    //user id
    public String getUserId() {
        return preferences.getString(USER_ID, "");
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    //user firstname
    public String getUserFullname() {
        return preferences.getString(USER_FULLNAME, "");
    }

    public void setUserFullname(String fullname) {
        editor.putString(USER_FULLNAME, fullname);
        editor.commit();
    }

    //user email
    public String getUserEmail() {
        return preferences.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    //user mobile
    public String getUserMobile() {
        return preferences.getString(USER_MOBILE, "");
    }

    public void setUserMobile(String mobile) {
        editor.putString(USER_MOBILE, mobile);
        editor.commit();
    }

    //username

    public String getUserName() {
        return preferences.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public String getUserResidence() {
        return preferences.getString(USER_RESIDENCE, "");
    }

    //user area of residene
    public void setUserResidence(String residence) {
        editor.putString(USER_RESIDENCE, residence);
        editor.commit();
    }

    public String getUserCenter() {
        return preferences.getString(USER_CENTER, "");
    }

    //user nearest chhote scientists center
    public void setUserCenter(String center) {
        editor.putString(USER_CENTER, center);
        editor.commit();
    }
}