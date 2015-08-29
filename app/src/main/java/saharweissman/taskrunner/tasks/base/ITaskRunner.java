package saharweissman.taskrunner.tasks.base;

import android.util.Log;

/**
 * Created by sahar on 8/29/15.
 */
public abstract class ITaskRunner implements Runnable {

    private final TaskInput mInput;
    private final ITaskCallback mCallback;
    private final String TAG = "ITaskRunner";

    public abstract boolean isSyncTask();

    public abstract boolean checkPre(TaskInput input);
    public abstract void initTask(TaskInput input);
    public abstract boolean runTask(TaskInput input);
    public abstract TaskResult getResult();
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
                if (runTask(mInput)) {
                    mCallback.onTaskSuccess(getResult());
                }else{
                    mCallback.onTaskError(null);// TODO: add task error
                }
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
}
