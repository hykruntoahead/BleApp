package com.example.heyukun.bleapp.ble.callback;

import com.clj.fastble.exception.BleException;
import com.example.heyukun.bleapp.ble.e.WarnEntity;

/**
 * Created by heyukun on 2017/8/31.
 *
 */

public interface RevCallBack {
    //控制成功
    void onCtlSuccess(int heightOrState);
    //控制失败 返回设备发送过来的值
    void onCtlFailure(String value);
    //设备 报警
    void onCtlWarning(WarnEntity warn);
    //失败
    void onFailure(BleException exception);
}
