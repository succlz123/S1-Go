package org.succlz123.s1go.app.bean.hostthreads;

import com.google.gson.Gson;

/**
 * Created by fashi on 2015/4/12.
 */
public class HotThreadsObject {
    private String Version;
    private String Charset;
    private HotThreadsVariables Variables;

    public HotThreadsVariables getVariables() {
        return Variables;
    }

    public void setVariables(HotThreadsVariables variables) {
        Variables = variables;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getCharset() {
        return Charset;
    }

    public void setCharset(String charset) {
        Charset = charset;
    }

    public static HotThreadsObject parseJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, HotThreadsObject.class);
    }
}
