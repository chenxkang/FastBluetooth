package com.chenxkang.android.fastbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * author: chenxkang
 * time  : 2020-01-13
 * desc  : 蓝牙相关操作实现类
 */
public class BTOperator implements IBTOperator {

    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static BluetoothSocket bluetoothSocket;

    private BluetoothAdapter bluetoothAdapter;

    private InputStream inputStream;
    private OutputStream outputStream;

    public BTOperator(BluetoothAdapter adapter) {
        this.bluetoothAdapter = adapter;
    }

    @Override
    public boolean connect(String mac) {
        bluetoothAdapter.cancelDiscovery();
        if (mac == null) {
            throw new IllegalArgumentException("The mac can't be null.");
        }

        mac = mac.toUpperCase();

        try {
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(mac);
            if (bluetoothDevice == null) {
                return false;
            }
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothSocket.connect();
        } catch (Exception e) {
            return false;
        }

        try {
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean disconnect() {
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }

            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }

            if (bluetoothSocket != null) {
                bluetoothSocket.close();
                bluetoothSocket = null;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isConnect() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }

    @Override
    public boolean writeData(byte[] data) {
        if (this.outputStream == null) {
            return false;
        }

        try {
            byte[] bytes01 = new byte[10000];
            int length = data.length;
            int size = length / 10000;
            int index;
            for (int i = 0; i < size; ++i) {
                for (index = i * 10000; index < (i + 1) * 10000; ++index) {
                    bytes01[index % 10000] = data[index];
                }
                this.outputStream.write(bytes01, 0, bytes01.length);
                this.outputStream.flush();
            }

            if (length % 10000 != 0) {
                byte[] bytes02 = new byte[data.length - size * 10000];
                for (index = size * 10000; index < data.length; ++index) {
                    bytes02[index - size * 10000] = data[index];
                }

                this.outputStream.write(bytes02, 0, bytes02.length);
                this.outputStream.flush();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
