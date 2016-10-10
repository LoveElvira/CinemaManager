package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2016/7/31.
 * 精选实体类
 */
public class ChosenFilmEntity {
    public String chosenFilmId;
    public String chosenFilmImage;
    public String chosenFilmName;
    public String chosenFilmShowTime;
    public String chosenFilmPrice;
    public String chosenFilmAddress;

    public String getChosenFilmAddress() {
        return chosenFilmAddress;
    }

    public void setChosenFilmAddress(String chosenFilmAddress) {
        this.chosenFilmAddress = chosenFilmAddress;
    }

    public ChosenFilmEntity() {}

    public ChosenFilmEntity(String chosenFilmId, String chosenFilmImage, String chosenFilmName, String chosenFilmShowTime, String chosenFilmPrice) {
        this.chosenFilmId = chosenFilmId;
        this.chosenFilmImage = chosenFilmImage;
        this.chosenFilmName = chosenFilmName;
        this.chosenFilmShowTime = chosenFilmShowTime;
        this.chosenFilmPrice = chosenFilmPrice;
    }

    public String getChosenFilmId() {
        return chosenFilmId;
    }

    public void setChosenFilmId(String chosenFilmId) {
        this.chosenFilmId = chosenFilmId;
    }

    public String getChosenFilmImage() {
        return chosenFilmImage;
    }

    public void setChosenFilmImage(String chosenFilmImage) {
        this.chosenFilmImage = chosenFilmImage;
    }

    public String getChosenFilmName() {
        return chosenFilmName;
    }

    public void setChosenFilmName(String chosenFilmName) {
        this.chosenFilmName = chosenFilmName;
    }

    public String getChosenFilmShowTime() {
        return chosenFilmShowTime;
    }

    public void setChosenFilmShowTime(String chosenFilmShowTime) {
        this.chosenFilmShowTime = chosenFilmShowTime;
    }

    public String getChosenFilmPrice() {
        return chosenFilmPrice;
    }

    public void setChosenFilmPrice(String chosenFilmPrice) {
        this.chosenFilmPrice = chosenFilmPrice;
    }
}
