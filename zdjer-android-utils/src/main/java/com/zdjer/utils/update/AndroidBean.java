package com.zdjer.utils.update;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by zdj on 16/4/28.
 */
@XStreamAlias("android")
public class AndroidBean implements Serializable {
    @XStreamAlias("versionCode")
    private int versionCode;
    @XStreamAlias("versionName")
    private String versionName;
    @XStreamAlias("downloadUrl")
    private String downloadUrl;
    @XStreamAlias("updateLog")
    private String updateLog;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }
}