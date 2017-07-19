package com.yyjlr.tickets.model.appversion;

/**
 * Created by Elvira on 2016/12/19.
 * 版本更新
 */

public class AppVersion {
    private String launchImage;//闪屏图
    private MyAndroid android;
    private MyIphone iphone;
    private Readme readme;// 阅读须知
    private Theme theme;//  主题分类配置
    private int videoShow;//是否播放视频
    private int scrollPageShow;//是否播放闪屏页
    private int showExpress;//是否显示物流信息

    public AppVersion() {
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getLaunchImage() {
        return launchImage;
    }

    public void setLaunchImage(String launchImage) {
        this.launchImage = launchImage;
    }

    public MyAndroid getAndroid() {
        return android;
    }

    public void setAndroid(MyAndroid android) {
        this.android = android;
    }

    public MyIphone getIphone() {
        return iphone;
    }

    public void setIphone(MyIphone iphone) {
        this.iphone = iphone;
    }

    public Readme getReadme() {
        return readme;
    }

    public void setReadme(Readme readme) {
        this.readme = readme;
    }

    public int getVideoShow() {
        return videoShow;
    }

    public void setVideoShow(int videoShow) {
        this.videoShow = videoShow;
    }

    public int getScrollPageShow() {
        return scrollPageShow;
    }

    public void setScrollPageShow(int scrollPageShow) {
        this.scrollPageShow = scrollPageShow;
    }

    public int getShowExpress() {
        return showExpress;
    }

    public void setShowExpress(int showExpress) {
        this.showExpress = showExpress;
    }
}
