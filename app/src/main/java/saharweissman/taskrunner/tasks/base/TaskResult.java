package saharweissman.taskrunner.tasks.base;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sahar on 8/29/15.
 */
public abstract class TaskResult {
    public abstract JSONObject getAsJson() throws JSONException;
}
