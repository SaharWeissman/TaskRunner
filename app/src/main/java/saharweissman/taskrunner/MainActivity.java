package saharweissman.taskrunner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import saharweissman.taskrunner.executors.TaskExecutor;
import saharweissman.taskrunner.tasks.Bluetooth.TaskBluetoothConnectInput;
import saharweissman.taskrunner.tasks.base.ITaskCallback;
import saharweissman.taskrunner.tasks.base.TaskError;
import saharweissman.taskrunner.tasks.base.TaskResult;
import saharweissman.taskrunner.tasks.sound.TaskSetRingerModeInput;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    // sound
    private final int MENU_ITEM_SET_RINGER_SILENT = 0;
    private final int MENU_ITEM_SET_RINGER_VIBRATE = 1;
    private final int MENU_ITEM_SET_RINGER_NORMAL = 2;

    // bluetooth
    private final int MENU_ITEM_CONNECT_BT = 10;

    private static Context sActivityContext = null;
    private String TEST_BT_DEVICE_NAME = "NYNE NB-250";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sActivityContext = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //sound
        menu.add(0, MENU_ITEM_SET_RINGER_SILENT, MENU_ITEM_SET_RINGER_SILENT, "Set Ringer - Silent");
        menu.add(0, MENU_ITEM_SET_RINGER_VIBRATE, MENU_ITEM_SET_RINGER_VIBRATE, "Set Ringer - Vibrate");
        menu.add(0, MENU_ITEM_SET_RINGER_NORMAL, MENU_ITEM_SET_RINGER_NORMAL, "Set Ringer - Normal");

        //bluetooth
        menu.add(0, MENU_ITEM_CONNECT_BT, MENU_ITEM_CONNECT_BT, "Connect to bluetooth device");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_ITEM_SET_RINGER_SILENT:{
                Log.d(TAG, "case MENU_ITEM_SET_RINGER_SILENT");
                runTaskSetRinger(TaskSetRingerModeInput.RINGER_MODE_SILENT);
                break;
            }
            case MENU_ITEM_SET_RINGER_VIBRATE:{
                Log.d(TAG, "case MENU_ITEM_SET_RINGER_VIBRATE");
                runTaskSetRinger(TaskSetRingerModeInput.RINGER_MODE_VIBRATE);
                break;
            }
            case MENU_ITEM_SET_RINGER_NORMAL:{
                Log.d(TAG, "case MENU_ITEM_SET_RINGER_NORMAL");
                runTaskSetRinger(TaskSetRingerModeInput.RINGER_MODE_NORMAL);
                break;
            }
            case MENU_ITEM_CONNECT_BT:{
                Log.d(TAG, "case MENU_ITEM_CONNECT_BT");
                showPairedDevicesDialog();
                break;
            }
            default:{
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPairedDevicesDialog() {

        //TODO: display list of paired devices & when user clicks one try to connect to it.
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            Log.e(TAG, "bluetooth adapter is null!");
            return;
        }
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        Iterator<BluetoothDevice> iterator = devices.iterator();
        while(iterator.hasNext()){
            BluetoothDevice device = iterator.next();
//            ParcelUuid[] uuids = device.getUuids();
//            StringBuilder sb = new StringBuilder();
//            for(int i = 0; i < uuids.length; i++){
//                sb.append(uuids[i].getUuid().toString() + ", ");
//            }

            Log.d(TAG, "device: name= " + device.getName() + ", address= " + device.getAddress());

            if(device.getName().equals(TEST_BT_DEVICE_NAME)){// testing connection to bluetooth speaker
                runTaskConnectBT(device);
                return;
            }
        }
    }

    private void runTaskConnectBT(BluetoothDevice device) {
        TaskBluetoothConnectInput input = new TaskBluetoothConnectInput(device);
        TaskExecutor.getInstance().runTask(2, input, new ITaskCallback() {
            @Override
            public void onTaskResult(TaskResult result) {
                try {
                    Log.d(TAG, "onTaskResult: result= " + result.getAsJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTaskError(TaskError error) {
                Log.e(TAG, "onTaskError");
            }

            @Override
            public void onTaskCancel() {
                Log.e(TAG, "onTaskCancel");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sActivityContext = null;
    }

    private void runTaskSetRinger(int ringerMode) {
        TaskSetRingerModeInput input = new TaskSetRingerModeInput(ringerMode);
        TaskExecutor.getInstance().runTask(1, input, new ITaskCallback() {
            @Override
            public void onTaskResult(TaskResult result) {
                try {
                    Log.d(TAG, "onTaskResult: result= " + result.getAsJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTaskError(TaskError error) {
                Log.d(TAG, "onTaskError: error= " + error);
            }

            @Override
            public void onTaskCancel() {
                Log.d(TAG, "onTaskCancel");
            }
        });
    }

    public static Context getActivityContext(){
        return sActivityContext;
    }
}
