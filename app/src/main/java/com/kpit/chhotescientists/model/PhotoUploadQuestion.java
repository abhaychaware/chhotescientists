package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kpit.chhotescientists.activity.SessionCheckInActivity;
import com.kpit.chhotescientists.model.result_views.ResultMediaButtonContainer;
import com.kpit.chhotescientists.model.result_views.ResultViewContainer;
import com.kpit.chhotescientists.view.MediaButton;

/**
 * See parent CheckInQuestion for documentation.
 */
public class PhotoUploadQuestion extends CheckInQuestion {
    public static final String QUESTION_TYPE = "photo";

    public PhotoUploadQuestion(String question) {
        super(question);
    }

    public PhotoUploadQuestion(Parcel in) {
        super(in);
    }

    @Override
    public ResultViewContainer getQuestionViewContainer(final Activity activity) {
        final MediaButton mediaButton = new MediaButton(activity);

        final ResultMediaButtonContainer resultMediaButtonContainer = new ResultMediaButtonContainer(mediaButton);

        resultMediaButtonContainer.setMediaButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else (todo: do video separately?)
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                activity.startActivityForResult(Intent.createChooser(intent, "Select file"), SessionCheckInActivity.PICK_IMAGE_REQUEST);

                // Set the image view to await the result in the activity where this view will go.
                //  Then, when the user picks an image, the parent activity will fill in this image view
                //  with the selection.
                viewResultReceiver.setMediaButtonContainerAwaitingResult(resultMediaButtonContainer);

                // todo: keep reference to the photo chosen; populate the ImageView if one was chosen earlier. REALM!
            }
        });

        return resultMediaButtonContainer;
    }

    @Override
    public String getQuestionType() {
        return QUESTION_TYPE;
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
