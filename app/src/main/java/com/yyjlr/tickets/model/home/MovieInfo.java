package com.yyjlr.tickets.model.home;

/**
 * Created by Elvira on 2017/4/7.
 */

public class MovieInfo {
    /**
     * "id":154,//影片id
     * "name":"钢铁侠",//影片名称
     * "imageUrl":"http://xxxxxxx.jpg",//电影图片地址
     * "score":8.5,//评分
     * //是否预售，0否，1是
     */
    private long id;
    private String name;
    private String imageUrl;
    private float score;
    private String presell;//是否预售，0否，1是

    public MovieInfo() {
    }

    public String getPresell() {
        return presell;
    }

    public void setPresell(String presell) {
        this.presell = presell;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
