package com.yyjlr.tickets.model.sale;

/**
 * Created by Elvira on 2017/2/14.
 */

public class PackageDetails {
    /**
     *  "id": 300674, //卖品ID
     "name": "套餐名称",
     "detail": "套餐详情",
     "desc": "套餐说明",
     "image": "http://xxxxxxxxxxxxxxx", //套餐图片
     "costPrice": 5600, //原价，单位分
     "price": 5000 //套餐价，单位分
     * */

    private long id;//卖品ID
    private String name;//套餐名称
    private String detail;//套餐详情
    private String desc;//套餐说明
    private String image;//套餐图片
    private long costPrice;//原价，单位分
    private long price;//套餐价，单位分

    public PackageDetails() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(long costPrice) {
        this.costPrice = costPrice;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
