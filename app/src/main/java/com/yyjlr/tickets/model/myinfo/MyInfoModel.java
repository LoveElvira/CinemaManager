package com.yyjlr.tickets.model.myinfo;

/**
 * Created by Elvira on 2017/1/3.
 * 个人信息
 */

public class MyInfoModel {
    private int userId;// 用户ID
    private String nickname;//用户昵称
    private String headImgUrl;// 头像URL地址
    private String sex;// 性别，1：男；2：女
    private String birthday; // 生日
    private String phone; // 手机号码
    private String sexImgUrl;//性别图标

    public MyInfoModel() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSexImgUrl() {
        return sexImgUrl;
    }

    public void setSexImgUrl(String sexImgUrl) {
        this.sexImgUrl = sexImgUrl;
    }
}
