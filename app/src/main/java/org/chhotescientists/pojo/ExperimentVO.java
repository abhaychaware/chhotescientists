package org.chhotescientists.pojo;

import java.io.Serializable;


public class ExperimentVO implements Serializable {

    String expid;
    String expname;
    String expdescription;

    String expdescriptionShort;
    String expicon;
    String expstandard;

    public String getExpstandard() {
        return expstandard;
    }

    public void setExpstandard(String expstandard) {
        this.expstandard = expstandard;
    }

    public String getExpboard() {
        return expboard;
    }

    public void setExpboard(String expboard) {
        this.expboard = expboard;
    }

    public String getExptextbookreference() {
        return exptextbookreference;
    }

    public void setExptextbookreference(String exptextbookreference) {
        this.exptextbookreference = exptextbookreference;
    }

    String expboard;
    String exptextbookreference;
    String expcat;
    String expVideoURL;
    String expPDFUrl;

    public String[] getExpimages() {
        return expimages;
    }

    public void setExpimages(String[] expimages) {
        this.expimages = expimages;
    }

    String[] expimages;

    public ExperimentVO() {

    }

    public ExperimentVO(String expid, String expname, String expdescription,String expdescriptionShort, String expicon, String[] expimages,String expcat, String expVideoURL, String expPDFUrl) {
        this.expid = expid;
        this.expname = expname;
        this.expdescriptionShort = expdescriptionShort;
        this.expdescription = expdescription;
        this.expicon = expicon;
        this.expimages=expimages;
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

    public String getExpicon() {
        return expicon;
    }

    public void setExpicon(String expicon) {
        this.expicon = expicon;
    }

    public String getExpdescriptionShort() {
        return expdescriptionShort;
    }

    public void setExpdescriptionShort(String expdescriptionShort) {this.expdescriptionShort = expdescriptionShort; }


}
