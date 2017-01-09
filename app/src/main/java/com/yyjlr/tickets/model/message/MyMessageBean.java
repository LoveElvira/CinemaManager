package com.yyjlr.tickets.model.message;

import java.util.List;

/**
 * Created by Richie on 2016/9/13.
 */
public class MyMessageBean {
    private int hasMore;//是否还有信息：1有，0所有信息加载完成
    private String pagable;//分页参数
    private List<MyMessageInfo> messages;//消息列表

    public int getHasMore() {
        return hasMore;
    }

    public void setHasMore(int hasMore) {
        this.hasMore = hasMore;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
    }

    public List<MyMessageInfo> getMessages() {
        return messages;
    }

    public void setMessages(List<MyMessageInfo> messages) {
        this.messages = messages;
    }
}
