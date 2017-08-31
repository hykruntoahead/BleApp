package com.example.heyukun.bleapp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.example.heyukun.bleapp.ble.BleCmdUtil;
import com.example.heyukun.bleapp.ble.MyBleManager;
import com.example.heyukun.bleapp.ble.callback.RevCallBack;
import com.example.heyukun.bleapp.ble.callback.SendCallBack;

import java.util.ArrayList;
import java.util.List;

import static com.example.heyukun.bleapp.ble.MyBleManager.REV_SUCCESS_FLAG;

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
    private RadioGroup mRadioGroup;

    private int readCount = 3;

    private Handler handler = new Handler();
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            notifyData();
//            handler.postDelayed(this,1000);
        }
    };
//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_data_road);
        initWidgets();
    }




    private void initWidgets() {
        mRecLv = (ListView) findViewById(R.id.lv_rev);
        mSendEt = (EditText) findViewById(R.id.et_send);
        mSendBtn = (Button) findViewById(R.id.btn_send);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg);
        mList = new ArrayList<>();
        mArrAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mList);
        mRecLv.setAdapter(mArrAdapter);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            writeHeightTo(Integer.parseInt(mSendEt.getText().toString()));
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_1:
                        writeCmdTo(BleCmdUtil.getReadHeightCmd());
                        break;
                    case R.id.rb_2:
                        writeCmdTo(BleCmdUtil.getAutoLearnCmd());
                        break;
                    case R.id.rb_3:
                        writeCmdTo(BleCmdUtil.getResetCmd());
                        break;
                    case R.id.rb_4:
                        writeCmdTo(BleCmdUtil.getUpCmd());
                        break;
                    case R.id.rb_5:
                        writeCmdTo(BleCmdUtil.getUpCancelCmd());
                        break;
                    case R.id.rb_6:
                        writeCmdTo(BleCmdUtil.getDownCmd());
                        break;
                    case R.id.rb_7:
                        writeCmdTo(BleCmdUtil.getDownCancelCmd());
                        break;
                    case R.id.rb_8:
                        writeCmdTo(BleCmdUtil.getStopCmd());
                        break;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void writeHeightTo(int height){
        mList.add("[Send-]"+height);
        mArrAdapter.notifyDataSetChanged();
        mRecLv.smoothScrollToPosition(mList.size()-1);
        MyBleManager.get().sendVariableToDevice(height, new SendCallBack() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                mList.add("[Send-Success]-Hex-"+HexUtil.encodeHexStr(characteristic.getValue()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mArrAdapter.notifyDataSetChanged();
                        notifyData();
                    }
                });
            }

            @Override
            public void onFailure(BleException exception) {
                mList.add("[Send-Failure]"+exception.getDescription());
                mArrAdapter.notifyDataSetChanged();
            }
        });
    }


    private void writeCmdTo(String cmdStr){
        mList.add("[Send-]"+cmdStr);
        mArrAdapter.notifyDataSetChanged();
        mRecLv.smoothScrollToPosition(mList.size()-1);
        MyBleManager.get().sendFixedToDevice(cmdStr, new SendCallBack() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                mList.add("[Send-Success]-Hex-"+HexUtil.encodeHexStr(characteristic.getValue()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mArrAdapter.notifyDataSetChanged();
                        notifyData();
                    }
                });
            }

            @Override
            public void onFailure(BleException exception) {
                mList.add("[Send-Failure]"+exception.getDescription());
                mArrAdapter.notifyDataSetChanged();
            }
        });
    }

    private void notifyData(){
        boolean ind = MyBleManager.get().revFromDevice(new RevCallBack() {
            @Override
            public void onCtlSuccess(int heightOrState) {
                if(heightOrState == REV_SUCCESS_FLAG){
                    mList.add("[Notify-Success]-OK!OK!");
                }else {
                    mList.add("[Notify-Success]-OK!-height"+heightOrState);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"操作成功",Toast.LENGTH_SHORT).show();
                        mArrAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onCtlFailure(String errorValue) {
                mList.add("[Notify-onCtlFailure]"+errorValue);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mArrAdapter.notifyDataSetChanged();
                    }
                });
            }


            @Override
            public void onFailure(BleException exception) {
                mList.add("[Notify-Failure]"+exception.getDescription());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mArrAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        Log.d("Ble-","notify-"+ind);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyBleManager.get().stopRev();
        MyBleManager.get().disconnectGatt();
    }
}
