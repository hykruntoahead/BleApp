package com.example.heyukun.bleapp.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.ListScanCallback;
import com.clj.fastble.utils.HexUtil;
import com.example.heyukun.bleapp.MyApp;
import com.example.heyukun.bleapp.ble.callback.ConnectCallBack;
import com.example.heyukun.bleapp.ble.callback.RevCallBack;
import com.example.heyukun.bleapp.ble.callback.ScanCallBack;
import com.example.heyukun.bleapp.ble.callback.SendCallBack;
import com.example.heyukun.bleapp.ble.e.WarnEntity;

import java.util.Arrays;

/**
 * Created by heyukun on 2017/8/31.
 */

public class MyBleManager {
    private static final String TAG = "MyBleManager";
    public static final int REV_SUCCESS_FLAG = -200;


    private static final String UUID_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final String UUID_CHARA = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private static final int SCAN_TIMEOUT = 3000; // 扫描时长
    private int CONNECT_COUNT = 3; // 连接重试次数

    public static MyBleManager sInstance;
    private BleManager bleManager;

    public static MyBleManager get() {
        if (sInstance == null) {
            sInstance = new MyBleManager();
        }
        return sInstance;
    }

    MyBleManager() {
        bleManager = new BleManager(MyApp.getAppInstance().getApplicationContext());
    }


    /**
     * 打开蓝牙
     */
    public void openBle() {
        bleManager.enableBluetooth();
    }


    /**
     * 关闭蓝牙
     */
    public void closeBle() {
        bleManager.disableBluetooth();
    }

    /**
     * 搜索
     */
    public void scanDevice(final ScanCallBack scanCallBack) {
        bleManager.scanDevice(new ListScanCallback(SCAN_TIMEOUT) {
            @Override
            public void onScanning(ScanResult result) {
                scanCallBack.onScanning(result);
            }

            @Override
            public void onScanComplete(ScanResult[] results) {
                scanCallBack.onScanComplete(results);
            }
        });
    }

    /**
     * 停止搜索
     */

    public void stopScanBle() {
        if (bleManager.isInScanning()) {
            bleManager.cancelScan();
        }
    }


    /**
     * 连接
     *
     * @param scanResult      扫描后可获得 指定的scanResult
     * @param connectCallBack 连接状态回调
     */

    public void connectBle(ScanResult scanResult, final ConnectCallBack connectCallBack) {
        stopScanBle();
        bleManager.connectDevice(scanResult, true, new BleGattCallback() {

            @Override
            public void onConnecting(BluetoothGatt gatt, int status) {
                connectCallBack.onConnecting(gatt, status);
            }

            @Override
            public void onConnectError(BleException exception) {
                connectCallBack.onConnectError(exception);
            }

            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                connectCallBack.onConnectSuccess(gatt, status);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                connectCallBack.onServicesDiscovered(gatt, status);
            }

            @Override
            public void onDisConnected(BluetoothGatt gatt, int status, BleException exception) {
                CONNECT_COUNT--;
                if (CONNECT_COUNT > 0) {
                    gatt.connect();
                }
                connectCallBack.onDisConnected(gatt, status, exception);
            }

        });
    }


    /**
     * 断开连接
     */
    public void disconnectGatt() {
        bleManager.closeBluetoothGatt();
    }


    /**
     * bel write fix 发送固定指令
     *
     * @param cmdStr       固定的指令，目前有以下几个：
     *                      <p>
     *                      1. 获取设备高度的发送指令 直接调用 BleCmdUtil.getReadHeightCmd()
     *                      2. 行程校准--自学习  BleCmdUtil.getAutoLearnCmd()
     *                      3. 行程校准--复位 BleCmdUtil.getResetCmd()
     *                      4. 运动控制--停止  BleCmdUtil.getStopCmd()
     *                      5. 运动控制--上按键按下 BleCmdUtil.getUpCmd()
     *                      6. 运动控制--上按键弹起 BleCmdUtil.getUpCancelCmd()
     *                      7. 运动控制--下按键按下 BleCmdUtil.getDownCmd()
     *                      8. 运动控制--下按键弹起 BleCmdUtil.getDownCancelCmd()
     * @param writeCallBack 回调接口
     */

    public void sendFixedToDevice(String cmdStr, final SendCallBack writeCallBack) {
        bleManager.writeDevice(UUID_SERVICE, UUID_CHARA, HexUtils.HexToByteArr(cmdStr), new BleCharacterCallback() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                writeCallBack.onSuccess(characteristic);
            }

            @Override
            public void onFailure(BleException exception) {
                writeCallBack.onFailure(exception);
            }

            @Override
            public void onInitiatedResult(boolean result) {

            }
        });
    }


    /**
     * bel write height 发送指定高度（可变）指令
     *
     * @param height
     * @param charaCallBack
     */

    public void sendVariableToDevice(int height, final SendCallBack charaCallBack) {
        bleManager.writeDevice(UUID_SERVICE, UUID_CHARA, HexUtils.HexToByteArr(BleCmdUtil.getSetHeightCmd(height)), new BleCharacterCallback() {
            @Override
            public void onFailure(BleException exception) {
                charaCallBack.onFailure(exception);
            }

            @Override
            public void onInitiatedResult(boolean result) {

            }

            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                charaCallBack.onSuccess(characteristic);
            }
        });
    }

    /**
     * bel rev
     */
    public boolean revFromDevice(final RevCallBack callBack) {
        return bleManager.notify(UUID_SERVICE, UUID_CHARA, new BleCharacterCallback() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                String retCmd = HexUtil.encodeHexStr(characteristic.getValue());
                Log.d("Ble-", "-------[Notify-onSuccess]:cmd-" + retCmd);

                if (retCmd == null) {
                    callBack.onCtlFailure(null);
                }

                if (retCmd.startsWith("1b") && retCmd.endsWith("05")) {
                    if (retCmd.startsWith("1b41")) {
                        if (retCmd.contains("1b41343105")) {
                            callBack.onCtlSuccess(REV_SUCCESS_FLAG);
                        } else if(retCmd.length() == 18){
                            callBack.onCtlSuccess(BleCmdUtil.getReturnHeight(retCmd));
                        }else if(retCmd.length() == 26){

                            String hex =retCmd.substring(4,retCmd.length() - 6);
//                            String hex = "3030343830303030";

                            String errorData = HexUtils.hexToAscii(hex);

                            if(errorData.equals("00000000")){
                                callBack.onCtlSuccess(REV_SUCCESS_FLAG);
                            }else {
                                byte[] bytes = HexUtil.hexStringToBytes(errorData);
                                for(int i=0;i<bytes.length;i++){
                                    Log.d("Ble-", "-------[Notify-onSuccess]:byte["+i+"]="+HexUtils.getBit(bytes[i]));
                                }
                                WarnEntity warn = new WarnEntity(getWarnCode(bytes));
                                callBack.onCtlWarning(warn);
                            }
                        }else {
                            callBack.onCtlFailure(retCmd);
                        }
                    } else {
                        callBack.onCtlFailure(retCmd);
                    }
                }
            }

            @Override
            public void onFailure(BleException exception) {
                callBack.onFailure(exception);
            }

            @Override
            public void onInitiatedResult(boolean result) {

            }
        });
    }



    private int getWarnCode(byte[] bys){
        if(HexUtils.getBitOnIndexIsTrue(bys[3],0)){
            return BleErrorCode.WARN_POWER_OVER_VOLTAGE;
        }else if(HexUtils.getBitOnIndexIsTrue(bys[3],1)){
            return BleErrorCode.WARN_POWER_UNDER_VOLTAGE;
        }else if(HexUtils.getBitOnIndexIsTrue(bys[3],6)){
            return BleErrorCode.WARN_OVER_LOAD;
        }else if(HexUtils.getBitOnIndexIsTrue(bys[2],3)){
            return BleErrorCode.WARN_SUPER_HEAT;
        }else if(HexUtils.getBitOnIndexIsTrue(bys[1],0)){
            return BleErrorCode.WARN_COLLISION;
        }else if(HexUtils.getBitOnIndexIsTrue(bys[1],1)){
            return BleErrorCode.WARN_STEP_OUT;
        }else if(HexUtils.getBitOnIndexIsTrue(bys[1],6)){
            return BleErrorCode.WARN_NO_HOLZER;
        }
        return BleErrorCode.WARN_UN_KNOW;
    }

    /**
     * 停止接收
     */
    public void stopRev(){
        bleManager.stopNotify(UUID_SERVICE,UUID_CHARA);
    }


}
