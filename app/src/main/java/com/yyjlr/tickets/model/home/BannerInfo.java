package com.yyjlr.tickets.model.home;

/**
 * Created by Elvira on 2017/4/7.
 */

public class BannerInfo {
    /**
     * "id": 1,//id
     * "imageUrl": "http://xxxxxx.jpg",//图片地址
     * "type": 1,//类型，1表示影院，2表示活动,3广告页
     * "gotoUrl":"https://www.baidu.com" //跳转地址 （只有type=3时 此字段才会有值）
     * "address":"SFC上影-世博店", // 活动地点
     * "startTime": 40000, //活动开始时间
     * "endTime":50000, // 活动结束时间
     * "price": "免费", //活动费用
     */
    private long id;
    private String imageUrl;
    private int type;
    private String gotoUrl;
    private String address;
    private long startTime;
    private long endTime;
    private String price;

    public BannerInfo() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGotoUrl() {
        return gotoUrl;
    }

    public void setGotoUrl(String gotoUrl) {
        this.gotoUrl = gotoUrl;
    }
}
