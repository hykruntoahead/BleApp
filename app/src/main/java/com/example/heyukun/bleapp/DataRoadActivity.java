package com.example.heyukun.bleapp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyukun on 2017/8/28.
 */

public class DataRoadActivity extends FragmentActivity {
    private static final String UUID_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";

//    private static final String UUID_NOTIFY_SERVICE = "000001801-0000-1000-8000-00805f9b34fb";

    private static final String UUID_INDICATE = "0000000-0000-0000-8000-00805f9b0000";
    private static final String UUID_NOTIFY = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private static final String UUID_READ = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private static final String UUID_WRITE = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private static final String SAMPLE_WRITE_DATA = "1B5352303130303031433705";                  // 要写入设备某一个character的指令

    private ListView mRecLv;
    private EditText mSendEt;
    private Button mSendBtn;
    private List<String> mList;
    private ArrayAdapter<String> mArrAdapter;
    private BleManager mBleManager;
    private int readCount = 3;

    private Handler handler = new Handler();
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            notifyData();
//            readData();
            handler.postDelayed(this,1000);
        }
    };
//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_data_road);
        initWidgets();
        mBleManager = MyApp.getBle();
        handler.postDelayed(mRun,1000);
    }



    private void initWidgets() {
        mRecLv = (ListView) findViewById(R.id.lv_rev);
        mSendBtn = (Button) findViewById(R.id.btn_send);
        mSendEt = (EditText) findViewById(R.id.et_send);
        mList = new ArrayList<>();
        mArrAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mList);
        mRecLv.setAdapter(mArrAdapter);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            writeToDevice(mSendEt.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void writeToDevice(String sendStr){
        mList.add("[Send-]"+sendStr);
        mArrAdapter.notifyDataSetChanged();
        mRecLv.smoothScrollToPosition(mList.size()-1);
        handler.removeCallbacks(mRun);
        mBleManager.writeDevice(
                UUID_SERVICE,
                UUID_WRITE,
                HexUtils.HexToByteArr(sendStr),
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
                        mList.add("[Send-Success]-Hex-"+HexUtil.encodeHexStr(characteristic.getValue()));
//                         mList.add("[Send-Success]-Ascii-"+HexUtils.bytetoString(characteristic.getValue()));
                           mArrAdapter.notifyDataSetChanged();
                        handler.postDelayed(mRun,100);
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        mList.add("[Send-Failure]"+exception.getDescription());
                        mArrAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onInitiatedResult(boolean result) {

                    }
                });
    }

    private void notifyData(){
        boolean ind = mBleManager.notify(
                UUID_SERVICE,
                UUID_NOTIFY,
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
                        Log.d("Ble-", "-------[Notify-onSuccess]-" + characteristic.getStringValue(0));
                        mList.add("[Notify-Success]"+characteristic.getValue());
                        mArrAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        Log.d("Ble-", "-------[Notify-onFailure]-" + exception.getDescription());
                        mList.add("[Notify-Failure]"+exception.getDescription());
                        mArrAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onInitiatedResult(boolean result) {
                        Log.d("Ble-", "-------[Notify-onInitiatedResult]-" + result);
                    }
                });
        Log.d("Ble-", "-------[Indicate]-" + ind);
    }

    private void readData(){
       boolean read = mBleManager.readDevice(
                UUID_SERVICE,
                UUID_READ,
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
                        mList.add("[Read-Success]-Hex:"+HexUtil.encodeHexStr(characteristic.getValue()));
                        mList.add("[Read-Success]-Hex-Size:"+characteristic.getValue().length);
                        mArrAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        mList.add("[Read-Failure]"+exception.getDescription());
                        mArrAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onInitiatedResult(boolean result) {
                    }
                });
            Log.d("Ble-", "-------[Indicate]-" + read);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mBleManager.stopNotify(UUID_SERVICE,UUID_NOTIFY);
        mBleManager.closeBluetoothGatt();
    }
}
