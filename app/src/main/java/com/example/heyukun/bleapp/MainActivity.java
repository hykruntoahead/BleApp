package com.example.heyukun.bleapp;

import android.Manifest;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.exception.BleException;
import com.example.heyukun.bleapp.ble.MyBleManager;
import com.example.heyukun.bleapp.ble.callback.ConnectCallBack;
import com.example.heyukun.bleapp.ble.callback.ScanCallBack;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Ble-MainActivity";
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private ListView mListView;
    private List<ScanResult> mScanresults;
    private BleManager bleManager;
    private ScanAdapter mScanAdapter;
    MaterialDialog mMaterialDialog;
    int connectCount = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        openScanDevice();
        //android 6.0 + 需要打开GPS定位才可进行蓝牙搜索及后续操作
        initLoc();
        if(!isLocationEnable(this)){
            setLocationService();
        }
    }
    private void setLocationService() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
    }
    private void initLoc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(getApplicationContext(),"自Android 6.0开始需要打开位置权限才可以搜索到Ble设备",Toast.LENGTH_SHORT).show();
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                //permission was granted, yay! Do the contacts-related task you need to do.
                //这里进行授权被允许的处理
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                //这里进行权限被拒绝的处理
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) return true;
        return false;
    }


    //打开并扫描蓝牙
    private void openScanDevice() {
        MyBleManager.get().openBle();
        scanDevice();
    }

    private void initWidgets() {
        mListView = (ListView) findViewById(R.id.lv);
        mScanresults = new ArrayList<>();
        mScanAdapter = new ScanAdapter(this,mScanresults);
        mListView.setAdapter(mScanAdapter);

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDevice();
            }
        });



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mMaterialDialog = new MaterialDialog(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("确定连接："+mScanresults.get(position).getDevice().getName())
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                connect(mScanresults.get(position));
                                mMaterialDialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });

                mMaterialDialog.show();
            }
        });
    }


    private void connect(final ScanResult scanResult){
        MyBleManager.get().connectBle(scanResult, new ConnectCallBack() {
            @Override
            public void onConnecting(BluetoothGatt gatt, int status) {
                Log.d(TAG,"onConnecting");
            }

            @Override
            public void onConnectError(BleException exception) {
                Log.d(TAG,"onConnectError`-"+exception.getDescription());
            }

            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                Log.d(TAG,"onConnectSuccess`");
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Log.d(TAG,"onServicesDiscovered`");
                startActivity(new Intent(MainActivity.this,DataRoadActivity.class));
            }

            @Override
            public void onDisConnected(BluetoothGatt gatt, int status, BleException exception) {
                Log.d(TAG,"onDisConnected`"+exception.getDescription());
//                connectCount--;
//                if(connectCount > 0){
//                    connect(scanResult);
//                }
            }
        });
    }


    //扫面蓝牙设备
    private void scanDevice(){
        if(mScanresults !=null && mScanresults.size()>0){
            mScanresults.clear();
        }

        MyBleManager.get().scanDevice(new ScanCallBack() {
            @Override
            public void onScanning(ScanResult result) {

            }

            @Override
            public void onScanComplete(ScanResult[] results) {
                if(mScanresults == null){
                    return;
                }
                for(ScanResult sc : results){
                    if(!mScanresults.contains(sc)){
                        mScanresults.add(sc);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mScanAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyBleManager.get().closeBle();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (isLocationEnable(this)) {
                //定位已打开的处理
                Toast.makeText(getApplicationContext(),"定位已打开",Toast.LENGTH_SHORT).show();
            } else {
                //定位依然没有打开的处理
                Toast.makeText(getApplicationContext(),"定位没有打开",Toast.LENGTH_SHORT).show();
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }
}
