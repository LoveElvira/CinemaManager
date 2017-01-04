package com.yyjlr.tickets.model.order;

import java.io.Serializable;

/**
 * Created by Elvira on 2016/12/30.
 */

public class PaymentType implements Serializable {
    private int id;
    private String name;

    public PaymentType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
