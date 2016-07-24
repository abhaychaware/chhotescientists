package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.view.View;
import android.widget.Button;

/**
 * Created by grahamearley on 7/24/16.
 */
public class PhotoUploadQuestion extends CheckInQuestion {
    private static final int PICK_IMAGE_REQUEST = 1;

    public PhotoUploadQuestion(String question) {
        super(question);
    }

    public PhotoUploadQuestion(Parcel in) {
        super(in);
    }

    @Override
    public View getQuestionView(final Activity activity) {
        Button uploadPhotosButton = new Button(activity);
        uploadPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else (todo: do video separately?)
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                activity.startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_IMAGE_REQUEST);

                // todo: receive Activity result in Activity. Then display in ImageView.
                // todo: keep reference to the photo chosen; populate the ImageView if one was chosen earlier
            }
        });
        return null;
    }

    public static final Creator<PhotoUploadQuestion> CREATOR = new Creator<PhotoUploadQuestion>() {
        @Override
        public PhotoUploadQuestion createFromParcel(Parcel in) {
            return new PhotoUploadQuestion(in);
        }

        @Override
        public PhotoUploadQuestion[] newArray(int size) {
            return new PhotoUploadQuestion[size];
        }
    };
}
