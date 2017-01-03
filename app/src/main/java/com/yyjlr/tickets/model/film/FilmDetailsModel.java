package com.yyjlr.tickets.model.film;

import com.yyjlr.tickets.model.chosen.ShareInfo;

import java.util.List;

/**
 * Created by Elvira on 2016/12/28.
 * 影片详情
 */

public class FilmDetailsModel {
    private int movieId;//影片ID
    private String movieName; //影片名称
    private String movieAlas; // 影片别名
    private String language; //语言
    private String movieBanner; //影片横版图片
    private String moviePortrait; //影片竖版图片
    private String movieDesc; //剧情介绍
    private int interest; //关注数
    private int score; // 评分，10分制
    private int isFavority;  // 是否收藏,1:是；0：否
    private ShareInfo share;//分享
    private List<String> tags;//标签 "动画/喜剧/动作","97分钟"
    private List<FilmWorker> worker;//工作人员

    public FilmDetailsModel() {
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMoviePortrait() {
        return moviePortrait;
    }

    public void setMoviePortrait(String moviePortrait) {
        this.moviePortrait = moviePortrait;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieAlas() {
        return movieAlas;
    }

    public void setMovieAlas(String movieAlas) {
        this.movieAlas = movieAlas;
    }

    public String getMovieBanner() {
        return movieBanner;
    }

    public void setMovieBanner(String movieBanner) {
        this.movieBanner = movieBanner;
    }

    public String getMovieDesc() {
        return movieDesc;
    }

    public void setMovieDesc(String movieDesc) {
        this.movieDesc = movieDesc;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIsFavority() {
        return isFavority;
    }

    public void setIsFavority(int isFavority) {
        this.isFavority = isFavority;
    }

    public ShareInfo getShare() {
        return share;
    }

    public void setShare(ShareInfo share) {
        this.share = share;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<FilmWorker> getWorker() {
        return worker;
    }

    public void setWorker(List<FilmWorker> worker) {
        this.worker = worker;
    }
}
