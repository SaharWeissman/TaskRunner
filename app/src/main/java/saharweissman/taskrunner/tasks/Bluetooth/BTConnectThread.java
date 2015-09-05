package saharweissman.taskrunner.tasks.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Thread used to establish a new connection to a bluetooth device
 *
 * Created by sahar on 9/5/15.
 */
public class BTConnectThread extends Thread {
    private final BluetoothDevice mDevice;
    private final BluetoothAdapter mAdapter;
    private final String TAG = "BTConnectThread";
    private final BluetoothSocket mSocket;
    private final IBTConnectCallback mCallback;

    public BTConnectThread(BluetoothAdapter adapter, BluetoothDevice device, UUID uuid, IBTConnectCallback callback){
        mDevice = device;
        mAdapter = adapter;
        mCallback = callback;

        // get BT socket for connection with device
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            Log.e(TAG, "creation of thread failed!");
        }
        mSocket = tmp;
    }

    @Override
    public void run() {
        super.run();
        Log.d(TAG, "run");

        // cancel discovery because it slows down connection
        mAdapter.cancelDiscovery();
        try {
            mSocket.connect();
        } catch (IOException e) {
            Log.e(TAG, "connect to socket failed!", e);
            mCallback.onConnectionFailed();
            try {
                mSocket.close();
            } catch (IOException e1) {
                Log.e(TAG, "cannot close socket after connection failure!", e1);
            }
            return;
        }
        mCallback.onConnectionSuccess();
    }

    public void cancel(){
        try {
            mSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "cancel: cannot close socket!");
        }
    }
}
