package com.chenxkang.android.fastbluetooth.listener;

/**
 * author: chenxkang
 * time  : 2020-01-13
 * desc  : 蓝牙连接监听事件
 */
public interface OnConnectListener {

    void onPreConnect();

    void onPostConnect();

    void onError(String error);
}
