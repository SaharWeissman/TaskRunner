package saharweissman.taskrunner.tasks.sound;

import org.json.JSONException;
import org.json.JSONObject;

import saharweissman.taskrunner.tasks.base.TaskResult;

/**
 * Created by sahar on 8/29/15.
 */
public class TaskSetRingerModeResult extends TaskResult {

    private final String KEY_SUCCESS = "success";

    public boolean mSuccess = false;

    @Override
    public JSONObject getAsJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(KEY_SUCCESS, mSuccess);
        return json;
    }
}
