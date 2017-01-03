package com.yyjlr.tickets.requestdata;

import com.yyjlr.tickets.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2016/12/29.
 * 锁定座位
 */

public class LockSeatRequest implements IRequestMainData {
    private String planId;
    private List<String> seatIds;

    public LockSeatRequest() {
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public List<String> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<String> seatIds) {
        this.seatIds = seatIds;
    }
}
