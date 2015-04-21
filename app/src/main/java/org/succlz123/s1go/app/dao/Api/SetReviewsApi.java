package org.succlz123.s1go.app.dao.Api;

import org.succlz123.s1go.app.bean.setreviews.SetReviewsObject;
import org.succlz123.s1go.app.dao.Helper.S1UrlHelper;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/20.
 */
public class SetReviewsApi {

    public static SetReviewsObject SetReviews(String tid, HashMap<String, String> hearders, HashMap<String, String> body) {
        String url_get = S1UrlHelper.SET_REVIEWS_GET;
        String url_post = S1UrlHelper.SET_REVIEWS_POST.replace("tid=", "tid=" + tid);
        OkHttpApi.getInstance().loginDoGet(url_get,hearders);
        String json = OkHttpApi.getInstance().setReviews(url_post, hearders,body);
        SetReviewsObject setReviewsObject = SetReviewsObject.parseJson(json);
        return setReviewsObject;
    }
}
