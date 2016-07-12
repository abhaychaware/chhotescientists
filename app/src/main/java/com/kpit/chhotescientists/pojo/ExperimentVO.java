package com.kpit.chhotescientists.pojo;

import java.io.Serializable;

/**
 * Created by VB on 4/19/2016.
 */
public class ExperimentVO implements Serializable {

    String expid, expname, expdescription, expimage, expcat, expVideoURL, expPDFUrl;

    public ExperimentVO() {

    }

    public ExperimentVO(String expid, String expname, String expdescription, String expimage, String expcat, String expVideoURL, String expPDFUrl) {
        this.expid = expid;
        this.expname = expname;
        this.expdescription = expdescription;
        this.expimage = expimage;
        this.expcat = expcat;
        this.expVideoURL = expVideoURL;
        this.expPDFUrl = expPDFUrl;

    }

    public String getExpVideoURL() {
        return expVideoURL;
    }

    public void setExpVideoURL(String expVideoURL) {
        this.expVideoURL = expVideoURL;
    }

    public String getExpPDFUrl() {
        return expPDFUrl;
    }

    public void setExpPDFUrl(String expPDFUrl) {
        this.expPDFUrl = expPDFUrl;
    }

    public String getExpcat() {
        return expcat;
    }

    public void setExpcat(String expcat) {
        this.expcat = expcat;
    }

    public String getExpid() {
        return expid;
    }

    public void setExpid(String expid) {
        this.expid = expid;
    }

    public String getExpname() {
        return expname;
    }

    public void setExpname(String expname) {
        this.expname = expname;
    }

    public String getExpdescription() {
        return expdescription;
    }

    public void setExpdescription(String expdescription) {
        this.expdescription = expdescription;
    }

    public String getExpimage() {
        return expimage;
    }

    public void setExpimage(String expimage) {
        this.expimage = expimage;
    }
}
