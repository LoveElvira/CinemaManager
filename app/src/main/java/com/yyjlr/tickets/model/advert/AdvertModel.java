package com.yyjlr.tickets.model.advert;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 * 广告闪屏
 */

public class AdvertModel {
//    autoSkip":0, // 是否自动跳过
//    "showTime": -1, // 自动跳过延迟时间（毫秒），只有当启用自动跳过时该参数才有效
//    "advertList":
    private int autoSkip; // 是否自动跳过
    private long showTime;// 自动跳过延迟时间（秒），只有当启用自动跳过时该参数才有效
    private List<Advert> advertList;//广告信息列表

    public AdvertModel() {
    }

    public int getAutoSkip() {
        return autoSkip;
    }

    public void setAutoSkip(int autoSkip) {
        this.autoSkip = autoSkip;
    }

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }

    public List<Advert> getAdvertList() {
        return advertList;
    }

    public void setAdvertList(List<Advert> advertList) {
        this.advertList = advertList;
    }
}
