package saharweissman.taskrunner.executors;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import saharweissman.taskrunner.managers.TaskManager;
import saharweissman.taskrunner.tasks.base.ITaskRunner;
import saharweissman.taskrunner.tasks.base.TaskInput;

/**
 * Created by sahar on 8/29/15.
 */
public class TaskExecutor {

    private static TaskExecutor sInstance = null;
    private static int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private ThreadPoolExecutor mTaskThreadPool = null;
    private final BlockingQueue<Runnable> mTaskQueue;
    private static final String TAG = "TaskExecutor";
    private Handler mUIHandler = null;

    public static TaskExecutor getInstance(){
        if(sInstance == null){
            sInstance = new TaskExecutor();
        }
        return sInstance;
    }


    private TaskExecutor(){

        // init work queue
        mTaskQueue = new LinkedBlockingQueue<Runnable>();


        // init ui handler
        mUIHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "handleMessage");
            }
        };

        // create thread pool manager
        mTaskThreadPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mTaskQueue
        );
    }

    public void startTask(int taskID, TaskInput input){
        ITaskRunner runner = TaskManager.getInstance().getTaskRunner(taskID, input);
        if(runner == null){
            Log.e(TAG, "startTask: invalid taskID!");
            return;
        }
        sInstance.mTaskThreadPool.execute(runner);
    }
}
