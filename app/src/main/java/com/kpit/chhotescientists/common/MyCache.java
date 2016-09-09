package com.kpit.chhotescientists.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.kpit.chhotescientists.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by AbhayC on 9/6/2016.
 */
public class MyCache  {

    private static MyCache myCache;
    private static Context myCtx;
    private Map cacheData;

    public static MyCache getInstance(Context ctx)
    {
        if(myCache==null)
        {
            myCache = new MyCache();
            myCtx = ctx;
        }
        return myCache;
    }
    public Bitmap get(String key) {
        if(cacheData==null)
        {
            cacheData = new HashMap();
        }
        Bitmap bitmap = (Bitmap) cacheData.get(key);

        if (bitmap!=null) {
            return bitmap;
        }
        return readFromCache(key);
    }

    public void put(String key, Bitmap entry) {
        if(cacheData==null)
        {
            cacheData = new HashMap();
        }
        cacheData.put(key, entry);
        persistCache(key,entry);
    }

    public void invalidate(String key, boolean fullExpire) {
        cacheData=null;
    }

    public void remove(String key) {
        if(cacheData!=null)
        {
            cacheData.remove(key);
        }
    }

    public void clear() {
        if(cacheData!=null) {
            cacheData.clear();
        }
    }

    private boolean persistCache(String key, Bitmap entry)
    {
        try {
            File root = getFileCacheDir(myCtx);
            Log.d(getClass().getName(), "Cache : Dir "+root);
            File cacheFile = new File(root, constructFileName(key));
            Log.d(getClass().getName(), "Cache : File "+cacheFile);
            FileOutputStream out;
            out = new FileOutputStream(cacheFile);
            entry.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.d(getClass().getName(), "Successfully stored object to SDCard");
            return true;
        } catch (Exception e) {
            Log.e(getClass().getName(), "Could not save the image in the cache.", e);
        }
        return false;
    }

    private Bitmap readFromCache(String key)
    {
        try {
            File root = getFileCacheDir(myCtx);
            File cacheFile = new File(root, constructFileName(key));
            Log.d(getClass().getName(), "Cache : Retrieving File "+cacheFile);
            FileInputStream in = new FileInputStream(cacheFile);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 1;
            bitmapOptions.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, bitmapOptions);
            Log.d(getClass().getName(), "Cache : Returning from Cache : "+bitmap);
            return bitmap;
        } catch (Exception e) {
            Log.e(getClass().getName(), "Could not retrieve image from cache.", e);
        }
        return null;
    }

    private File getFileCacheDir(Context context) {
        Log.i(getClass().getName(),"Checking cache dir..");
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/cs");
            dir.mkdirs();
            Log.i(getClass().getName(),"Returning > "+dir);
            return dir;
        }catch (Exception e) {
            Log.e(getClass().getName(), "Could not get internal or external storage", e);
/*
            //Find the dir to save cached images
            File cacheDir = null;
            try {
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
                    cacheDir = context.getExternalCacheDir();
                else
                    cacheDir = context.getCacheDir();
                if (!cacheDir.exists())
                    cacheDir.mkdirs();
            } catch (Exception re) {
                Log.e(getClass().getName(), "Could not get cache dir.", re);
            }
            return cacheDir;
*/
        }
        return null;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    private String constructFileName(String key) {
/*        key = key.replace("/","-");
        key = key.replace(":","_");*/
        return Integer.toString(key.hashCode());
    }
}
