package org.succlz123.s1go.app.bean.setreviews;

import com.google.gson.Gson;

/**
 * Created by fashi on 2015/4/20.
 */
public class SetReviewsObject {
    private SetReviewsMessage Message;

    public SetReviewsMessage getMessage() {
        return Message;
    }

    public void setMessage(SetReviewsMessage message) {
        Message = message;
    }

    public static SetReviewsObject parseJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, SetReviewsObject.class);
    }
}
