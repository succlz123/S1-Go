package org.succlz123.s1go.app.bean.forum;

import com.google.gson.Gson;

/**
 * Created by fashi on 2015/4/14.
 */
public class ForumObject {
    private ForumVariables Variables;
    private ForumMessage Message;

    public ForumMessage getMessage() {
        return Message;
    }

    public void setMessage(ForumMessage message) {
        Message = message;
    }

    public ForumVariables getVariables() {
        return Variables;
    }

    public void setVariables(ForumVariables variables) {
        Variables = variables;
    }

    public static ForumObject parseJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, ForumObject.class);
    }
}
