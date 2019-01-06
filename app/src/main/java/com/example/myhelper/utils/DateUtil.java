package com.example.myhelper.utils;

import java.text.SimpleDateFormat;

/**
 * Created by lxj on 2016/8/30.
 */
public class DateUtil {
    public static String timestamp2ymd(long timestamp){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(timestamp);
    }

    public static String toymdhms(long timestamp){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

}
