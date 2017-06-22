package com.yyjlr.tickets.model.message;

import java.io.Serializable;

/**
 * Created by Richie on 2016/9/13.
 */
public class MyMessageInfo implements Serializable {
    /**
     * "messageId":1, // 消息ID
     * "title":"标题", // 消息标题
     * "sendDate":166676767667, // 消息发送时间
     * "isRead":"1" // 1代表已读，0代表未读
     */
    private Long messageId;//消息id
    private String title;//消息标题
    private Long sendDate;//消息发送时间
    private String isRead;//是否已读，1已读，0未读
    private int isSelect = 0;//是否被选择  0 未选择 1 已选择
    private boolean isDelete = false;//是否显示选择按钮

    public int getIsSelect() {
        return isSelect;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

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
