package com.yyjlr.tickets.model.chosen;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 * 影院信息
 */

public class CinemaInfo {
    /**
     * "cinemaName": "影院名称",
     * "address": "影院地址",
     * "feature": ["3d眼镜","儿童票","停车场"],
     * "hallType": ["IMAX","4D","4K","ATMOS"]
     */

    private String cinemaName;
    private String address;
    private List<String> feature;
    private List<String> hallType;

    public CinemaInfo() {
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getFeature() {
        return feature;
    }

    public void setFeature(List<String> feature) {
        this.feature = feature;
    }

    public List<String> getHallType() {
        return hallType;
    }

    public void setHallType(List<String> hallType) {
        this.hallType = hallType;
    }
}
