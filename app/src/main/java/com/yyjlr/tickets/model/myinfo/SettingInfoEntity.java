package com.yyjlr.tickets.model.myinfo;

/**
 * Created by Elvira on 2017/6/19.
 */

public class SettingInfoEntity {
    /**
     * "modNo": 112,//菜单id
     * "modName": "我的积分",//菜单名称
     * "icon": "http://yyjlr-uat.oss-cn-shanghai.aliyuncs.com/menus/posterlist1497797510271.jpg",//图标
     */
    private int modNo;
    private String modName;
    private String icon;

    public SettingInfoEntity() {
    }

    public int getModNo() {
        return modNo;
    }

    public void setModNo(int modNo) {
        this.modNo = modNo;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
