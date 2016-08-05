package com.kpit.chhotescientists.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.kpit.chhotescientists.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by grahamearley on 7/31/16.
 */
public class MediaButton extends LinearLayout {

    private Button button;
    private LinearLayout imageCarousel;

    public MediaButton(Context context) {
        super(context);
        button = new Button(context);
        button.setText(R.string.add_file);

        HorizontalScrollView scrollView = new HorizontalScrollView(context);

        imageCarousel = new LinearLayout(context);
        imageCarousel.setOrientation(HORIZONTAL);

        scrollView.addView(imageCarousel);

        this.setOrientation(VERTICAL);

        LayoutParams buttonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.CENTER_HORIZONTAL;

        this.addView(button, buttonParams);
        this.addView(scrollView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public void setButtonOnClickListener(OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }
    public void addImageBitmap(Bitmap imageBitmap) {
        Context context = getContext();

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(imageBitmap);

        int height = 300;
        int width = height;

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        this.imageCarousel.addView(imageView, width, height);
    }
}
