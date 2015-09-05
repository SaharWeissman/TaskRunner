package saharweissman.taskrunner.tasks.base;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sahar on 9/5/15.
 */
public abstract class ITaskRunnerAsync extends ITaskRunner {
    private final String TAG = "ITaskRunnerAsync";
    private final long mTimeout;
    private Timer mTimer;

    public abstract void onTimeout();

    public ITaskRunnerAsync(TaskInput input, ITaskCallback callback, long timeout) {
        super(input, callback);
        mTimeout = timeout;
        mTimer = new Timer();
    }

    @Override
    public void run() {
        try {
            if (checkPre(mInput)) {
                initTask(mInput);
                runTask(mInput);
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        onTimeout();
                    }
                }, mTimeout);
            } else {
                handlePreCon();
            }
        }catch (Exception e){
            Log.e(TAG, "caught exception");
            if(mCallback != null){
                mCallback.onTaskError(null);//TODO: add specific taskError
            }
        }
    }

    @Override
    public void submitResult(TaskResult result) {
        super.submitResult(result);
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }
}
