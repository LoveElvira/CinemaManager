package com.yyjlr.tickets.requestdata;

import com.yyjlr.tickets.service.IRequestMainData;

/**
 * Created by Elvira on 2016/12/20.
 */

public class IdRequest implements IRequestMainData {
    private String activityId;
    private String movieId;//获取影院详情
    private String planId;//排期ID
    private String orderId;//订单号

    public IdRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
