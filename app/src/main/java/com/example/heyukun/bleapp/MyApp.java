package com.example.heyukun.bleapp;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * Created by heyukun on 2017/8/28.
 */

public class MyApp extends Application{
    @SuppressLint("StaticFieldLeak")
    private static MyApp mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MyApp getAppInstance(){
        return mInstance;
    }

}
