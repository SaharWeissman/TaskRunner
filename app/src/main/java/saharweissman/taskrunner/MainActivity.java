package saharweissman.taskrunner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;

import saharweissman.taskrunner.executors.TaskExecutor;
import saharweissman.taskrunner.tasks.base.ITaskCallback;
import saharweissman.taskrunner.tasks.base.TaskError;
import saharweissman.taskrunner.tasks.base.TaskResult;
import saharweissman.taskrunner.tasks.sound.TaskSetRingerModeInput;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private final int MENU_ITEM_SET_RINGER_SILENT = 0;
    private final int MENU_ITEM_SET_RINGER_VIBRATE = 1;
    private final int MENU_ITEM_SET_RINGER_NORMAL = 2;

    private static Context sActivityContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sActivityContext = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ITEM_SET_RINGER_SILENT, MENU_ITEM_SET_RINGER_SILENT, "Set Ringer - Silent");
        menu.add(0, MENU_ITEM_SET_RINGER_VIBRATE, MENU_ITEM_SET_RINGER_VIBRATE, "Set Ringer - Vibrate");
        menu.add(0, MENU_ITEM_SET_RINGER_NORMAL, MENU_ITEM_SET_RINGER_NORMAL, "Set Ringer - Normal");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_ITEM_SET_RINGER_SILENT:{
                Log.d(TAG, "case MENU_ITEM_SET_RINGER_SILENT");
                runTaskSetAlarm(TaskSetRingerModeInput.RINGER_MODE_SILENT);
                break;
            }
            case MENU_ITEM_SET_RINGER_VIBRATE:{
                Log.d(TAG, "case MENU_ITEM_SET_RINGER_VIBRATE");
                runTaskSetAlarm(TaskSetRingerModeInput.RINGER_MODE_VIBRATE);
                break;
            }
            case MENU_ITEM_SET_RINGER_NORMAL:{
                Log.d(TAG, "case MENU_ITEM_SET_RINGER_NORMAL");
                runTaskSetAlarm(TaskSetRingerModeInput.RINGER_MODE_NORMAL);
                break;
            }
            default:{
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sActivityContext = null;
    }

    private void runTaskSetAlarm(int ringerMode) {
        TaskSetRingerModeInput input = new TaskSetRingerModeInput(ringerMode);
        TaskExecutor.getInstance().runTask(1, input, new ITaskCallback() {
            @Override
            public void onTaskSuccess(TaskResult result) {
                try {
                    Log.d(TAG, "onTaskSuccess: result= " + result.getAsJson().toString());
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
