package com.yyjlr.tickets.model.ticket;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 */

public class EventEntity {
    private int hasMore;//是否还有卖品信息：1：有；0：所有卖品信息已经全部加载完成
    private String pagable;//分页参数
    private List<EventInfoEntity> activityList;

    public EventEntity() {
    }

    public List<EventInfoEntity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<EventInfoEntity> activityList) {
        this.activityList = activityList;
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
}
