package org.succlz123.s1go.app.bean.forum;

import java.util.List;

/**
 * Created by fashi on 2015/4/14.
 */
public class ForumVariables {
    private ForumForum forum;
    private List<ForumForumThreadlist> forum_threadlist;
    private int page;//页数
    private int tpp;//返回帖子数

    public List<ForumForumThreadlist> getForum_threadlist() {
        return forum_threadlist;
    }

    public void setForum_threadlist(List<ForumForumThreadlist> forum_threadlist) {
        this.forum_threadlist = forum_threadlist;
    }

    public ForumForum getForum() {
        return forum;

    }

    public void setForum(ForumForum forum) {
        this.forum = forum;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTpp() {
        return tpp;
    }

    public void setTpp(int tpp) {
        this.tpp = tpp;
    }
}
