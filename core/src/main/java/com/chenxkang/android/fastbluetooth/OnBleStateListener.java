package com.chenxkang.android.fastbluetooth;

/**
 * author: chenxkang
 * time  : 2020-01-14
 * desc  : 定义本地蓝牙和远程蓝牙的状态监听
 */
public interface OnBleStateListener {

    void onLocalStatusChanged(BTState status);

    void onRemoteStatusChanged(BTState status);
}
