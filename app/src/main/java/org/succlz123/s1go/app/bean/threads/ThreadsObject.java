package org.succlz123.s1go.app.bean.threads;

import com.google.gson.Gson;

/**
 * Created by fashi on 2015/4/14.
 */
public class ThreadsObject {
    private ThreadsVariables Variables;
    private ThreadsMessage Message;

    public ThreadsMessage getMessage() {
        return Message;
    }

    public void setMessage(ThreadsMessage message) {
        Message = message;
    }

    public ThreadsVariables getVariables() {
        return Variables;
    }

    public void setVariables(ThreadsVariables variables) {
        Variables = variables;
    }

    public static ThreadsObject parseJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, ThreadsObject.class);
    }
}
