package saharweissman.taskrunner.tasks.Bluetooth;

import android.bluetooth.BluetoothDevice;

import saharweissman.taskrunner.tasks.base.TaskInput;

/**
 * Created by sahar on 9/5/15.
 */
public class TaskBluetoothConnectInput extends TaskInput {

    private final BluetoothDevice mDevice;

    public TaskBluetoothConnectInput(BluetoothDevice device){
        mDevice = device;
    }

    public BluetoothDevice getDevice(){
        return mDevice;
    }

    @Override
    public boolean validateInput() throws IllegalArgumentException {
        if(mDevice == null){
            throw new IllegalArgumentException("invalid input! (null)");
        }
        return true;
    }
}
