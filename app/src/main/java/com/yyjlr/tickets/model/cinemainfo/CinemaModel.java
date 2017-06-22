package com.yyjlr.tickets.model.cinemainfo;

import java.util.List;

/**
 * Created by Elvira on 2017/4/5.
 * 影院列表
 */

public class CinemaModel {
    private int hasMore;//是否还有信息：1有，0所有信息加载完成
    private String pagable;//分页参数
    private List<CinemaListInfo> cinemaList;

    public CinemaModel() {
    }

    public int getHasMore() {
        return hasMore;
    }

    public void setHasMore(int hasMore) {
        this.hasMore = hasMore;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
    }

    public List<CinemaListInfo> getCinemaList() {
        return cinemaList;
    }

    public void setCinemaList(List<CinemaListInfo> cinemaList) {
        this.cinemaList = cinemaList;
    }
}
