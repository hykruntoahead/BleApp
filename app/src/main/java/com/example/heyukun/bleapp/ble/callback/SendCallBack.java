package com.example.heyukun.bleapp.ble.callback;

import android.bluetooth.BluetoothGattCharacteristic;

import com.clj.fastble.exception.BleException;

/**
 * Created by heyukun on 2017/8/31.
 */

public interface SendCallBack {
    void onSuccess(BluetoothGattCharacteristic characteristic);
    void onFailure(BleException exception);
}
