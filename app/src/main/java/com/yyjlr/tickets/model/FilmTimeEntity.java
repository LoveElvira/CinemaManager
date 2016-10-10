package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2016/8/2.
 */
public class FilmTimeEntity {
    private String time;
    private String week;

    public FilmTimeEntity() {}

    public FilmTimeEntity(String time, String week) {
        this.time = time;
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
