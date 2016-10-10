package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2016/8/10.
 * 套餐
 */
public class FilmSaleEntity {
    private String saleId;
    private String saleImage;
    private String salePackage;
    private String salePackageContent;
    private String saleTime;
    private String salePrice;
    private String saleNum;//数量
    private boolean saleSelect;

    public FilmSaleEntity() {
    }

    public FilmSaleEntity(String saleId, String saleImage, String salePackage, String salePackageContent, String saleTime, String salePrice, String saleNum, boolean saleSelect) {
        this.saleId = saleId;
        this.saleImage = saleImage;
        this.salePackage = salePackage;
        this.salePackageContent = salePackageContent;
        this.saleTime = saleTime;
        this.salePrice = salePrice;
        this.saleNum = saleNum;
        this.saleSelect = saleSelect;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getSaleImage() {
        return saleImage;
    }

    public void setSaleImage(String saleImage) {
        this.saleImage = saleImage;
    }

    public String getSalePackage() {
        return salePackage;
    }

    public void setSalePackage(String salePackage) {
        this.salePackage = salePackage;
    }

    public String getSalePackageContent() {
        return salePackageContent;
    }

    public void setSalePackageContent(String salePackageContent) {
        this.salePackageContent = salePackageContent;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(String saleNum) {
        this.saleNum = saleNum;
    }

    public boolean isSaleSelect() {
        return saleSelect;
    }

    public void setSaleSelect(boolean saleSelect) {
        this.saleSelect = saleSelect;
    }
}
