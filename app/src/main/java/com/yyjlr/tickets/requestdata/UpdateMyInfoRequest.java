package com.yyjlr.tickets.requestdata;

import com.yyjlr.tickets.service.IRequestMainData;

/**
 * Created by Elvira on 2017/1/5.
 */

public class UpdateMyInfoRequest implements IRequestMainData {
    private String nickname; //昵称
    private String birthday; //生日
    private String sex;//性别1男，2女
    private String phone;//手机号
    private String headImgUrl;//手机号

    public UpdateMyInfoRequest() {
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }
}
