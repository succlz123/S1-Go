package org.succlz123.s1go.app.dao.Api;

import org.succlz123.s1go.app.bean.hostpost.HotPostObject;
import org.succlz123.s1go.app.dao.Helper.S1UrlHelper;

/**
 * Created by fashi on 2015/4/12.
 */
public class HostPostApi {

    public static HotPostObject getHostPost() {
        String url = S1UrlHelper.HOT_POST;
        String json = OkHttpApi.getInstance().doGet(url);
        HotPostObject result = HotPostObject.parseJson(json);
        return result;
    }
}
