package saharweissman.taskrunner.executors;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by sahar on 8/29/15.
 */
public class ParallelExecutor extends ThreadPoolExecutor {
    private final String TAG = "ParallelExecutor";

    public ParallelExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        Log.d(TAG, "beforeExecute: t.id=" + t.getId());
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
        Log.d(TAG, "execute");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        Log.d(TAG, "afterExecute:");
    }

    @Override
    public void shutdown() {
        super.shutdown();
        Log.d(TAG, "shutdown");
    }
}
