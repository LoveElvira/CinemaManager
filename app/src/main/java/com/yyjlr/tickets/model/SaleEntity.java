package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2016/7/31.
 * 卖品实体
 */
public class SaleEntity {
    public String saleId;
    public String saleImage;
    public String saleName;
    public String salePackage;//套餐
    public String salePackageContent;//套餐内容
    public String originalPrice;//原价
    public String appPrice;//app价格
    public String vipPrice;//会员价格
    public String saleTime;//时间

    public SaleEntity() {}

    public SaleEntity(String saleId, String saleImage, String saleName, String salePackage, String salePackageContent, String originalPrice, String appPrice, String vipPrice, String saleTime) {
        this.saleId = saleId;
        this.saleImage = saleImage;
        this.saleName = saleName;
        this.salePackage = salePackage;
        this.salePackageContent = salePackageContent;
        this.originalPrice = originalPrice;
        this.appPrice = appPrice;
        this.vipPrice = vipPrice;
        this.saleTime = saleTime;
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

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
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

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(String appPrice) {
        this.appPrice = appPrice;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }
}
