package saharweissman.taskrunner.tasks.base;

/**
 * Created by sahar on 8/29/15.
 */
public abstract class TaskInput {

    public abstract boolean validateInput() throws IllegalArgumentException;

    public TaskInput(){
//        validateInput();
    }
}
