package org.succlz123.s1go.app.bean.hostpost;

import com.google.gson.Gson;

/**
 * Created by fashi on 2015/4/12.
 */
public class HotPostObject {
    private String Version;
    private String Charset;
    private HotPostVariables Variables;

    public HotPostVariables getVariables() {
        return Variables;
    }

    public void setVariables(HotPostVariables variables) {
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

    public static HotPostObject parseJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, HotPostObject.class);
    }
}
