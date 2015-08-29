package saharweissman.taskrunner.managers;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import saharweissman.taskrunner.tasks.base.ITaskRunner;
import saharweissman.taskrunner.tasks.base.TaskInput;
import saharweissman.taskrunner.tasks.sound.TaskSetRingerMode;

/**
 * Created by sahar on 8/29/15.
 */
public class TaskManager {

    private static HashMap<Integer, Class> mSupportedTasksMap;
    private static TaskManager sInstance = null;
    private static final String TAG = "TaskManager";


    static {
        mSupportedTasksMap = new HashMap<Integer, Class>();

        mSupportedTasksMap.put(1, TaskSetRingerMode.class);
    }

    private TaskManager(){}

    public static TaskManager getInstance(){
        if(sInstance == null){
            sInstance = new TaskManager();
        }
        return sInstance;
    }

    public ITaskRunner getTaskRunner(int taskID, TaskInput input){
        Class taskRunnerClass = mSupportedTasksMap.get(taskID);
        if(taskRunnerClass == null){
            Log.e(TAG, "getTaskRunner: invalid taskID = " + taskID);
            return null;
        }

        try {
            Constructor<ITaskRunner> taskRunner = taskRunnerClass.getConstructor(TaskInput.class);
            return taskRunner.newInstance(input);
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
