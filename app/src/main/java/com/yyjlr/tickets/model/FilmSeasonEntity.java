package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2016/8/2.
 * 影片场次
 */
public class FilmSeasonEntity {
    private String sunOrMoon;
    private String startTime;
    private String endTime;
    private String language;
    private String filmType;
    private String hall;
    private String hallType;
    private String originalPrice;
    private String appPrice;
    private String vipPrice;

    public FilmSeasonEntity() {}

    public FilmSeasonEntity(String sunOrMoon, String startTime, String endTime, String language, String filmType, String hall, String hallType, String originalPrice, String appPrice, String vipPrice) {
        this.sunOrMoon = sunOrMoon;
        this.startTime = startTime;
        this.endTime = endTime;
        this.language = language;
        this.filmType = filmType;
        this.hall = hall;
        this.hallType = hallType;
        this.originalPrice = originalPrice;
        this.appPrice = appPrice;
        this.vipPrice = vipPrice;
    }

    public String getSunOrMoon() {
        return sunOrMoon;
    }

    public void setSunOrMoon(String sunOrMoon) {
        this.sunOrMoon = sunOrMoon;
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

    public String getFilmType() {
        return filmType;
    }

    public void setFilmType(String filmType) {
        this.filmType = filmType;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getHallType() {
        return hallType;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(String appPrice) {
        this.appPrice = appPrice;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }
}
