package saharweissman.taskrunner.tasks.base;

/**
 * Created by sahar on 8/29/15.
 */
public abstract class ITaskRunner implements Runnable {

    private final TaskInput mInput;

    public abstract boolean checkPre(TaskInput input);
    public abstract void initTask(TaskInput input);
    public abstract void runTask(TaskInput input);

    public ITaskRunner(TaskInput input){
        mInput = input;
    }

    @Override
    public void run() {
        if(checkPre(mInput)){
            initTask(mInput);
            runTask(mInput);
        }else{
            handlePreCon();
        }
    }

    void handlePreCon() {

    }
}
