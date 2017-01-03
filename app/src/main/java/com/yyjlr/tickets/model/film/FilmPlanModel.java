package com.yyjlr.tickets.model.film;

import java.util.List;

/**
 * Created by Elvira on 2016/12/28.
 * 影片排期
 */

public class FilmPlanModel {
    private int movieId;
    private String name;
    private List<FilmPlan> planList;

    public FilmPlanModel() {
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FilmPlan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<FilmPlan> planList) {
        this.planList = planList;
    }
}
