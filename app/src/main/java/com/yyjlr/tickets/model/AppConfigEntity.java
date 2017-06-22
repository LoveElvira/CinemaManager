package com.yyjlr.tickets.model;

import java.io.Serializable;

/**
 * Created by Elvira on 2017/6/19.
 * 获取APP设置信息
 */

public class AppConfigEntity implements Serializable {
    /**
     * "tel":"400", // 影院经理人客服电话
     * "styleImage":"url"//背景图片
     * "movieIconImage":"url",//影片按钮
     * "movieIconoptImage":"url",//影片按钮选中
     * "activityIconImage":"url",//活动按钮
     * "activityIconoptImage":"url",//活动按钮选中
     * "storeIconImage":"url",//商城按钮
     * "storeIconoptImage":"url",//商城按钮选中
     * "myIconImage":"url",//我的按钮
     * "myIconoptImage":"url",//我的按钮选中
     * "fontColor":"4e78f2"//按钮字体色号
     */
    private String tel;
    private String styleImage;
    private String movieIconImage;
    private String movieIconoptImage;
    private String activityIconImage;
    private String activityIconoptImage;
    private String storeIconImage;
    private String storeIconoptImage;
    private String myIconImage;
    private String myIconoptImage;
    private String fontColor;

    public AppConfigEntity() {
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStyleImage() {
        return styleImage;
    }

    public void setStyleImage(String styleImage) {
        this.styleImage = styleImage;
    }

    public String getMovieIconImage() {
        return movieIconImage;
    }

    public void setMovieIconImage(String movieIconImage) {
        this.movieIconImage = movieIconImage;
    }

    public String getMovieIconoptImage() {
        return movieIconoptImage;
    }

    public void setMovieIconoptImage(String movieIconoptImage) {
        this.movieIconoptImage = movieIconoptImage;
    }

    public String getActivityIconImage() {
        return activityIconImage;
    }

    public void setActivityIconImage(String activityIconImage) {
        this.activityIconImage = activityIconImage;
    }

    public String getActivityIconoptImage() {
        return activityIconoptImage;
    }

    public void setActivityIconoptImage(String activityIconoptImage) {
        this.activityIconoptImage = activityIconoptImage;
    }

    public String getStoreIconImage() {
        return storeIconImage;
    }

    public void setStoreIconImage(String storeIconImage) {
        this.storeIconImage = storeIconImage;
    }

    public String getStoreIconoptImage() {
        return storeIconoptImage;
    }

    public void setStoreIconoptImage(String storeIconoptImage) {
        this.storeIconoptImage = storeIconoptImage;
    }

    public String getMyIconImage() {
        return myIconImage;
    }

    public void setMyIconImage(String myIconImage) {
        this.myIconImage = myIconImage;
    }

    public String getMyIconoptImage() {
        return myIconoptImage;
    }

    public void setMyIconoptImage(String myIconoptImage) {
        this.myIconoptImage = myIconoptImage;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
}
