package saharweissman.taskrunner.tasks.sound;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import saharweissman.taskrunner.MainActivity;
import saharweissman.taskrunner.tasks.base.ITaskRunner;
import saharweissman.taskrunner.tasks.base.TaskInput;

public class TaskSetRingerMode extends ITaskRunner {

    private final String TAG = "TaskSetRingerMode";
    private TaskSetRingerModeInput mTaskInput = null;

    public TaskSetRingerMode(TaskInput input) {
        super(input);
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
    public void runTask(TaskInput input) {
        Log.d(TAG, "runTask");

        AudioManager am;
        am= (AudioManager) MainActivity.getActivityContext().getSystemService(Context.AUDIO_SERVICE);
        switch (mTaskInput.getRingerMode()){
            case TaskSetRingerModeInput.RINGER_MODE_SILENT:{
                Log.d(TAG, "runTask: set silent mode");
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            }
            case TaskSetRingerModeInput.RINGER_MODE_VIBRATE:{
                Log.d(TAG, "runTask: set silent mode");
                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            }
            case TaskSetRingerModeInput.RINGER_MODE_NORMAL:{
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            }
        }
    }
}