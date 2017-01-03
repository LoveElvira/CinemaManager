package com.yyjlr.tickets.model.film;

import java.util.List;

/**
 * Created by Elvira on 2016/12/28.
 */

public class FilmPlan {
    private String date;
    private List<FilmPlanContent> sessionList;

    public FilmPlan() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<FilmPlanContent> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<FilmPlanContent> sessionList) {
        this.sessionList = sessionList;
    }
}
