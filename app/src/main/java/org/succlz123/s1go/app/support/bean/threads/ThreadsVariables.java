package org.succlz123.s1go.app.support.bean.threads;

import java.util.List;

/**
 * Created by fashi on 2015/4/14.
 */
public class ThreadsVariables {
	private ForumInfo forum;
	private String formhash;
	private List<ThreadsList> forum_threadlist;
	private int page;//页数
	private int tpp;//返回帖子数

	public String getFormhash() {
		return formhash;
	}

	public void setFormhash(String formhash) {
		this.formhash = formhash;
	}

	public List<ThreadsList> getForum_threadlist() {
		return forum_threadlist;
	}

	public void setForum_threadlist(List<ThreadsList> forum_threadlist) {
		this.forum_threadlist = forum_threadlist;
	}

	public ForumInfo getForum() {
		return forum;

	}

	public void setForum(ForumInfo forumInfo) {
		this.forum = forumInfo;
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
