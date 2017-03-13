package com.yyjlr.tickets.model.message;

import java.io.Serializable;

/**
 * Created by Richie on 2016/9/13.
 */
public class MyMessageDetails implements Serializable {
    /**
     * "title":"标题", // 消息标题
     * "content":"内容",  // 消息内容
     * "sendDate":166676767667, // 消息发送时间
     */
    private String title;//消息标题
    private Long sendDate;//消息发送时间
    private String content;// 消息内容

    public MyMessageDetails() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
