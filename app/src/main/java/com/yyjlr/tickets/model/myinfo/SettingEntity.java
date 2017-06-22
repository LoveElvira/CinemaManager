package com.yyjlr.tickets.model.myinfo;

import java.util.List;

/**
 * Created by Elvira on 2017/6/19.
 * 个人中心首页
 */

public class SettingEntity {
    /**
     * "nickname":"小蛮腰",//用户昵称
     * "headImgUrl":"http://xxxxxx",//头像图标
     * "sexImgUrl":"http:xcsdfsdfdsfsdf.jpg",//性别图标
     * "allModuleInfo":
     */
    private String nickname;
    private String headImgUrl;
    private String sexImgUrl;
    private List<List<SettingInfoEntity>> allModuleInfo;

    public SettingEntity() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getSexImgUrl() {
        return sexImgUrl;
    }

    public void setSexImgUrl(String sexImgUrl) {
        this.sexImgUrl = sexImgUrl;
    }

    public List<List<SettingInfoEntity>> getAllModuleInfo() {
        return allModuleInfo;
    }

    public void setAllModuleInfo(List<List<SettingInfoEntity>> allModuleInfo) {
        this.allModuleInfo = allModuleInfo;
    }
}
