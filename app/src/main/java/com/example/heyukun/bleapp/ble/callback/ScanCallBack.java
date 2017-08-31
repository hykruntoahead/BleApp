package com.example.heyukun.bleapp.ble.callback;

import com.clj.fastble.data.ScanResult;

/**
 * Created by heyukun on 2017/8/31.
 */

public interface ScanCallBack {
    void  onScanning(ScanResult result);
    void  onScanComplete(ScanResult[] results);
}
