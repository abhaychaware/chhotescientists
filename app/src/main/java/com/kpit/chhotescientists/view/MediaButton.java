package com.kpit.chhotescientists.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by grahamearley on 7/31/16.
 */
public class MediaButton extends LinearLayout {

    private Button button;
    private ImageView imageView;

    public MediaButton(Context context) {
        super(context);
        button = new Button(context);
        button.setText("Add file");

        imageView = new ImageView(context);

        this.setOrientation(HORIZONTAL);
        this.addView(button);
        this.addView(imageView);
    }

    public void setButtonText(String text) {
        this.button.setText(text);
    }

    public void setImageView(Drawable image) {
        imageView.setImageDrawable(image);
    }

    public void setButtonOnClickListener(OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageView.setImageBitmap(imageBitmap);
    }
}
