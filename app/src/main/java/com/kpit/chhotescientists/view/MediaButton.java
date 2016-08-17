package com.kpit.chhotescientists.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        final Context context = getContext();

        final ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(imageBitmap);

        int height = 300;
        int width = height;

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.remove_photo))
                        .setMessage(context.getString(R.string.do_you_want_to_remove_photo))
                        .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imageCarousel.removeView(imageView);
                            }
                        })
                        .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        });

        this.imageCarousel.addView(imageView, width, height);
    }

    public Map<String,Bitmap> getImageBitmaps() {
        Map<String,Bitmap> bitmaps = new HashMap<String, Bitmap>();
        for (int i = 0; i < imageCarousel.getChildCount(); i++) {
            ImageView imageVewChild = (ImageView) imageCarousel.getChildAt(i);

            Bitmap bitmap = ((BitmapDrawable) imageVewChild.getDrawable()).getBitmap();
            // TODO: 8/12/2016 capture the image name from file chooser and use it here
            bitmaps.put("pic"+i+".jpg",bitmap);
        }

        return bitmaps;
    }
}
