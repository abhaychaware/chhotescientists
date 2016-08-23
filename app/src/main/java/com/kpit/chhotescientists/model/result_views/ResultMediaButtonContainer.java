package com.kpit.chhotescientists.model.result_views;

import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;

import com.kpit.chhotescientists.view.MediaButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public void addImage(Bitmap imageBitmap) {
        this.mediaButton.addImageBitmap(imageBitmap);
        this.bitmapBase64 = this.bitmapToBase64(imageBitmap);
    }

    public void setMediaButtonOnClick(View.OnClickListener onClickListener) {
        this.mediaButton.setButtonOnClickListener(onClickListener);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    @Override
    public JSONArray addContentsToTextJsonArray(JSONArray dataToSend) throws JSONException {
        JSONObject viewContentsJson = getBaseJsonDataToSend();

        // Don't send actual media data here, just send a yes/no whether it exists.
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
    public List<JSONObject> getMediaJsonsToUpload(String eventTypeId, String scheduleId) throws JSONException {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();

        Map map = mediaButton.getImageBitmaps();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            Bitmap bitmap = (Bitmap)map.get(key);

            JSONObject dataObject = new JSONObject();
            dataObject.put("event_type_id", eventTypeId);
            dataObject.put("schedule_id", scheduleId);
            dataObject.put("filename", key);

            dataObject.put("media_type", "photo"); // TODO videos...

            String encodedBitmap = bitmapToBase64(bitmap);
            dataObject.put("filebindata", encodedBitmap);

            JSONArray dataWrapper = new JSONArray();
            dataWrapper.put(dataObject);

            JSONObject resultWrapper = new JSONObject();
            resultWrapper.put("data", dataWrapper);

            jsonObjects.add(resultWrapper);
        }

        return jsonObjects;
    }
}
