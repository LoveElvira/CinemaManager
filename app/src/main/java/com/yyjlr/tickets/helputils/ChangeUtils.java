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

    /* 保留两位小数
    * @param val RMB（单位：分）。[例：3358]
    * @return RMB（单位：元）,并保留两位小数。[例：33.58]
    */
    public static String save2Decimal(long val) {
        Double decimal = Double.valueOf(val + "");
        return String.format("%.2f", decimal / 100);
    }

}
