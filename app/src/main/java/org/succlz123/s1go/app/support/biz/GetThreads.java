package org.succlz123.s1go.app.support.biz;

import org.succlz123.s1go.app.support.bean.threads.ThreadsObject;
import org.succlz123.s1go.app.support.utils.S1Url;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/14.
 */
public class GetThreads {

	public static ThreadsObject getThreads(String fid, int page, HashMap<String, String> paramss) {
		String url = S1Url.S1_BASE + S1Url.GET_THREADS.replace("fid=", "fid=" + fid);
		url = url.replace("page=", "page=" + page);
		String json = MyOkHttp.getInstance().doGet(url, paramss);
		ThreadsObject threadsObject = ThreadsObject.parseJson(json);
		return threadsObject;
	}
}
