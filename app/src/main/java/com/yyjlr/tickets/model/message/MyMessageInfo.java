package com.yyjlr.tickets.model.message;

import java.io.Serializable;

/**
 * Created by Richie on 2016/9/13.
 */
public class MyMessageInfo implements Serializable{
    private Long messageId;//消息id
    private String title;//消息标题
    private String content;//消息内容
    private Long sendDate;//消息发送时间
    private String isRead;//是否已读，1已读，0未读

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSendDate() {
        return sendDate;
    }

    public void setSendDate(Long sendDate) {
        this.sendDate = sendDate;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
