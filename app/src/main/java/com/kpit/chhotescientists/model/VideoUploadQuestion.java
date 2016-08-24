package com.kpit.chhotescientists.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.view.View;

import com.kpit.chhotescientists.activity.SessionCheckInActivity;
import com.kpit.chhotescientists.model.view_containers.ResultMediaButtonContainer;
import com.kpit.chhotescientists.model.view_containers.ResultViewContainer;
import com.kpit.chhotescientists.view.MediaButton;

/**
 * See parent CheckInQuestion for documentation.
 */
public class VideoUploadQuestion extends CheckInQuestion {
    public static final String QUESTION_TYPE = "video";

    public VideoUploadQuestion(String question) {
        super(question);
    }

    public VideoUploadQuestion(Parcel in) {
        super(in);
    }

    // TODO: MAKE THIS NOT BE PHOTOS!! this is a direct copy-paste  for now.
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

    public static final Creator<VideoUploadQuestion> CREATOR = new Creator<VideoUploadQuestion>() {
        @Override
        public VideoUploadQuestion createFromParcel(Parcel in) {
            return new VideoUploadQuestion(in);
        }

        @Override
        public VideoUploadQuestion[] newArray(int size) {
            return new VideoUploadQuestion[size];
        }
    };
}
