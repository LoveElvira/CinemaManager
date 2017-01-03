package com.yyjlr.tickets.model.chosen;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 * 精选首页
 */

public class ChosenModel {
    private CinemaInfo cinemaInfo;
    private List<EventInfo> activityList;

    public ChosenModel() {
    }

    public CinemaInfo getCinemaInfo() {
        return cinemaInfo;
    }

    public void setCinemaInfo(CinemaInfo cinemaInfo) {
        this.cinemaInfo = cinemaInfo;
    }

    public List<EventInfo> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<EventInfo> activityList) {
        this.activityList = activityList;
    }
}
