package com.yyjlr.tickets.model.cinemainfo;

/**
 * Created by Elvira on 2016/12/20.
 */

public class HallType {
    /**
     *  ”icon":"http://xxxxx", // 图标URL地址
     "memo":"营业时间" // 营业时间说明
     "actionIcon": "http://xxxxx", // 链接图片地址【有值时可以操作】,点击后跳转到actionUrl指定的地址
     "actionUrl":"yyjlr_1://tel" // 点击后跳转地址
     * */

    private String icon;
    private String memo;
    private String actionIcon;
    private String actionUrl;
    private String desc;

    public HallType() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getActionIcon() {
        return actionIcon;
    }

    public void setActionIcon(String actionIcon) {
        this.actionIcon = actionIcon;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
}
