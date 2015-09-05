package saharweissman.taskrunner.tasks.base;

import android.util.Log;

/**
 * Created by sahar on 8/29/15.
 */
public abstract class ITaskRunner implements Runnable {

    protected final TaskInput mInput;
    protected final ITaskCallback mCallback;
    private final String TAG = "ITaskRunner";

    public abstract boolean isSyncTask();

    public abstract boolean checkPre(TaskInput input);
    public abstract void initTask(TaskInput input);
    public abstract void runTask(TaskInput input);
    protected abstract void onCancelTask();

    public void cancel(){
        onCancelTask();
        if(mCallback != null){
            mCallback.onTaskCancel();
        }
    }


    public ITaskRunner(TaskInput input, ITaskCallback callback){
        mInput = input;
        mCallback = callback;
    }

    @Override
    public void run() {
        try {
            if (checkPre(mInput)) {
                initTask(mInput);
                runTask(mInput);
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

    void handlePreCon() {

    }

    public void submitResult(TaskResult result){
        mCallback.onTaskResult(result);
    }
}
