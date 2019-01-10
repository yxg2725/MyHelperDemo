package com.example.myhelper.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2019/1/10.
 */

public class OrderNoCreateFactory {

    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
        hashCodeV = - hashCodeV;
        }
        //         0 代表前面补充0     
        //         4 代表长度为4     
        //         d 代表参数为正数型
        return machineId+ String.format("%015d", hashCodeV);
    }

    public static String getOrderIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
            String newDate=sdf.format(new Date());
            String result="";
            Random random=new Random();
            for(int i=0;i<3;i++){
                result+=random.nextInt(10);
            }
            return newDate+result;
        }
    }
