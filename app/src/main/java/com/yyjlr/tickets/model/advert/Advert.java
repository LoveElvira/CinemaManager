package com.yyjlr.tickets.model.advert;

/**
 * Created by Elvira on 2016/12/20.
 */

public class Advert {
//    "actionUrl":"", // 点击跳转地址
//    "imageUrl": "http://xxxx.jpg" //广告图
    private String actionUrl;// 点击跳转地址
    private String imageUrl;//广告图

    public Advert() {
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
