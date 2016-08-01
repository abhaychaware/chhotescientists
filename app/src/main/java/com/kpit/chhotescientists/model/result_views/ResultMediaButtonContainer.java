package com.kpit.chhotescientists.model.result_views;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.View;

import com.kpit.chhotescientists.view.MediaButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by grahamearley on 7/31/16.
 */
public class ResultMediaButtonContainer extends ResultViewContainer {

    private MediaButton mediaButton;

    public ResultMediaButtonContainer(MediaButton mediaButton) {
        this.mediaButton = mediaButton;
    }

    @Override
    public String getResult() {
        if (mediaButton.getMediaFileStream() != null) {
            return mediaButton.getMediaFileStream().toString();
        } else {
            return "";
        }
    }

    @Override
    public View getView() {
        return this.mediaButton;
    }

    public void setImageWithFile(Bitmap imageBitmap, InputStream fileInputStream) {
        this.mediaButton.setImageBitmap(imageBitmap);
        this.mediaButton.setMediaFileStream(fileInputStream);
    }

    public void setMediaButtonOnClick(View.OnClickListener onClickListener) {
        this.mediaButton.setButtonOnClickListener(onClickListener);
    }
}
