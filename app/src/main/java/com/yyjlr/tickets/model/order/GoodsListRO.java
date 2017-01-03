package com.yyjlr.tickets.model.order;

/**
 * Created by Richie on 2016/8/25.
 */
public class GoodsListRO {
    private Long id;//卖品id
    private int num;//卖品数量

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
