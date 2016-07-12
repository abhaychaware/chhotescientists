package com.kpit.chhotescientists.pojo;

import java.io.Serializable;

/**
 * Created by VB on 4/21/2016.
 */
public class VersionCheckVO implements Serializable {

    String version, mendatory;

    public VersionCheckVO() {
    }

    public VersionCheckVO(String version, String mendatory) {
        this.version = version;
        this.mendatory = mendatory;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMendatory() {
        return mendatory;
    }

    public void setMendatory(String mendatory) {
        this.mendatory = mendatory;
    }
}
