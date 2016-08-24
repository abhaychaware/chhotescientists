package com.kpit.chhotescientists.interfaces;

import com.kpit.chhotescientists.model.view_containers.ResultMediaButtonContainer;

/**
 * An interface used to send views between Question's views and their parent activity.
 *
 *  For example, if the view has a button to upload an image and an ImageView next to the button
 *  (the ImageView is meant to hold the chosen image), then when the intent to open the gallery goes through,
 *  the Activity will get the image as a result. Passing the ImageView to the activity through this
 *  interface will allow the activity to fill in the ImageView with the resulting image.
 *
 */
public interface ResultViewContainerReceiver {
    void setMediaButtonContainerAwaitingResult(ResultMediaButtonContainer resultMediaButtonContainer);
}
