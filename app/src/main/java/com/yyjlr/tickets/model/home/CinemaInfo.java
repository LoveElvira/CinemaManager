package com.yyjlr.tickets.model.home;

/**
 * Created by Elvira on 2017/4/7.
 */

public class CinemaInfo {
    /**
     * "id":1552,//影院id
     * "name": "影院名称"//影院名称
     */

    private long id;
    private String name;

    public CinemaInfo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
