package com.yyjlr.tickets.model.point;

import java.util.List;

/**
 * Created by Elvira on 2017/2/23.
 */

public class PointList {
    /**
     * "hasMore":1, //是否还有信息：1：有；0：所有信息已经全部加载完成
     * "pagable":"1", //分页参数
     * "nowPoints": 10000, //当前积分
     * "oldPoints":15000, //历史积分
     */
    private int hasMore;//是否还有卖品信息：1：有；0：所有卖品信息已经全部加载完成
    private String pagable;//分页参数
    private long newPoints; //当前积分
    private long oldPoints;//历史积分
    private List<PointDetail> pointsDetail;

    public PointList() {
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

    public long getNewPoints() {
        return newPoints;
    }

    public void setNewPoints(long newPoints) {
        this.newPoints = newPoints;
    }

    public long getOldPoints() {
        return oldPoints;
    }

    public void setOldPoints(long oldPoints) {
        this.oldPoints = oldPoints;
    }

    public List<PointDetail> getPointsDetail() {
        return pointsDetail;
    }

    public void setPointsDetail(List<PointDetail> pointsDetail) {
        this.pointsDetail = pointsDetail;
    }
}
