package com.example.myhelper;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2019/1/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
