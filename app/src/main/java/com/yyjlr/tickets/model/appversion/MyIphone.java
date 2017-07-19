package com.yyjlr.tickets.model.appversion;

/**
 * Created by Elvira on 2016/12/19.
 */

public class MyIphone {
    private String version;//当前最新版本号
    private String updateUrl;//更新地址
    private String forcedUpdate;//是否强制更新，1：是；0：否
    private String title;//标题
    private String desc;//升级说明

    public MyIphone() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getForcedUpdate() {
        return forcedUpdate;
    }

    public void setForcedUpdate(String forcedUpdate) {
        this.forcedUpdate = forcedUpdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
