package org.succlz123.s1go.app.support.biz;

import org.succlz123.s1go.app.support.bean.reviews.ReviewsObject;
import org.succlz123.s1go.app.support.utils.S1Url;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/15.
 */
public class GetReviews {

	public static ReviewsObject getReviews(String tid, int pagerNum, HashMap<String, String> hearders) {
		String url = S1Url.S1_BASE + S1Url.GET_REVIEWS.replace("tid=", "tid=" + tid);
		url = url.replace("page=", "page=" + pagerNum);
		String json = MyOkHttp.getInstance().doGet(url, hearders);
		ReviewsObject reviewsObject = ReviewsObject.parseJson(json);

		return reviewsObject;
	}
}
