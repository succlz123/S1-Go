package org.succlz123.s1go.app.dao.Api;

import org.succlz123.s1go.app.bean.thread.ThreadObject;
import org.succlz123.s1go.app.dao.Helper.S1UrlHelper;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/15.
 */
public class ReviewsApi {

    public static ThreadObject getReviews(String tid,HashMap<String, String> hearders) {
        String url = S1UrlHelper.S1_BASE + S1UrlHelper.THREAD_POST.replace("tid=", "tid=" + tid);
        String json = OkHttpApi.getInstance().loginDoGet(url,hearders);
        ThreadObject threadObject = ThreadObject.parseJson(json);
        return threadObject;
    }
}
