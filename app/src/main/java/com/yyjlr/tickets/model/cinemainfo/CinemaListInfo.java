package com.yyjlr.tickets.model.cinemainfo;

/**
 * Created by Elvira on 2017/4/5.
 */

public class CinemaListInfo {
    /**
     * "id": 50120515,
     * "name": "某某影院",
     * "address": "上海市普陀区长寿路",
     * "distance":"1千米"
     */
    private long id;
    private String name;
    private String address;
    private String distance;
    private int checked;
    private String state;//1正常营业  0店面维护
    private String message;

    public CinemaListInfo() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
