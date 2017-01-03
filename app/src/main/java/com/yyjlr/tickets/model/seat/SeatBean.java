package com.yyjlr.tickets.model.seat;

import java.util.List;

/**
 * Created by Richie on 2016/8/18.
 */
public class SeatBean {
    private int movieId;
    private String movieName;
    private String hallName;// 影厅
    private String hallType;// 影厅类型
    private String language;// 语言
    private String movieType; // 影片类型
    private String planId;
    private Long playStartTime;// 影片开始播放时间
    private int price;
    private List<SeatTypeInfo> seatType;
    private List<SeatInfo> seatList;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Long getPlayStartTime() {
        return playStartTime;
    }

    public void setPlayStartTime(Long playStartTime) {
        this.playStartTime = playStartTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<SeatTypeInfo> getSeatType() {
        return seatType;
    }

    public void setSeatType(List<SeatTypeInfo> seatType) {
        this.seatType = seatType;
    }

    public List<SeatInfo> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<SeatInfo> seatList) {
        this.seatList = seatList;
    }
}
