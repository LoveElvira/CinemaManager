package com.yyjlr.tickets.service;

import com.yyjlr.tickets.model.ChosenFilmEntity;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.model.FilmPeopleEntity;
import com.yyjlr.tickets.model.FilmSaleEntity;
import com.yyjlr.tickets.model.FilmSeasonEntity;
import com.yyjlr.tickets.model.FilmTimeEntity;
import com.yyjlr.tickets.model.MessageEntity;
import com.yyjlr.tickets.model.OrderEntity;
import com.yyjlr.tickets.model.PointsEntity;
import com.yyjlr.tickets.model.SaleEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by PuTi(编程即菩提) on 4/28/16.
 */
public interface IDataService {
    //精选
    public List<ChosenFilmEntity> getChosenMovieList(int size);
    //影片
    public List<FilmEntity> getFilmList();
    //卖品
    public List<SaleEntity> getSaleList();
    //卖品
    public List<FilmPeopleEntity> getFilmPeopleList();
    //时间
    public List<FilmTimeEntity> getTimeList();
    //场次
    public List<FilmSeasonEntity> getSeasonList();
    //时间
    public List<Map<String,String>> getMapList();
    //消息
    public List<MessageEntity> getMessageList();
    //卖品 套餐
    public List<FilmSaleEntity> getFileSaleList();
    //我的订单
    public List<OrderEntity> getOrderList();
    //我的积分
    public List<PointsEntity> getPointsList();
}
