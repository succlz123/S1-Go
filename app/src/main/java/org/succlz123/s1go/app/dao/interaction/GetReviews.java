package org.succlz123.s1go.app.dao.interaction;

import org.succlz123.s1go.app.bean.reviews.ReviewsObject;
import org.succlz123.s1go.app.dao.api.MyOkHttp;
import org.succlz123.s1go.app.dao.helper.S1Url;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/15.
 */
public class GetReviews {

    public static ReviewsObject getReviews(String tid,HashMap<String, String> hearders) {
        String url = S1Url.S1_BASE + S1Url.THREAD_POST.replace("tid=", "tid=" + tid);
        String json = MyOkHttp.getInstance().doGet(url, hearders);
        ReviewsObject reviewsObject = ReviewsObject.parseJson(json);
        return reviewsObject;
    }
}
