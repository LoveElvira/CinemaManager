package com.yyjlr.tickets.model.event;

import java.util.List;

/**
 * Created by Elvira on 2017/1/5.
 */

public class FollowEventModel {
    private int hasMore;//是否还有卖品信息：1：有；0：所有卖品信息已经全部加载完成
    private String pagable;//分页参数
    private List<FollowEventInfo> activities;//活动信息列表

    public FollowEventModel() {
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

    public List<FollowEventInfo> getActivities() {
        return activities;
    }

    public void setActivities(List<FollowEventInfo> activities) {
        this.activities = activities;
    }
}
