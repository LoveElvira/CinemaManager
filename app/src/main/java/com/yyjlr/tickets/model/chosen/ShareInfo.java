package com.yyjlr.tickets.model.chosen;

/**
 * Created by Elvira on 2016/12/20.
 */

public class ShareInfo {
    /**
     *  "url":"http://xxxx", // 分享地址
     "title":"分享标题", // 分享标题
     "memo":"分享描述" // 分享描述
     * */
    private String url;
    private String title;
    private String memo;

    public ShareInfo() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
