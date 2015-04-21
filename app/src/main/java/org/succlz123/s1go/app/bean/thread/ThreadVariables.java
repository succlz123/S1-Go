package org.succlz123.s1go.app.bean.thread;

import java.util.List;

/**
 * Created by fashi on 2015/4/15.
 */
public class ThreadVariables {
    private List<ThreadPostlist> postlist;
    private ThreadThread thread;
    private String formhash;

    public String getFormhash() {
        return formhash;
    }

    public void setFormhash(String formhash) {
        this.formhash = formhash;
    }

    public ThreadThread getThread() {
        return thread;
    }

    public void setThread(ThreadThread thread) {
        this.thread = thread;
    }

    public List<ThreadPostlist> getPostlist() {
        return postlist;
    }

    public void setPostlist(List<ThreadPostlist> postlist) {
        this.postlist = postlist;
    }
}
