package com.yyjlr.tickets.requestdata;

import com.yyjlr.tickets.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2017/4/28.
 * 删除消息列表
 */

public class DeleteRequest implements IRequestMainData {
    private List<Long> ids;

    public DeleteRequest() {
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
