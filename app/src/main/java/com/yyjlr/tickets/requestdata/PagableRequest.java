package com.yyjlr.tickets.requestdata;

import com.yyjlr.tickets.service.IRequestMainData;

/**
 * Created by Elvira on 2016/12/20.
 * 分页
 */

public class PagableRequest implements IRequestMainData {
    private String pagable;
    private String type;
    private String lon;//经度
    private String lat;//纬度
    private String goodsName;//搜索内容
    private String movieId;//电影ID
    private String status;//状态：1未使用，2已使用，3已过期


    public PagableRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
    }
}
