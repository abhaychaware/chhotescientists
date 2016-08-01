package com.kpit.chhotescientists.model.result_views;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import com.kpit.chhotescientists.view.MediaButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by grahamearley on 7/31/16.
 */
public class ResultMediaButtonContainer extends ResultViewContainer {

    private MediaButton mediaButton;
    private String bitmapBase64;

    public ResultMediaButtonContainer(MediaButton mediaButton) {
        this.mediaButton = mediaButton;
    }

    @Override
    public String getResult() {
        if (bitmapBase64 != null) {
            return bitmapBase64;
        } else {
            return "";
        }
    }

    @Override
    public View getView() {
        return this.mediaButton;
    }

    public void setImageWithFile(Bitmap imageBitmap) {
        this.mediaButton.setImageBitmap(imageBitmap);
        this.bitmapBase64 = this.bitmapToBase64(imageBitmap);
    }

    public void setMediaButtonOnClick(View.OnClickListener onClickListener) {
        this.mediaButton.setButtonOnClickListener(onClickListener);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
