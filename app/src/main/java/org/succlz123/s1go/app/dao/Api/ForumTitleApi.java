package org.succlz123.s1go.app.dao.Api;

import org.succlz123.s1go.app.bean.forum.ForumObject;
import org.succlz123.s1go.app.dao.Helper.S1UrlHelper;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/14.
 */
public class ForumTitleApi {

    public static ForumObject getForumTitle(String fid, HashMap<String, String> paramss) {
        String url = S1UrlHelper.S1_BASE + S1UrlHelper.FORUM_TITLE.replace("fid=", "fid=" + fid);
        String json = OkHttpApi.getInstance().loginDoGet(url, paramss);
        ForumObject forumObject = ForumObject.parseJson(json);
        return forumObject;
    }
}
