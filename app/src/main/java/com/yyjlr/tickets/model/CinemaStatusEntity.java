package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2017/6/22.
 * 影院运营状态
 */

public class CinemaStatusEntity {
    private String state;
    private String message;

    public CinemaStatusEntity() {
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
}
