package com.kpit.chhotescientists.model.result_views;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import com.kpit.chhotescientists.view.MediaButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * See parent class ResultViewContainer for documentation.
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

    @Override
    public JSONArray addContentsToTextJsonArray(JSONArray dataToSend) throws JSONException {
        JSONObject viewContentsJson = getBaseJsonDataToSend();

        // Don't send actual media data here, just send whether it exists.
        //  Media is sent to a different endpoint.
        if (bitmapBase64 != null) {
            viewContentsJson.put("response", "Yes");
        } else {
            viewContentsJson.put("response", "No");
        }

        dataToSend.put(viewContentsJson);

        return dataToSend;
    }

    @Override
    public JSONObject getMediaJsonToUpload(String eventTypeId, String scheduleId) throws JSONException {

        if (getResult().equals("")) {
            return null;
        }

        JSONObject dataObject = new JSONObject();
        dataObject.put("event_type_id", eventTypeId);
        dataObject.put("schedule_id", scheduleId);

        dataObject.put("media_type", "photo"); // TODO videos...

        dataObject.put("filebindata", getResult());

        JSONArray dataWrapper = new JSONArray();
        dataWrapper.put(dataObject);

        JSONObject resultWrapper = new JSONObject();
        resultWrapper.put("data", dataWrapper);

        return resultWrapper;
    }
}
