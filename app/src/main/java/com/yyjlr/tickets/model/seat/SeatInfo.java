package com.yyjlr.tickets.model.seat;

import java.util.List;

/**
 * Created by Richie on 2016/8/18.
 * a
 */
public class SeatInfo {
    private String id;
    private String row;//行号
    private String col;//列号
    private int gRow;// 行坐标
    private int gCol;// 列坐标
    private String type;//座椅类别，0：普通座位,1：情侣座首座，2：情侣次座,3：特殊人群,4：VIP座位
    private String flag;// 座位情况，0：可点击，1不可点击
    private int price;//价格
    private List<String> relevance;//情侣座关联座位

    public List<String> getRelevance() {
        return relevance;
    }

    public void setRelevance(List<String> relevance) {
        this.relevance = relevance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public int getgRow() {
        return gRow;
    }

    public void setgRow(int gRow) {
        this.gRow = gRow;
    }

    public int getgCol() {
        return gCol;
    }

    public void setgCol(int gCol) {
        this.gCol = gCol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
