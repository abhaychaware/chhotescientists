package org.chhotescientists.pojo;

import java.io.Serializable;

/**
 * Created by dnyaneshwarkanpurne on 3/17/16.
 */
public class UpdatesVO implements Serializable {
    String updateID, updateHeading, updateDescription, updateImage, updateDate, updateTimestamp;

    public UpdatesVO() {
    }

    public UpdatesVO(String updateID, String updateHeading, String updateDescription, String updateImage, String updateDate, String updateTimestamp) {
        this.updateID = updateID;
        this.updateHeading = updateHeading;
        this.updateDescription = updateDescription;
        this.updateImage = updateImage;
        this.updateDate = updateDate;
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateID() {
        return updateID;
    }

    public void setUpdateID(String updateID) {
        this.updateID = updateID;
    }

    public String getUpdateHeading() {
        return updateHeading;
    }

    public void setUpdateHeading(String updateHeading) {
        this.updateHeading = updateHeading;
    }

    public String getUpdateDescription() {
        return updateDescription;
    }

    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription;
    }

    public String getUpdateImage() {
        return updateImage;
    }

    public void setUpdateImage(String updateImage) {
        this.updateImage = updateImage;
    }
}
