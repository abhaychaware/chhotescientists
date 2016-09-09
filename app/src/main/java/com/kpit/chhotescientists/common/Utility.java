package com.kpit.chhotescientists.common;

import android.content.Context;
import android.net.Uri;


/**
 * Created by AbhayC on 9/6/2016.
 */
public class Utility {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORESLASH = "/";
    public static Uri resIdToUri(Context context, int resId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName()
                + FORESLASH + resId);
    }
}
