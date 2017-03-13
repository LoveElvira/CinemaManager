package com.yyjlr.tickets.model;

import com.yyjlr.tickets.model.event.CollectUserInfo;

import java.util.List;

/**
 * Created by Elvira on 2017/3/1.
 * 点击收藏返回的数据
 */

public class ResponeCollect {
    private int isInterest;
    private int interestUsers;
    private List<CollectUserInfo> interestUserInfo;

    public ResponeCollect() {
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
