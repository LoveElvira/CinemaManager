package com.yyjlr.tickets.service;

/**
 * Created by Elvira on 16/8/11.
 */
public class ResponseData {
    private int statusCode;

    public ResponseData() {
    }

    public ResponseData(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
