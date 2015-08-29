package saharweissman.taskrunner.tasks.sound;

import saharweissman.taskrunner.tasks.base.TaskInput;

/**
 * Created by sahar on 8/29/15.
 */
public class TaskSetRingerModeInput extends TaskInput {

    public static final int RINGER_MODE_SILENT = 0;
    public static final int RINGER_MODE_VIBRATE = 1;
    public static final int RINGER_MODE_NORMAL = 2;

    private int mRingerMode;

    public TaskSetRingerModeInput(int ringerMode){
        mRingerMode = ringerMode;
    }

    public int getRingerMode(){
        return mRingerMode;
    }

    @Override
    public boolean validateInput() throws IllegalArgumentException {
        if(mRingerMode != RINGER_MODE_NORMAL && mRingerMode != RINGER_MODE_SILENT && mRingerMode != RINGER_MODE_VIBRATE){
            throw new IllegalArgumentException("invalid input!");
        }
        return true;
    }
}
