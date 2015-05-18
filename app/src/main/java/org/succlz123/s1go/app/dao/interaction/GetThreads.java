package org.succlz123.s1go.app.dao.interaction;

import org.succlz123.s1go.app.bean.threads.ThreadsObject;
import org.succlz123.s1go.app.dao.api.MyOkHttp;
import org.succlz123.s1go.app.dao.helper.S1Url;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/14.
 */
public class GetThreads {

	public static ThreadsObject getThreads(String fid, HashMap<String, String> paramss) {
		String url = S1Url.S1_BASE + S1Url.FORUM_TITLE.replace("fid=", "fid=" + fid);
		String json = MyOkHttp.getInstance().doGet(url, paramss);
		ThreadsObject threadsObject = ThreadsObject.parseJson(json);
		return threadsObject;
	}
}
