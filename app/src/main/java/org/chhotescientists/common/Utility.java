package org.chhotescientists.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.net.URL;


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

    public static Bitmap getScaledBitmapFromUrl(String imageUrl, int requiredWidth, int requiredHeight) {
        Bitmap bm = null;
        try {
            URL url = new URL(imageUrl);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
            if (requiredWidth == 0 && requiredHeight == 0) {
                options.inSampleSize = 1;
            } else {
                options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
            }
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

    static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
