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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhtq on 16/4/28.
 * 模拟数据
 */
public class MockupDataServiceImp implements IDataService {

    private List<Map<String,String>> mTimeList;

    @Override
    public List<ChosenFilmEntity> getChosenMovieList(int size) {
        List<ChosenFilmEntity> chosenFilmEntityList = new ArrayList<ChosenFilmEntity>();
        for (int i = 0; i < size; i++) {
            ChosenFilmEntity chosenFilmEntity = new ChosenFilmEntity();
            chosenFilmEntity.setChosenFilmId(i+"");
            chosenFilmEntity.setChosenFilmImage("http://www.99inf.com/space/201405/3465948/pic/pic_file_3465948_1415606672.jpg");
            chosenFilmEntity.setChosenFilmName("《独立日2》15元起零点首映超前预售");
            chosenFilmEntity.setChosenFilmShowTime("07月28日~08月02日");
            chosenFilmEntity.setChosenFilmPrice("免费");
            chosenFilmEntity.setChosenFilmAddress("SFC上影-世博店");
            chosenFilmEntityList.add(chosenFilmEntity);
        }
        return chosenFilmEntityList;
    }

    @Override
    public List<FilmEntity> getFilmList() {
        List<FilmEntity> filmEntityList = new ArrayList<FilmEntity>();
        for (int i = 0; i < 10; i++) {
            FilmEntity filmEntity = new FilmEntity();
            filmEntity.setFilmId(i+"");
            filmEntity.setFilmImage("http://www.99inf.com/space/201405/3465948/pic/pic_file_3465948_1415606672.jpg");
            filmEntity.setFilmName("小飞侠：幻梦起航");
            filmEntity.setFilmDirector("乔·莱特");
            filmEntity.setFilmStar("乔·莱特 维·米勒 加内特·赫…");
            filmEntity.setFilmShowTime("2016-07-31");
            filmEntityList.add(filmEntity);
        }
        return filmEntityList;
    }

    @Override
    public List<SaleEntity> getSaleList() {
        List<SaleEntity> saleEntityList = new ArrayList<SaleEntity>();
        for (int i = 0; i < 10; i++) {
            SaleEntity saleEntity = new SaleEntity();
            saleEntity.setSaleId(i+"");
            saleEntity.setSaleImage("http://www.99inf.com/space/201405/3465948/pic/pic_file_3465948_1415606672.jpg");
            saleEntity.setSalePackage("套餐一");
            saleEntity.setSalePackageContent("中份爆米花1份+可乐2杯");
            saleEntity.setOriginalPrice("36");
            saleEntity.setAppPrice("26");
            saleEntity.setVipPrice("22");
            saleEntity.setSaleTime("2016-07-31~2016-08-31");
            saleEntityList.add(saleEntity);
        }
        return saleEntityList;
    }

    @Override
    public List<FilmPeopleEntity> getFilmPeopleList() {
        List<FilmPeopleEntity> filmPeopleEntityList = new ArrayList<FilmPeopleEntity>();
        for (int i = 0; i < 7; i++) {
            FilmPeopleEntity filmPeopleEntity = new FilmPeopleEntity();
            filmPeopleEntity.setName("费格尔·雷利");
            filmPeopleEntity.setPosition("导演");
            filmPeopleEntityList.add(filmPeopleEntity);
        }
        return filmPeopleEntityList;
    }

    @Override
    public List<FilmTimeEntity> getTimeList() {
        List<FilmTimeEntity> filmTimeEntityList = new ArrayList<FilmTimeEntity>();
        for (int i = 0; i < 7; i++) {
            FilmTimeEntity filmTimeEntity = new FilmTimeEntity();
            filmTimeEntity.setTime("");
            filmTimeEntity.setWeek("");
            filmTimeEntityList.add(filmTimeEntity);
        }
        return filmTimeEntityList;
    }

    @Override
    public List<FilmSeasonEntity> getSeasonList() {
        List<FilmSeasonEntity> filmSeasonEntityList = new ArrayList<FilmSeasonEntity>();
        for (int i = 0; i < 7; i++) {
            FilmSeasonEntity filmSeasonEntity = new FilmSeasonEntity();
            filmSeasonEntity.setSunOrMoon("");
            filmSeasonEntity.setStartTime("09:00");
            filmSeasonEntity.setEndTime("11:15散场");
            filmSeasonEntity.setLanguage("英语");
            filmSeasonEntity.setFilmType("3D");
            filmSeasonEntity.setOriginalPrice("¥120");
            filmSeasonEntity.setAppPrice("¥50");
            filmSeasonEntity.setVipPrice("¥50");
            filmSeasonEntity.setHall("6号厅");
            filmSeasonEntity.setHallType("巨幕厅");
            filmSeasonEntityList.add(filmSeasonEntity);
        }
        return filmSeasonEntityList;
    }

    @Override
    public List<Map<String, String>> getMapList() {

        mTimeList = new ArrayList<Map<String, String>>();
        Calendar calendar = Calendar.getInstance();
        int currtyear = calendar.get(Calendar.YEAR);
        int currtmonth = calendar.get(Calendar.MONTH)+1;
        int currtday = calendar.get(Calendar.DAY_OF_MONTH);
        int currtweek = calendar.get(Calendar.DAY_OF_WEEK);
        initDate(currtmonth,currtday,currtweek,currtyear,1);
        return mTimeList;
    }

    @Override
    public List<MessageEntity> getMessageList() {
        List<MessageEntity> messageEntityList = new ArrayList<MessageEntity>();
        for (int i = 0; i < 20; i++) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTime("2016-08-08");
            if (i%2==0){
                messageEntity.setTitle("最新电影上映 赶紧进来看看吧");
            }else {
                messageEntity.setTitle("《夏有乔木雅望天堂》电影票免费送!只需关注@SFC世博店 并转发该活动+@ 上你的三位好友,就有机会获得大地影院送出的《夏有乔木雅望天堂》电影票一张,共25名。快快来参与吧!票已出好[抱抱][抱抱],你来,我送票! [心][心][心]");
            }
            messageEntity.setMessage("《夏有乔木雅望天堂》电影票免费送!只需关注@SFC世博店 并转发该活动+@ 上你的三位好友,就有机会获得大地影院送出的《夏有乔木雅望天堂》电影票一张,共25名。快快来参与吧!票已出好[抱抱][抱抱],你来,我送票! [心][心][心] ");
            messageEntityList.add(messageEntity);
        }
        return messageEntityList;
    }

    @Override
    public List<FilmSaleEntity> getFileSaleList() {
        List<FilmSaleEntity> filmSaleEntityList = new ArrayList<FilmSaleEntity>();
        for (int i = 0; i < 6; i++) {
            FilmSaleEntity filmSaleEntity = new FilmSaleEntity();
            filmSaleEntity.setSaleId(i+"");
            filmSaleEntity.setSaleImage("http://www.99inf.com/space/201405/3465948/pic/pic_file_3465948_1415606672.jpg");
            filmSaleEntity.setSalePackage("套餐"+(i+1));
            filmSaleEntity.setSalePackageContent("中份爆米花1份+可乐2杯");
            filmSaleEntity.setSaleSelect(false);
            filmSaleEntity.setSaleNum("0");
            filmSaleEntity.setSalePrice("20");
            filmSaleEntity.setSaleTime("2016-07-31~2016-08-31");
            filmSaleEntityList.add(filmSaleEntity);
        }
        return filmSaleEntityList;
    }

    @Override
    public List<OrderEntity> getOrderList() {
        List<OrderEntity> orderEntityList = new ArrayList<OrderEntity>();
        for (int i = 0; i < 10; i++) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderId(i+"");
            orderEntity.setOrderFilmName("小飞侠：幻梦起航");
            orderEntity.setOrderPackage("卖品"+(i+1)+"套餐");
            orderEntity.setOrderNum("2565263322");
            if (i<6) {
                orderEntity.setOrderComplete("2");
            }else {
                orderEntity.setOrderComplete("1");
            }
            orderEntityList.add(orderEntity);
        }
        return orderEntityList;
    }

    @Override
    public List<PointsEntity> getPointsList() {
        List<PointsEntity> pointsEntityList = new ArrayList<PointsEntity>();
        for (int i = 0; i < 5; i++) {
            PointsEntity pointsEntity = new PointsEntity();
            pointsEntity.setPointsId(i+"");
            if (i%2==0) {
                pointsEntity.setPointsFlag("1");
                pointsEntity.setPointsNum("400积分");
                pointsEntity.setPointsContent("400积分 换购x月x日，盗墓笔记SFC永华店 x场，x排x座");
                pointsEntity.setPointsTime("2016.09.20");
            }else {
                pointsEntity.setPointsFlag("2");
                pointsEntity.setPointsNum("50积分");
                pointsEntity.setPointsContent("50积分 参加XXX活动，获得积分");
                pointsEntity.setPointsTime("2016.09.18");
            }
            pointsEntityList.add(pointsEntity);
        }
        return pointsEntityList;
    }

    /**
     * 给日期赋值
     * */
    private void initDate(int month,int day,int week,int year,int num){

        Map<String, String> map = new HashMap<String, String>();
        if ((year%4 == 0 && year%100 != 0) || year%400 == 0)//是闰年
        {
            if (month==2)
            {
                if (day>29)
                {
                    month=3;
                    day = 1;
                }
            }
        }else {
            if (month==2)
            {
                if (day>28)
                {
                    month=3;
                    day = 1;
                }
            }
        }

        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:// 1 3 5 7 8 10 12 均为31天
                if (day > 31)
                {
                    if (month != 12)
                    {
                        month += 1;
                    }else {
                        month = 1;
                    }
                    day = 1;
                }
                break;
            default://其他的为30天
                if (day > 30)
                {
                    month += 1;
                    day = 1;
                }
                break;
        }
        map.put("time", (month<10?"0"+month:month)+"月"+(day<10?"0"+day:day)+"日");

        if (num==1)
        {
            if (week==8)
            {
                week=1;
            }
            map.put("week","今天");
            map.put("show","0");
        }else if(num==2){
            if (week==8)
            {
                week=1;
            }
            map.put("week","明天");
            map.put("show","1");
        }else {
            map.put("show","1");
            switch (week)
            {
                case 1:
                    map.put("week","星期天");
                    break;
                case 2:
                    map.put("week","星期一");
                    break;
                case 3:
                    map.put("week","星期二");
                    break;
                case 4:
                    map.put("week","星期三");
                    break;
                case 5:
                    map.put("week","星期四");
                    break;
                case 6:
                    map.put("week","星期五");
                    break;
                case 7:
                    map.put("week","星期六");
                    break;

                default:
                    if (week==8)
                    {
                        week=1;
                        map.put("week","星期天");
                    }
                    break;
            }
        }

        num++;
        if (mTimeList.size()<7)
        {
            mTimeList.add(map);
        }else {
            return;
        }
        initDate(month,day+1,week+1,year,num);
    }


}
