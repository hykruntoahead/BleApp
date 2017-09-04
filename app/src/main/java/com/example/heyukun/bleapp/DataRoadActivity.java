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
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.example.heyukun.bleapp.ble.BleCmdUtil;
import com.example.heyukun.bleapp.ble.MyBleManager;
import com.example.heyukun.bleapp.ble.callback.RevCallBack;
import com.example.heyukun.bleapp.ble.callback.SendCallBack;
import com.example.heyukun.bleapp.ble.e.WarnEntity;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

import static com.example.heyukun.bleapp.ble.MyBleManager.REV_SUCCESS_FLAG;

/**
 * Created by heyukun on 2017/8/28.
 */

public class DataRoadActivity extends FragmentActivity {

    private ListView mRecLv;
    private EditText mSendEt,mSetHeightEt;
    private Button mSendBtn;
    private TextView mGetHeightTv;
    private List<String> mList;
    private ArrayAdapter<String> mArrAdapter;
    private RadioGroup mRadioGroup;
    private MaterialDialog md;


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
        mSetHeightEt = (EditText) findViewById(R.id.et_set_height);
        mGetHeightTv = (TextView) findViewById(R.id.tv_set_height);
        mList = new ArrayList<>();
        mArrAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mList);
        mRecLv.setAdapter(mArrAdapter);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeCmdTo(mSendEt.getText().toString());
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

        findViewById(R.id.btn_set_height).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeHeightTo(Integer.parseInt(mSetHeightEt.getText().toString()));
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
            public void onCtlSuccess(final int heightOrState) {
                if(heightOrState == REV_SUCCESS_FLAG){
                    mList.add("[Notify-Success]-操作成功！");
                }else {
                    mList.add("[Notify-Success]-OK!-height="+heightOrState);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGetHeightTv.setText(heightOrState+"CM");
                        }
                    });
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
            public void onCtlWarning(final WarnEntity warn) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       md =  new MaterialDialog(DataRoadActivity.this)
                                .setTitle(warn.getName())
                                .setMessage("提示："+warn.getPrompt()+"\r\n\r\n解决："+warn.getSolution())
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        md.dismiss();
                                    }
                                });
                        md.show();
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
    protected void onStop() {
        super.onStop();
        if(md!=null){
             md.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyBleManager.get().stopRev();
        MyBleManager.get().disconnectGatt();
    }
}
