package com.yyjlr.tickets.model.point;

/**
 * Created by Elvira on 2017/2/23.
 */

public class PointDetail {
    /**
     * "inOrOut":1,//1为收入，0为支出
     * "image":"http://xxxxxxxx",//图标
     * "points":100,//积分
     * "desc":"参加了刘强见面会活动，获得积分",//使用详情
     * "time":45121548654//积分使用或获取时间
     */
    private int inOrOut;//1为收入，0为支出
    private String image;//图标
    private long points;//积分
    private String desc;////使用详情
    private long time;//积分使用或获取时间

    public PointDetail() {
    }

    public int getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(int inOrOut) {
        this.inOrOut = inOrOut;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
