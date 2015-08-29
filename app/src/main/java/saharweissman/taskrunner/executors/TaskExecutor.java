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
import saharweissman.taskrunner.tasks.base.ITaskCallback;
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

    private ParallelExecutor mTaskThreadPoolParallel = null;

    private ThreadPoolExecutor mTaskThreadPoolSync = null;
    private final BlockingQueue<Runnable> mTaskQueueSync;
    private final BlockingQueue<Runnable> mTaskQueueParallel;
    private static final String TAG = "TaskExecutor";

    public static TaskExecutor getInstance(){
        if(sInstance == null){
            sInstance = new TaskExecutor();
        }
        return sInstance;
    }


    private TaskExecutor(){

        // init work queue
        mTaskQueueSync = new LinkedBlockingQueue<Runnable>();
        mTaskQueueParallel = new LinkedBlockingQueue<Runnable>();


        // create thread pool manager
        // sync - for UI
        mTaskThreadPoolSync = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mTaskQueueSync
        );

        // sync - for non-UI
        mTaskThreadPoolParallel = new ParallelExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
                mTaskQueueParallel
        );
    }

    public void runTask(int taskID, TaskInput input, ITaskCallback callback){
        ITaskRunner runner = TaskManager.getInstance().getTaskRunner(taskID, input, callback);
        if(runner == null){
            Log.e(TAG, "runTask: invalid taskID!");
            return;
        }
        if(runner.isSyncTask()) {
            sInstance.mTaskThreadPoolSync.execute(runner);
        }else{
            sInstance.mTaskThreadPoolParallel.execute(runner);
        }
    }
}
