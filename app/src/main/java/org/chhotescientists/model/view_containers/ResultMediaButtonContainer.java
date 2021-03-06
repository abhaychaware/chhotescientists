package org.chhotescientists.model.view_containers;

import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;

import org.chhotescientists.view.MediaButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * See parent class ResultViewContainer for documentation.
 */
public class ResultMediaButtonContainer extends ResultViewContainer {

    private MediaButton mediaButton;
    Map<String, String> namedBitmapsMap;

    public ResultMediaButtonContainer(MediaButton mediaButton) {
        this.mediaButton = mediaButton;
        this.namedBitmapsMap = new HashMap<>();
    }

    @Override
    public String getStringResult() {
        // Don't send actual media data here, just send a yes/no whether it exists.
        //  Media is sent to a different endpoint.
        if (namedBitmapsMap != null && namedBitmapsMap.size() > 0) {
            return "Yes";
        } else {
            return "No";
        }
    }

    @Override
    public View getView() {
        return this.mediaButton;
    }

    public void addNamedImage(String filename, Bitmap imageBitmap) {
        // Encode full-size bitmap and store in the map, keyed by filename.
        this.namedBitmapsMap.put(filename, bitmapToBase64(imageBitmap));

        // Now, resize the bitmap for display (for performance)
        int rawHeight = imageBitmap.getHeight();
        int rawWidth = imageBitmap.getWidth();

        // Calculate the scale factor to make the width be 300:
        float scale = 300f / rawWidth;

        int targetHeight = Math.round(scale * rawHeight);
        int targetWidth = 300;

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, targetWidth, targetHeight, false);

        this.mediaButton.addImageBitmap(scaledBitmap);
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
    public List<JSONObject> getMediaJsonsToUpload(String eventTypeId, String scheduleId) throws JSONException {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();

        Set<String> filenameKeys = this.namedBitmapsMap.keySet();
        for (String filename : filenameKeys) {
            String encodedBitmap = this.namedBitmapsMap.get(filename);

            JSONObject dataObject = new JSONObject();
            dataObject.put("event_type_id", eventTypeId);
            dataObject.put("schedule_id", scheduleId);
            dataObject.put("filename", filename);

            dataObject.put("media_type", "photo"); // TODO videos...

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
