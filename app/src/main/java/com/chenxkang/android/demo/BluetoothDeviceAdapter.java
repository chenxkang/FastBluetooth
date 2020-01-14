package com.chenxkang.android.demo;

import android.bluetooth.BluetoothDevice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * author: chenxkang
 * time  : 2020-01-13
 * desc  :
 */
public class BluetoothDeviceAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

    public BluetoothDeviceAdapter() {
        super(R.layout.view_bluetooth_device);
    }

    @Override
    protected void convert(BaseViewHolder helper, BluetoothDevice item) {
        helper.setText(R.id.item_bluetooth_device_name_tv, item.getName());
        helper.setText(R.id.item_bluetooth_device_mac_tv, item.getAddress());
    }
}
