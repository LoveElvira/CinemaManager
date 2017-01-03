package com.yyjlr.tickets.model.film;

import java.util.List;

/**
 * Created by Elvira on 2016/12/28.
 * 排期内容
 */

public class FilmPlanContent {
    private String planId;// 排期ID
    private String startTime;
    private String endTime;
    private String language;
    private String movieType;// 影片类型
    private String hallName;// 影厅
    private String hallType;// 影厅类型
    private String appPrice;//APP价
    private String price;//会员价
    private String orgPrice;//原价
    private String memberCardPrice;//最低会员卡价格
    private int playTimeIconType;// 电影播放时间图标类型，1：白天；0：黑夜
    private int countDown;//剩余可购买时间

    public FilmPlanContent() {
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getHallType() {
        return hallType;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
    }

    public String getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(String appPrice) {
        this.appPrice = appPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrgPrice() {
        return orgPrice;
    }

    public void setOrgPrice(String orgPrice) {
        this.orgPrice = orgPrice;
    }

    public String getMemberCardPrice() {
        return memberCardPrice;
    }

    public void setMemberCardPrice(String memberCardPrice) {
        this.memberCardPrice = memberCardPrice;
    }

    public int getPlayTimeIconType() {
        return playTimeIconType;
    }

    public void setPlayTimeIconType(int playTimeIconType) {
        this.playTimeIconType = playTimeIconType;
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }
}
