package com.yyjlr.tickets.model.order;

import java.io.Serializable;

/**
 * Created by Richie on 2016/8/25.
 */
public class MovieOrderInfo implements Serializable {
    private Long id;//订单id
    private String movieName;//电影名称
    private String movieType;//电影类型
    private String language;//语言
    private String hallName;//影厅名称
    private String playDate;//播放日期
    private String startTime;//播放时间
    private String endTime;//结束时间
    private String[] seatInfos;//购买座位
    private int nums;//座位数
    private int price;//单价（app价），单位分
    private int totalPrice;//总价，单位分

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getPlayDate() {
        return playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
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

    public String[] getSeatInfos() {
        return seatInfos;
    }

    public void setSeatInfos(String[] seatInfos) {
        this.seatInfos = seatInfos;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
