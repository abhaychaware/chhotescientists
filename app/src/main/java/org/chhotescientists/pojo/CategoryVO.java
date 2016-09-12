package org.chhotescientists.pojo;

import java.io.Serializable;

/**
 * Created by Dnyanesh on 3/22/2016.
 */
public class CategoryVO implements Serializable {

    String catID, catName, catDesc, catNotificationCount, catImage;

    public CategoryVO() {

    }

    public CategoryVO(String catID, String catName, String catDesc, String catNotificationCount, String catImage) {
        this.catID = catID;
        this.catName = catName;
        this.catDesc = catDesc;
        this.catNotificationCount = catNotificationCount;
        this.catImage = catImage;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public String getCatNotificationCount() {
        return catNotificationCount;
    }

    public void setCatNotificationCount(String catNotificationCount) {
        this.catNotificationCount = catNotificationCount;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }
}
