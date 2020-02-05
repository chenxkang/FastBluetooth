package com.chenxkang.android.fastbluetooth.manager;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chenxkang.android.fastbluetooth.connect.ConnectAsyncTask;
import com.chenxkang.android.fastbluetooth.listener.OnConnectListener;
import com.chenxkang.android.fastbluetooth.listener.OnResultListener;
import com.chenxkang.android.fastbluetooth.operator.BTOperator;
import com.chenxkang.android.fastbluetooth.operator.IBTOperator;

import java.util.List;

/**
 * author: chenxkang
 * time  : 2020-01-13
 * desc  : 蓝牙管理器
 */
public class BTManager {

    static volatile BTManager managerInstance;
    private static IBTOperator iOperator = null;

    private BluetoothAdapter bluetoothAdapter;

    public BTManager() {

    }

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
        if (isSupport() && !isEnabled())
            bluetoothAdapter.enable();

        iOperator = new BTOperator(bluetoothAdapter);
        return this;
    }

    public boolean isSupport() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null;
    }

    public boolean isEnabled() {
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.isEnabled();
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return false;
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
        if (isSupport()) {
            if (!isEnabled())
                bluetoothAdapter.enable();

            if (bluetoothAdapter.isDiscovering())
                bluetoothAdapter.cancelDiscovery();

            bluetoothAdapter.startDiscovery();
        }
        return this;
    }

    public BTManager cancelDiscovery() {
        if (isSupport()) {
            if (bluetoothAdapter.isDiscovering())
                bluetoothAdapter.cancelDiscovery();
        }
        return this;
    }

    public boolean isConnect() {
        if (iOperator != null) {
            return iOperator.isConnect();
        }
        return false;
    }

    public boolean connect(String mac) {
        if (iOperator != null) {
            if (iOperator.isConnect()) {
                return true;
            }
            return iOperator.connect(mac);
        }
        return false;
    }

    public void connect(String mac, @NonNull OnConnectListener listener) {
        if (!isEnabled()) {
            listener.onError("Please turn on Bluetooth.");
            return;
        }

        if (iOperator == null)
            iOperator = new BTOperator(bluetoothAdapter);

        if (iOperator.isConnect()) {
            listener.onPostConnect();
            return;
        }
        new ConnectAsyncTask(mac, iOperator, listener).execute();
    }

    public boolean disconnect() {
        if (iOperator != null && iOperator.isConnect()) {
            return iOperator.disconnect();
        }
        return true;
    }

    public void post(List<byte[]> commands, @NonNull OnResultListener listener) {
        if (commands != null && commands.size() > 0) {
            for (byte[] command : commands) {
                if (!writeData(command)) {
                    listener.onError("The command is error.");
                    break;
                }
            }
            listener.onSuccess();
            return;
        }

        listener.onError("commands is null.");
    }

    public void post(String mac, final List<byte[]> commands, @NonNull final OnResultListener listener) {
        if (TextUtils.isEmpty(mac)) {
            listener.onError("The mac can't be null.");
            return;
        }

        connect(mac, new OnConnectListener() {
            @Override
            public void onPreConnect() {

            }

            @Override
            public void onPostConnect() {
                if (commands != null) {
                    for (byte[] command : commands) {
                        if (!writeData(command)) {
                            listener.onError("The command is error.");
                            break;
                        }
                    }
                    listener.onSuccess();
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    private boolean writeData(byte[] data) {
        if (iOperator != null) {
            return iOperator.writeData(data);
        }
        return false;
    }
}
