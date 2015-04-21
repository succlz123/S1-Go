package org.succlz123.s1go.app.bean.thread;

import com.google.gson.Gson;

/**
 * Created by fashi on 2015/4/15.
 */
public class ThreadObject {
    private ThreadVariables Variables;

    public ThreadVariables getVariables() {
        return Variables;
    }

    public void setVariables(ThreadVariables variables) {
        Variables = variables;
    }

    public static ThreadObject parseJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, ThreadObject.class);
    }
}
