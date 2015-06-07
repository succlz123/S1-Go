package org.succlz123.s1go.app.support.utils;

/**
 * Created by fashi on 2015/4/12.
 */
public class S1Url {

	public static final String HOT_POST = "http://bbs.saraba1st.com/2b/api/mobile/index.php?mobile=no&version=1&module=hotthread";

	public static final String S1_BASE = "http://bbs.saraba1st.com/2b/api/mobile/index.php?mobile=no&version=1";
	public static final String FORUM_TITLE = "&module=forumdisplay&submodule=checkpost&tpp=100&fid=";
//	public static final String FORUM_TITLE = "&module=forumdisplay&submodule=checkpost&fid=&page=1&tpp=50&orderby=dateline";
	public static final String THREAD_POST = "&module=viewthread&submodule=checkpost&tid=&ppp=30&page=1";

	public static final String USER_AVATAR = "http://bbs.saraba1st.com/2b/uc_server/avatar.php?uid=&size=small";
	public static final String AVATAR_SMALL = "http://bbs.saraba1st.com/2b/uc_server/data/avatar/000/00/00/00_avatar_small.jpg";
	public static final String AVATAR_MIDDLE = "http://bbs.saraba1st.com/2b/uc_server/data/avatar/000/00/00/00_avatar_middle.jpg";

	public static final String LOGIN = "http://bbs.saraba1st.com/2b/api/mobile/index.php?mobile=no&version=1&module=login&loginsubmit=yes&loginfield=auto&submodule=checkpost";

	public static final String SET_GET = "http://bbs.saraba1st.com/2b/api/mobile/index.php?mobile=no&version=1&module=secure&type=post";
	public static final String SET_REVIEWS = "http://bbs.saraba1st.com/2b/api/mobile/index.php?mobile=no&version=1&module=sendreply&replysubmit=yes&tid=";

	public static final String SET_THREADS = "http://bbs.saraba1st.com/2b/api/mobile/index.php?mobile=no&version=1&module=newthread&topicsubmit=yes&fid=";
}
