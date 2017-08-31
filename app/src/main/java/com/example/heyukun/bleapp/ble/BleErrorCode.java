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
}
