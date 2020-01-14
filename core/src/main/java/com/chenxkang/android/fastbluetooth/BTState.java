package com.chenxkang.android.fastbluetooth;

/**
 * author: chenxkang
 * time  : 2020-01-14
 * desc  : 蓝牙状态
 */
public enum BTState {

    BT_STATE_ON("BT_ON"),// 本地蓝牙已开启
    BT_STATE_OFF("BT_OFF"),// 本地蓝牙已关闭
    BT_STATE_CONNECTED("BT_CONNECTED"),// 远程蓝牙已连接
    BT_STATE_DISCONNECTED("BT_DISCONNECTED");// 远程蓝牙已关闭

    private String status;

    BTState(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
