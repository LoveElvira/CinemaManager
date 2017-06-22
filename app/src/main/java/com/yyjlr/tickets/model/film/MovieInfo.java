package com.yyjlr.tickets.model.film;

/**
 * Created by Richie on 2016/8/11.
 */
public class MovieInfo {
    private long movieId;//电影id
    private String movieName;//电影名称
    private String movieImage;//电影海报
    private String director;//导演
    private String actors;//主演
    private String showTime;//上影时间
    private String presell;  //是否预售，0否，1是

    public String getPresell() {
        return presell;
    }

    public void setPresell(String presell) {
        this.presell = presell;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
}
