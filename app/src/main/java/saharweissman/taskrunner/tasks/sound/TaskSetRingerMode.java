package saharweissman.taskrunner.tasks.sound;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import saharweissman.taskrunner.MainActivity;
import saharweissman.taskrunner.tasks.base.ITaskCallback;
import saharweissman.taskrunner.tasks.base.ITaskRunner;
import saharweissman.taskrunner.tasks.base.TaskInput;
import saharweissman.taskrunner.tasks.base.TaskResult;

public class TaskSetRingerMode extends ITaskRunner {

    private final String TAG = "TaskSetRingerMode";
    private TaskSetRingerModeInput mTaskInput = null;
    private TaskSetRingerModeResult mResult;

    public TaskSetRingerMode(TaskInput input, ITaskCallback callback) {
        super(input, callback);
        mResult = new TaskSetRingerModeResult();
    }

    @Override
    public boolean isSyncTask() {
        return false;
    }

    @Override
    public boolean checkPre(TaskInput input) {
        if(!(input instanceof TaskSetRingerModeInput)){
            Log.e(TAG, "invalid input class!");
            return false;
        }
        mTaskInput = (TaskSetRingerModeInput)input;
        Log.d(TAG, "checkPre: input = " + mTaskInput.getRingerMode());
        return true;
    }

    @Override
    public void initTask(TaskInput input) {
        Log.d(TAG, "initTask");
    }

    @Override
    public boolean runTask(TaskInput input) {
        Log.d(TAG, "runTask");

        AudioManager am;
        try{
            am= (AudioManager) MainActivity.getActivityContext().getSystemService(Context.AUDIO_SERVICE);
            switch (mTaskInput.getRingerMode()) {
                case TaskSetRingerModeInput.RINGER_MODE_SILENT: {
                    Log.d(TAG, "runTask: set silent mode");
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    break;
                }
                case TaskSetRingerModeInput.RINGER_MODE_VIBRATE: {
                    Log.d(TAG, "runTask: set silent mode");
                    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    break;
                }
                case TaskSetRingerModeInput.RINGER_MODE_NORMAL: {
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
                }
            }
        }catch (Exception e){
            return false;
        }
        mResult.mSuccess = true;
        return true;
    }

    @Override
    public TaskResult getResult() {
        return mResult;
    }

    @Override
    protected void onCancelTask() {
        Log.d(TAG, "onCancelTask");
    }
}