package org.succlz123.s1go.app.bean.login;

import com.google.gson.Gson;

/**
 * Created by fashi on 2015/4/18.
 */
public class LoginObject {
    private LoginVariables Variables;
    private LoginMessage Message;

    public LoginMessage getMessage() {
        return Message;
    }

    public void setMessage(LoginMessage message) {
        Message = message;
    }

    public LoginVariables getVariables() {
        return Variables;
    }

    public void setVariables(LoginVariables variables) {
        Variables = variables;
    }

    public static LoginObject parseJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, LoginObject.class);
    }
}
