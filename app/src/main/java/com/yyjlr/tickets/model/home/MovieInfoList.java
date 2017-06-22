package com.yyjlr.tickets.model.home;

import java.util.List;

/**
 * Created by Elvira on 2017/4/28.
 */

public class MovieInfoList {
    private List<MovieInfo> hotMovieList;
    private List<MovieInfo> nextMovieList;

    public MovieInfoList() {
    }

    public List<MovieInfo> getHotMovieList() {
        return hotMovieList;
    }

    public void setHotMovieList(List<MovieInfo> hotMovieList) {
        this.hotMovieList = hotMovieList;
    }

    public List<MovieInfo> getNextMovieList() {
        return nextMovieList;
    }

    public void setNextMovieList(List<MovieInfo> nextMovieList) {
        this.nextMovieList = nextMovieList;
    }
}
