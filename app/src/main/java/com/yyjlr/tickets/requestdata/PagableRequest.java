package com.yyjlr.tickets.requestdata;

import com.yyjlr.tickets.service.IRequestMainData;

/**
 * Created by Elvira on 2016/12/20.
 * 分页
 */

public class PagableRequest implements IRequestMainData {
    private String pagable;
    private String type;

    public PagableRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
    }
}
