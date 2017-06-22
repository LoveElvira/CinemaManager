package com.yyjlr.tickets.model.film;

/**
 * Created by Elvira on 2017/5/9.
 */

public class FilmComment {
    /**
     * "userId":1,
     * "nickname":"小鸭子",
     * "headImg":"http:xxxxxxxxxxxxxx.jpg",
     * "content":"我是一只小鸭子~~~",//评论内容
     * "comTime":1544576444//评论时间
     */
    private long userId;
    private String nickname;
    private String headImg;
    private String content;
    private long comTime;

    public FilmComment() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getComTime() {
        return comTime;
    }

    public void setComTime(long comTime) {
        this.comTime = comTime;
    }
}
