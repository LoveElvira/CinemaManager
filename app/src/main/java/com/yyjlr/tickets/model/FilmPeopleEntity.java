package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2016/8/1.
 */
public class FilmPeopleEntity {
    private String name;
    private String position;

    public FilmPeopleEntity() {}

    public FilmPeopleEntity(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
