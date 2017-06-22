package com.yyjlr.tickets.model.event;

import com.yyjlr.tickets.model.chosen.ShareInfo;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 * 活动详情
 */

public class EventModel {
    private long activityId;
    private int activityType;
    private String activityName;
    private String activityDesc;
    private ShareInfo share;
    private String address;
    private long startTime;
    private long endTime;
    private long price;
    private String activityImg;
    private int isInterest;
    private int interestUsers;
    private List<CollectUserInfo> interestUserInfo;

    private String jumpUrl;

    public EventModel() {
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public ShareInfo getShare() {
        return share;
    }

    public void setShare(ShareInfo share) {
        this.share = share;
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

    public int getIsInterest() {
        return isInterest;
    }

    public void setIsInterest(int isInterest) {
        this.isInterest = isInterest;
    }

    public int getInterestUsers() {
        return interestUsers;
    }

    public void setInterestUsers(int interestUsers) {
        this.interestUsers = interestUsers;
    }

    public List<CollectUserInfo> getInterestUserInfo() {
        return interestUserInfo;
    }

    public void setInterestUserInfo(List<CollectUserInfo> interestUserInfo) {
        this.interestUserInfo = interestUserInfo;
    }
}
