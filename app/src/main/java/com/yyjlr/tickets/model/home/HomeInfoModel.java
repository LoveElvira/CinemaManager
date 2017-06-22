package com.yyjlr.tickets.model.home;

import java.util.List;

/**
 * Created by Elvira on 2017/4/7.
 */

public class HomeInfoModel {

    private CinemaInfo cinemaInfo;
    private List<BannerInfo> bannerList;
    private MovieInfoList movieInfo;
    private List<GoodsInfo> goodsList;

    public HomeInfoModel() {
    }

    public CinemaInfo getCinemaInfo() {
        return cinemaInfo;
    }

    public void setCinemaInfo(CinemaInfo cinemaInfo) {
        this.cinemaInfo = cinemaInfo;
    }

    public List<BannerInfo> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerInfo> bannerList) {
        this.bannerList = bannerList;
    }

    public MovieInfoList getMovieInfo() {
        return movieInfo;
    }

    public void setMovieInfo(MovieInfoList movieInfo) {
        this.movieInfo = movieInfo;
    }

    public List<GoodsInfo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsInfo> goodsList) {
        this.goodsList = goodsList;
    }
}
