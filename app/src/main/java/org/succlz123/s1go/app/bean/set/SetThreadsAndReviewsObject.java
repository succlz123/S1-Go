package org.succlz123.s1go.app.bean.set;

import com.google.gson.Gson;

/**
 * Created by fashi on 2015/4/20.
 */
public class SetThreadsAndReviewsObject {
	private SetThreadsAndReviewsMessage Message;

	public SetThreadsAndReviewsMessage getMessage() {
		return Message;
	}

	public void setMessage(SetThreadsAndReviewsMessage message) {
		Message = message;
	}

	public static SetThreadsAndReviewsObject parseJson(String json) {
		Gson gson = new Gson();

		return gson.fromJson(json, SetThreadsAndReviewsObject.class);
	}
}
