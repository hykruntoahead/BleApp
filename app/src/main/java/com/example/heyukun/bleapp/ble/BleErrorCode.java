package com.example.heyukun.bleapp.ble;

/**
 * Created by heyukun on 2017/8/31.
 * 100： 超时
 * 101： 连接异常
 * 102： 其他（异常信息可以通过异常描述获取，一般是开发过程中的操作中间步骤的异常）
 * 103： 设备未找到
 * 104： 蓝牙未启用
 * 105： 开启扫描过程失败
 */

public class BleErrorCode {
    public static final int TIME_OVER= 100;
    public static final int CONNECT_ERROR = 101;
    public static final int OTHER = 102;
    public static final int NO_FOUND = 103;
    public static final int BLE_DISABLE = 104;
    public static final int SCANING_FAIL = 105;


    public static final int WARN_POWER_OVER_VOLTAGE = 1000;//输入电源过压
    public static final int WARN_POWER_UNDER_VOLTAGE = 1001;//输入电源欠压
    public static final int WARN_OVER_LOAD = 1002;//过载报警
    public static final int WARN_SUPER_HEAT = 1003;//过热报警
    public static final int WARN_COLLISION = 1004;//碰撞报警
    public static final int WARN_STEP_OUT = 1005;//失步报警

    public static final int WARN_NO_HOLZER= 1006;//无霍尔报警

    public static final int WARN_UN_KNOW= 1111;

}
