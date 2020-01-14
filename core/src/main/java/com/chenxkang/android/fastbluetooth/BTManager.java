package com.chenxkang.android.fastbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

/**
 * author: chenxkang
 * time  : 2020-01-13
 * desc  : 蓝牙管理器
 */
public class BTManager {

    static volatile BTManager managerInstance;
    private static IBTOperator iOperator = null;

    private BluetoothAdapter bluetoothAdapter;

    public static BTManager getDefault() {
        if (managerInstance == null) {
            synchronized (BTManager.class) {
                if (managerInstance == null) {
                    managerInstance = new BTManager();
                }
            }
        }
        return managerInstance;
    }

    public BTManager init() {
        if (!isEnabled())
            bluetoothAdapter.enable();

        iOperator = new BTOperator(bluetoothAdapter);
        return this;
    }

    public boolean isEnabled(){
        if (bluetoothAdapter != null){
            return bluetoothAdapter.isEnabled();
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new IllegalArgumentException("Bluetooth is not supported on this device.");
        }
        return bluetoothAdapter.isEnabled();
    }

    public BTManager register(Context context, BroadcastReceiver receiver, String... actions) {
        if (receiver != null && actions != null) {
            IntentFilter filter = new IntentFilter();
            for (String action : actions) {
                filter.addAction(action);
            }
            context.registerReceiver(receiver, filter);
        }
        return this;
    }

    public BTManager unregister(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
        return this;
    }

    public BTManager startDiscovery() {
        if (!isEnabled())
            bluetoothAdapter.enable();

        if (bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();

        bluetoothAdapter.startDiscovery();
        return this;
    }

    public BTManager cancelDiscovery() {
        if (bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();
        return this;
    }

    public boolean isConnect(){
        if (iOperator != null) {
            return iOperator.isConnect();
        }
        return false;
    }

    public boolean connect(String mac) {
        if (iOperator != null) {
            return iOperator.connect(mac);
        }
        return false;
    }

    public void connect(String mac, @NonNull OnConnectListener listener) {
        if (!isEnabled()){
            listener.onError("Please turn on Bluetooth.");
            return;
        }

        if (iOperator != null) {
            new ConnectAsyncTask(mac, iOperator, listener).execute();
        }
    }

    public boolean disconnect() {
        if (iOperator != null && iOperator.isConnect()) {
            return iOperator.disconnect();
        }
        return true;
    }

    public boolean writeData(byte[] data){
        if (iOperator != null) {
            return iOperator.writeData(data);
        }
        return false;
    }
}
