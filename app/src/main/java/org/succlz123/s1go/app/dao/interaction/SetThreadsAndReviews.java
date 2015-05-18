package org.succlz123.s1go.app.dao.interaction;

import org.succlz123.s1go.app.bean.set.SetThreadsAndReviewsObject;
import org.succlz123.s1go.app.dao.api.MyOkHttp;
import org.succlz123.s1go.app.dao.helper.S1Url;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/20.
 */
public class SetThreadsAndReviews {

	public static SetThreadsAndReviewsObject SetThreads(String mFid, HashMap<String, String> mHearders, HashMap<String, String> mBody) {
		String url_get = S1Url.SET_GET;
		String url_setThreads = S1Url.SET_THREADS.replace("fid=", "fid=" + mFid);
		MyOkHttp.getInstance().doGet(url_get, mHearders);
		String json = MyOkHttp.getInstance().SetThreadsAndReviews(url_setThreads, 0, mHearders, mBody);
		SetThreadsAndReviewsObject setThreadsAndReviewsObject = SetThreadsAndReviewsObject.parseJson(json);
		return setThreadsAndReviewsObject;
	}

	public static SetThreadsAndReviewsObject SetReviews(String mTid, HashMap<String, String> mHearders, HashMap<String, String> mBody) {
		String url_get = S1Url.SET_GET;
		String url_setReviews = S1Url.SET_REVIEWS.replace("tid=", "tid=" + mTid);
		MyOkHttp.getInstance().doGet(url_get, mHearders);
		String json = MyOkHttp.getInstance().SetThreadsAndReviews(url_setReviews, 1, mHearders, mBody);
		SetThreadsAndReviewsObject setThreadsAndReviewsObject = SetThreadsAndReviewsObject.parseJson(json);
		return setThreadsAndReviewsObject;
	}
}
