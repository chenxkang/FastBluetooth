package com.chenxkang.android.demo;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chenxkang.android.fastbluetooth.BTManager;
import com.chenxkang.android.fastbluetooth.OnConnectListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mMobileStatusTv;
    private TextView mBluetoothNameTv, mBluetoothClassTv, mBluetoothAddressTv;
    private Button mConnectBtn;

    private String BT_TAG = "BT_TAG";
    private BluetoothDevice selectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMobileStatusTv = findViewById(R.id.main_bluetooth_mobile_tv);
        mBluetoothNameTv = findViewById(R.id.main_bluetooth_name_tv);
        mBluetoothClassTv = findViewById(R.id.main_bluetooth_class_tv);
        mBluetoothAddressTv = findViewById(R.id.main_bluetooth_address_tv);
        mConnectBtn = findViewById(R.id.main_bluetooth_connect_btn);

        findViewById(R.id.main_bluetooth_search_btn).setOnClickListener(this);
        findViewById(R.id.main_bluetooth_close_btn).setOnClickListener(this);
        mConnectBtn.setOnClickListener(this);

        changeBtnStatus(true, "连接");
        if (BTManager.getDefault().isEnabled()) {
            changeConnectStatus(BluetoothAdapter.STATE_ON);
        }

        BTManager.getDefault().init().register(this, mReceiver,
                BluetoothAdapter.ACTION_STATE_CHANGED,
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED,
                BluetoothDevice.ACTION_ACL_CONNECTED,
                BluetoothDevice.ACTION_ACL_DISCONNECTED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_bluetooth_search_btn:
                new BluetoothDeviceDialog(MainActivity.this, new BluetoothDeviceDialog.OnDeviceSelectedListener() {
                    @Override
                    public void onDeviceClick(BluetoothDevice device) {
                        if (device != null) {
                            mBluetoothNameTv.setText("蓝牙名称：" + device.getName());
                            mBluetoothClassTv.setText("蓝牙类型：" + device.getBluetoothClass().getMajorDeviceClass());
                            mBluetoothAddressTv.setText("蓝牙地址：" + device.getAddress());

                            selectedDevice = device;
                            connectDevice();
                        }
                    }
                }).show();
                break;
            case R.id.main_bluetooth_close_btn:
                if (!BTManager.getDefault().isConnect()) {
                    Toast.makeText(MainActivity.this, "当前未连接任何蓝牙设备！", Toast.LENGTH_LONG).show();
                    return;
                }

                if (BTManager.getDefault().disconnect()) {
                    selectedDevice = null;
                    Toast.makeText(MainActivity.this, "蓝牙设备已断开！", Toast.LENGTH_LONG).show();
                    mBluetoothNameTv.setText("蓝牙名称：......");
                    mBluetoothClassTv.setText("蓝牙类型：......");
                    mBluetoothAddressTv.setText("蓝牙地址：......");
                }
                break;
            case R.id.main_bluetooth_connect_btn:
                connectDevice();
                break;
        }
    }

    private void connectDevice() {
        if (selectedDevice == null) {
            Toast.makeText(MainActivity.this, "请搜索蓝牙设备！", Toast.LENGTH_LONG).show();
            return;
        }
        BTManager.getDefault().connect(selectedDevice.getAddress(), new OnConnectListener() {
            @Override
            public void onPreConnect() {
                Log.i(BT_TAG, "onPreConnect: 开始连接");
                changeBtnStatus(false, "连接中");
            }

            @Override
            public void onPostConnect() {
                Log.i(BT_TAG, "onPostConnect: 已连接");
                changeBtnStatus(false, "已连接");
            }

            @Override
            public void onError(String error) {
                Log.i(BT_TAG, "onError: " + error);
                changeBtnStatus(true, "连接");
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BTManager.getDefault().unregister(this, mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action))
                return;

            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    Log.i(BT_TAG, "onReceive: ACTION_STATE_CHANGED");
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    changeConnectStatus(state);
                    break;
                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    Log.i(BT_TAG, "onReceive: ACTION_CONNECTION_STATE_CHANGED");
                    int connectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED);
                    changeConnectStatus(connectionState);
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Log.i(BT_TAG, "onReceive: ACTION_ACL_CONNECTED");
                    Toast.makeText(MainActivity.this, "远程蓝牙设备已连接", Toast.LENGTH_LONG).show();
                    changeBtnStatus(false, "已连接");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log.i(BT_TAG, "onReceive: ACTION_ACL_DISCONNECTED");
                    Toast.makeText(MainActivity.this, "远程蓝牙设备已断开", Toast.LENGTH_LONG).show();
                    changeBtnStatus(true, "连接");
                    break;
            }
        }
    };

    private void changeConnectStatus(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_TURNING_ON:
                Log.i(BT_TAG, "changeConnectStatus: STATE_TURNING_ON");
                mMobileStatusTv.setText("当前设备蓝牙正在开启");
                break;
            case BluetoothAdapter.STATE_ON:
                Log.i(BT_TAG, "changeConnectStatus: STATE_ON");
                mMobileStatusTv.setText("当前设备蓝牙已开启");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                Log.i(BT_TAG, "changeConnectStatus: STATE_TURNING_OFF");
                mMobileStatusTv.setText("当前设备蓝牙正在关闭");
                break;
            case BluetoothAdapter.STATE_OFF:
                Log.i(BT_TAG, "changeConnectStatus: STATE_OFF");
                mMobileStatusTv.setText("当前设备蓝牙已关闭");
                break;
            case BluetoothAdapter.STATE_CONNECTING:
                Log.i(BT_TAG, "changeConnectStatus: STATE_CONNECTING");
                changeBtnStatus(false, "连接中");
                break;
            case BluetoothAdapter.STATE_CONNECTED:
                Log.i(BT_TAG, "changeConnectStatus: STATE_CONNECTED");
                changeBtnStatus(false, "已连接");
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                Log.i(BT_TAG, "changeConnectStatus: STATE_DISCONNECTING");
                changeBtnStatus(false, "断开中");
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                Log.i(BT_TAG, "changeConnectStatus: STATE_DISCONNECTED");
                changeBtnStatus(true, "连接");
                break;
        }
    }

    private void changeBtnStatus(boolean clickable, String status) {
        mConnectBtn.setText(status);
        mConnectBtn.setClickable(clickable);
        if (clickable) {
            mConnectBtn.setTextColor(Color.parseColor("#ffffffff"));
            mConnectBtn.setBackgroundResource(R.drawable.button_blue_bg);
        } else {
            mConnectBtn.setTextColor(Color.parseColor("#99999999"));
            mConnectBtn.setBackgroundResource(R.drawable.button_gray_bg);
        }
    }
}
