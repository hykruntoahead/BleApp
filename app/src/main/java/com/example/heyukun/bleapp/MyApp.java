package com.example.heyukun.bleapp;

import android.annotation.SuppressLint;
import android.app.Application;

import com.clj.fastble.BleManager;

/**
 * Created by heyukun on 2017/8/28.
 */

public class MyApp extends Application{
    @SuppressLint("StaticFieldLeak")
    private static BleManager sBleManager;
    @Override
    public void onCreate() {
        super.onCreate();
        sBleManager = new BleManager(this);
    }

    public static BleManager getBle(){
        return sBleManager;
    }

}
