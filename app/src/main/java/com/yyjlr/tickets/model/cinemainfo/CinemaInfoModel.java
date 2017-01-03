package com.yyjlr.tickets.model.cinemainfo;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 * 影院详情
 */

public class CinemaInfoModel {
    /**
     *  "cinemaName": "影院名称",
     "cinemaDesc": "影院描述",
     "cinemaImg": "http://XXXXXXX.jpg", //影院图片
     "district":"上海市浦东新区", // 影院所在行政区域
     "address": "影院地址", // 影院详细地址
     "hallType": ["IMAX","4D","4K","ATMOS"], // 影院特色
     * */
    private String cinemaName;
    private String cinemaDesc;
    private String cinemaImg;
    private String district;
    private String address;
    private String addressIcon;//图片
    private List<String> hallType;
    private List<HallType> timeAndTel;
    private List<HallType> traffic;
    private List<HallType> feature;

    public CinemaInfoModel() {
    }

    public String getAddressIcon() {
        return addressIcon;
    }

    public void setAddressIcon(String addressIcon) {
        this.addressIcon = addressIcon;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCinemaDesc() {
        return cinemaDesc;
    }

    public void setCinemaDesc(String cinemaDesc) {
        this.cinemaDesc = cinemaDesc;
    }

    public String getCinemaImg() {
        return cinemaImg;
    }

    public void setCinemaImg(String cinemaImg) {
        this.cinemaImg = cinemaImg;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getHallType() {
        return hallType;
    }

    public void setHallType(List<String> hallType) {
        this.hallType = hallType;
    }

    public List<HallType> getTimeAndTel() {
        return timeAndTel;
    }

    public void setTimeAndTel(List<HallType> timeAndTel) {
        this.timeAndTel = timeAndTel;
    }

    public List<HallType> getTraffic() {
        return traffic;
    }

    public void setTraffic(List<HallType> traffic) {
        this.traffic = traffic;
    }

    public List<HallType> getFeature() {
        return feature;
    }

    public void setFeature(List<HallType> feature) {
        this.feature = feature;
    }
}
