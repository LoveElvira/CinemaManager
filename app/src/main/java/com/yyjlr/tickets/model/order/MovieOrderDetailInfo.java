package com.yyjlr.tickets.model.order;

import java.util.List;

/**
 * Created by Richie on 2016/9/7.
 */
public class MovieOrderDetailInfo {
    private String movieName;//电影名称
    private Long startTime;//开始放映时间
    private Long endTime;//开始放映时间
    private String language;//语言
    private String movieType;//影片类型
    private String cinemaName;//影院名称
    private String hallName;//影厅名称
    private List<String> seatInfo;//座位信息
    private String phone;//手机号
    private String movieImg;//电影图片
    private String validCode;//取票码
    private String serialNumber;//序列号

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getMovieImg() {
        return movieImg;
    }

    public void setMovieImg(String movieImg) {
        this.movieImg = movieImg;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
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

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public List<String> getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(List<String> seatInfo) {
        this.seatInfo = seatInfo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
