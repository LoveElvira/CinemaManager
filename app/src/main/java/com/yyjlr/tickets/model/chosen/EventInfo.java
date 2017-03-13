package com.yyjlr.tickets.model.chosen;

/**
 * Created by Elvira on 2016/12/20.
 */

public class EventInfo {
    /**
     * "activityId": 300674,
     "activityName": "活动名称",
     "activityType":1, // 活动类别，1：电影；2：特价票；3：明星见面会；4：商户合作活动
     "share": {
     "url":"http://xxxx", // 分享地址
     "title":"分享标题", // 分享标题
     "memo":"分享描述" // 分享描述
     },
     "address":"SFC上影-世博店", // 活动地点
     "startTime": 40000, //活动开始时间
     "endTime":50000, // 活动结束时间
     "price": "免费", //活动费用
     "activityImg": "http://xxxx1.jpg" //活动图
     * */

    private long activityId;
    private String activityName;
    private int activityType; // 活动类别，1：电影；2：特价票；3：明星见面会；4：商户合作活动
    private String address;
    private long startTime;
    private long endTime;
    private long price;
    private String activityImg;
    private ShareInfo share;

    public EventInfo() {
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg;
    }

    public ShareInfo getShare() {
        return share;
    }

    public void setShare(ShareInfo share) {
        this.share = share;
    }
}
