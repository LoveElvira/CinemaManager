package com.yyjlr.tickets.model.film;

import java.util.List;

/**
 * Created by Richie on 2016/8/11.
 */
public class MovieBean {
    private int hasMore;//是否还有卖品信息：1：有；0：所有卖品信息已经全部加载完成
    private String pagable;//分页参数
    private List<MovieInfo> movieList;//电影信息列表

    public int getHasMore() {
        return hasMore;
    }

    public void setHasMore(int hasMore) {
        this.hasMore = hasMore;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
    }

    public List<MovieInfo> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<MovieInfo> movieList) {
        this.movieList = movieList;
    }
}
