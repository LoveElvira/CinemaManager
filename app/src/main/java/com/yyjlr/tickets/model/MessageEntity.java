package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2016/8/3.
 */
public class MessageEntity {
    private String title;
    private String time;
    private String message;

    public MessageEntity() {}

    public MessageEntity(String title, String time, String message) {
        this.title = title;
        this.time = time;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
