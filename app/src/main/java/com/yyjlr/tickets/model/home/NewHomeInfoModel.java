package com.yyjlr.tickets.model.home;

import java.util.List;

/**
 * Created by Elvira on 2017/4/7.
 */

public class NewHomeInfoModel {

    private CinemaInfo cinemaInfo;
    private List<BannerInfo> bannerList;

    public NewHomeInfoModel() {
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

}
