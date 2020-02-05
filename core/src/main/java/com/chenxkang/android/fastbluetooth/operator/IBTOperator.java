package com.chenxkang.android.fastbluetooth.operator;

/**
 * author: chenxkang
 * time  : 2020-01-13
 * desc  : 蓝牙相关操作接口类
 */
public interface IBTOperator {

    boolean connect(String mac);

    boolean disconnect();

    boolean isConnect();

    boolean writeData(byte[] data);
}
