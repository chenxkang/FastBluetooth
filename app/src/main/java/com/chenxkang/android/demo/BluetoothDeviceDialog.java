package com.chenxkang.android.demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenxkang.android.fastbluetooth.BTManager;

/**
 * author: chenxkang
 * time  : 2020-01-13
 * desc  :
 */
public class BluetoothDeviceDialog extends AppCompatDialog implements View.OnClickListener {

    private Context mContext;
    private RecyclerView recyclerView;

    private String bluetoothAddress = "";
    private BluetoothDeviceAdapter bluetoothDeviceAdapter;

    private OnDeviceSelectedListener listener;

    public BluetoothDeviceDialog(@NonNull Context context, OnDeviceSelectedListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bluetooth_device);

        recyclerView = findViewById(R.id.bluetooth_device_recycler_view);

        findViewById(R.id.bluetooth_device_cancel_tv).setOnClickListener(this);
        findViewById(R.id.bluetooth_device_again_tv).setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        bluetoothDeviceAdapter = new BluetoothDeviceAdapter();
        recyclerView.setAdapter(bluetoothDeviceAdapter);

        bluetoothDeviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BluetoothDevice device = (BluetoothDevice) adapter.getItem(position);
                if (device != null && !TextUtils.isEmpty(device.getAddress())) {
                    dismiss();
                    listener.onDeviceClick(device);
                }
            }
        });

        BTManager.getDefault().init()
                .register(mContext, mReceiver, BluetoothDevice.ACTION_FOUND, BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                .startDiscovery();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.bluetooth_device_cancel_tv) {
            dismiss();
        } else if (viewId == R.id.bluetooth_device_again_tv) {
            BTManager.getDefault().startDiscovery();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BTManager.getDefault().cancelDiscovery().unregister(mContext, mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //搜索设备时，取得设备的MAC地址(过滤蓝牙)
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                addBluetoothDevice(device);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                BTManager.getDefault().cancelDiscovery();
            }
        }
    };

    private void addBluetoothDevice(BluetoothDevice device) {
        if (device != null
                && (TextUtils.isEmpty(bluetoothAddress) || !bluetoothAddress.contains(device.getAddress()))) {
            bluetoothAddress += device.getAddress();
            bluetoothDeviceAdapter.addData(device);
        }
    }

    public interface OnDeviceSelectedListener {
        void onDeviceClick(BluetoothDevice device);
    }
}
