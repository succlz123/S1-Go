package org.succlz123.s1go.app.support.bean.reviews;

import com.google.gson.Gson;

/**
 * Created by fashi on 2015/4/15.
 */
public class ReviewsObject {
    private ReviewsVariables Variables;

    public ReviewsVariables getVariables() {
        return Variables;
    }

    public void setVariables(ReviewsVariables variables) {
        Variables = variables;
    }

    public static ReviewsObject parseJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, ReviewsObject.class);
    }
}
