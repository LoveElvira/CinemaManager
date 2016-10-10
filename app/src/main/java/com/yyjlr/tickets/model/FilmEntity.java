package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2016/7/31.
 * 影片实体类
 */
public class FilmEntity {
    public String FilmId;
    public String FilmImage;
    public String FilmName;
    public String FilmDirector;
    public String FilmStar;
    public String FilmShowTime;
    public FilmEntity() {}

    public FilmEntity(String filmId, String filmImage, String filmName, String filmDirector, String filmStar, String filmShowTime) {
        FilmId = filmId;
        FilmImage = filmImage;
        FilmName = filmName;
        FilmDirector = filmDirector;
        FilmStar = filmStar;
        FilmShowTime = filmShowTime;
    }

    public String getFilmId() {
        return FilmId;
    }

    public void setFilmId(String filmId) {
        FilmId = filmId;
    }

    public String getFilmImage() {
        return FilmImage;
    }

    public void setFilmImage(String filmImage) {
        FilmImage = filmImage;
    }

    public String getFilmName() {
        return FilmName;
    }

    public void setFilmName(String filmName) {
        FilmName = filmName;
    }

    public String getFilmDirector() {
        return FilmDirector;
    }

    public void setFilmDirector(String filmDirector) {
        FilmDirector = filmDirector;
    }

    public String getFilmStar() {
        return FilmStar;
    }

    public void setFilmStar(String filmStar) {
        FilmStar = filmStar;
    }

    public String getFilmShowTime() {
        return FilmShowTime;
    }

    public void setFilmShowTime(String filmShowTime) {
        FilmShowTime = filmShowTime;
    }
}
