package saharweissman.taskrunner.tasks.Bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothA2dp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import saharweissman.taskrunner.MainActivity;
import saharweissman.taskrunner.tasks.base.ITaskCallback;
import saharweissman.taskrunner.tasks.base.ITaskRunnerAsync;
import saharweissman.taskrunner.tasks.base.TaskInput;

/**
 * Created by sahar on 9/5/15.
 */
public class TaskBluetoothA2DPConnect extends ITaskRunnerAsync implements IBTConnectCallback {
    private static final long TIMEOUT = 10 * 1000;
    private final TaskBluetoothConnectResult mResult;
    private final String TAG = "TaskBluetoothA2DPConnect";
    private TaskBluetoothConnectInput mTaskInput;
    private BTConnectThread mConnectThread;
    private final String BTA2D_SERVICE_NAME = "bluetooth_a2dp";
    private BroadcastReceiver mBluetoothReceiver;

    public TaskBluetoothA2DPConnect(TaskInput input, ITaskCallback callback) {
        super(input, callback, TIMEOUT);
        mResult = new TaskBluetoothConnectResult();
    }

    @Override
    public boolean isSyncTask() {
        return false;
    }

    @Override
    public boolean checkPre(TaskInput input) {

        //TODO: check if Bluetooth in on / adapter exist!

        if(!(input instanceof TaskBluetoothConnectInput)){
            Log.e(TAG, "invalid input class!");
        }
        mTaskInput = (TaskBluetoothConnectInput)input;
        return true;
    }

    @Override
    public void initTask(TaskInput input) {
        Log.d(TAG, "initTask");
    }

    @Override
    public void runTask(TaskInput input) {
        try {
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder binder = (IBinder)method.invoke(clazz.newInstance(), BTA2D_SERVICE_NAME);
            if(binder == null){
                // android 4.2+
                BluetoothAdapter.getDefaultAdapter().getProfileProxy(MainActivity.getActivityContext(), new BluetoothProfile.ServiceListener() {
                    @Override
                    public void onServiceConnected(int profile, BluetoothProfile proxy) {
                        Log.d(TAG, "onServiceConnected");
                        BluetoothA2dp a2dp = (BluetoothA2dp)proxy;
                        try {
                            mBluetoothReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    if(intent != null){
                                        int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothAdapter.STATE_DISCONNECTED);
                                        if(state == BluetoothAdapter.STATE_CONNECTED){
                                            BluetoothDevice connectedDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                            mResult.mSuccess = (areBTDevicesEqual(connectedDevice, mTaskInput.getDevice()));
                                            finishTask();
                                        }
                                    }
                                }
                            };
                            MainActivity.getActivityContext().registerReceiver(mBluetoothReceiver, new IntentFilter(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED));
                            a2dp.getClass().getMethod("connect", BluetoothDevice.class).invoke(a2dp, mTaskInput.getDevice());
                        } catch (IllegalAccessException e) {
                            Log.e(TAG, "onServiceConnected: IllegalAccessException", e);
                            mResult.mSuccess = false;
                        } catch (InvocationTargetException e) {
                            Log.e(TAG, "onServiceConnected: InvocationTargetException", e);
                            mResult.mSuccess = false;
                        } catch (NoSuchMethodException e) {
                            Log.e(TAG, "onServiceConnected: NoSuchMethodException", e);
                            mResult.mSuccess = false;
                        }
                    }

                    @Override
                    public void onServiceDisconnected(int profile) {
                        Log.d(TAG, "onServiceDisconnected");
                        finishTask();
                    }
                }, BluetoothProfile.A2DP);
            }else{ //android -4.2
                Class<?> c3 = Class.forName("android.bluetooth.IBluetoothA2dp");
                Class<?>[] s2 = c3.getDeclaredClasses();
                Class<?> c = s2[0];
                Method m = c.getDeclaredMethod("asInterface", IBinder.class);
                m.setAccessible(true);
                IBluetoothA2dp a2dp = (IBluetoothA2dp) m.invoke(null, binder);
                a2dp.connect(mTaskInput.getDevice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelTask() {
        Log.d(TAG, "onCancelTask");
        if(mConnectThread != null){
            mConnectThread.cancel();
        }
    }

    @Override
    public void onConnectionFailed() {
        mResult.mSuccess = false;
        finishTask();
    }

    @Override
    public void onConnectionSuccess() {
        mResult.mSuccess = true;
        finishTask();

    }

    private void finishTask() {
        if(mConnectThread != null) {
            mConnectThread.cancel();
        }
        if(mBluetoothReceiver != null){
            try{
                MainActivity.getActivityContext().unregisterReceiver(mBluetoothReceiver);
            }catch (IllegalArgumentException e){
                Log.d(TAG, "broadcast receiver not registered!");
            }
        }
        submitResult(mResult);
    }

    @Override
    public void onTimeout() {
        submitResult(mResult);
    }

    private boolean areBTDevicesEqual(BluetoothDevice connectedDevice, BluetoothDevice device) {
        if(connectedDevice == null || device == null) return false;
        return (connectedDevice.getAddress().equals(device.getAddress())); // compare max address
    }
}
