package com.chenxkang.android.fastbluetooth.connect;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.chenxkang.android.fastbluetooth.operator.IBTOperator;
import com.chenxkang.android.fastbluetooth.listener.OnConnectListener;

/**
 * author: chenxkang
 * time  : 2020-01-13
 * desc  : 蓝牙异步连接
 */
public class ConnectAsyncTask extends AsyncTask<Void, String, Boolean> {

    private String mac;
    private IBTOperator operator;
    private OnConnectListener listener;

    public ConnectAsyncTask(String mac, IBTOperator iOperator, @NonNull OnConnectListener listener) {
        this.mac = mac;
        this.operator = iOperator;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreConnect();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return operator.connect(mac);
    }

    @Override
    protected void onPostExecute(Boolean isConnect) {
        if (isConnect) {
            listener.onPostConnect();
        } else {
            listener.onError("Bluetooth connection failed.");
        }
    }
}
