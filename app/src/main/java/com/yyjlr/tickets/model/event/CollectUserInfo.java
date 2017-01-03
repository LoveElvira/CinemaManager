package com.yyjlr.tickets.model.event;

/**
 * Created by Elvira on 2016/12/20.
 * 收藏用户信息
 */

public class CollectUserInfo {
    /**
     * "nickName":"小蛮腰", // 用户昵称
     "imageUrl":"http://xxxxx" // 用户头像地址
     * */
    private String nickName;
    private String imageUrl;

    public CollectUserInfo() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
