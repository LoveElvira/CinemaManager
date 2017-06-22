package com.yyjlr.tickets.helputils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Elvira on 2016/12/20.
 */

public class ChangeUtils {
    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（2016-6-16 17:25:59"）
     */
    public static String changeTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（2016.6.16 17:25"）
     */
    public static String changeTimeDot(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return format.format(new Date(time));
    }

    public static String changeYearDot(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(new Date(time));
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（6-16"）
     */
    public static String changeTimeDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        return format.format(new Date(time));
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（6-16"）
     */
    public static String changeTimeTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（6-16"）
     */
    public static String changeTimeYear(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }

    public static String changeTimeYearLine(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date(time));
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（2016-6-16 17:25:59"）
     */
    public static String changeTimeFormat(long t) {
        StringBuffer sb = new StringBuffer();
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            if (day <= 2) {
                sb.append(day + "天");
            } else {
                SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
                sb.append(format.format(new Date(t)));
            }
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (sb.toString().contains("月")) {
        } else if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }


    /* 保留两位小数
    * @param val RMB（单位：分）。[例：3358]
    * @return RMB（单位：元）,并保留两位小数。[例：33.58]
    */
    public static String save2Decimal(long val) {
        Double decimal = Double.valueOf(val + "");
        return String.format("%.2f", decimal / 100);
    }

    public static String saveDecimal(long val) {
        Double decimal = Double.valueOf(val + "");
        return String.format("%.0f", decimal / 100);
    }

}
