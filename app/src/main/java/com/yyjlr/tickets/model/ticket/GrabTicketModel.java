package com.yyjlr.tickets.model.ticket;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 */

public class GrabTicketModel {
    private List<TicketModel> activityList;

    public GrabTicketModel() {
    }

    public List<TicketModel> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<TicketModel> activityList) {
        this.activityList = activityList;
    }
}
