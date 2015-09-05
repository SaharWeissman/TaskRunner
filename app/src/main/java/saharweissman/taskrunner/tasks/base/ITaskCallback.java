package saharweissman.taskrunner.tasks.base;

/**
 * Created by sahar on 8/29/15.
 */
public interface ITaskCallback {
    void onTaskResult(TaskResult result);
    void onTaskError(TaskError error);
    void onTaskCancel();
}
