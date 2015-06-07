package org.succlz123.s1go.app.support.bean.reviews;

import java.util.List;

/**
 * Created by fashi on 2015/4/15.
 */
public class ReviewsVariables {
    private List<ReviewsList> postlist;
    private ThreadInfo thread;
    private String formhash;

    public String getFormhash() {
        return formhash;
    }

    public void setFormhash(String formhash) {
        this.formhash = formhash;
    }

    public ThreadInfo getThread() {
        return thread;
    }

    public void setThread(ThreadInfo thread) {
        this.thread = thread;
    }

    public List<ReviewsList> getPostlist() {
        return postlist;
    }

    public void setPostlist(List<ReviewsList> postlist) {
        this.postlist = postlist;
    }
}
