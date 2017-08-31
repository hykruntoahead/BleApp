package com.example.heyukun.bleapp.ble.callback;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.exception.BleException;

/**
 * Created by heyukun on 2017/8/31.
 */

public interface ConnectCallBack {
    //正在连接的回调
    void onConnecting(BluetoothGatt gatt, int status);
    //连接未成功的回调，通过解析BleException来判断具体未成功的原因
    void onConnectError(BleException exception);
    //连接成功的回调
    void onConnectSuccess(BluetoothGatt gatt, int status);
    //发现服务的回调
    void onServicesDiscovered(BluetoothGatt gatt, int status);
    // 连接断开的回调，特指连接之后的断开
    void onDisConnected(BluetoothGatt gatt, int status, BleException exception);
}
