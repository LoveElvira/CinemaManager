package com.yyjlr.tickets.model.seat;

/**
 * Created by Richie on 2016/8/18.
 */
public class SeatTypeInfo {
    private String name;
    private String type;
    private int seats;// 占用座位数
    private String icon;//图标URL地址
    private String isShow;//1显示在页面，0不显示

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}
