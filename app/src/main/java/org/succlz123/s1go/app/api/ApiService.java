package org.succlz123.s1go.app.api;

import org.succlz123.s1go.app.api.bean.HotPost;
import org.succlz123.s1go.app.api.bean.LoginInfo;
import org.succlz123.s1go.app.api.bean.ThreadInfo;
import org.succlz123.s1go.app.api.bean.ThreadList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by succlz123 on 16/4/12.
 */
public interface ApiService {

    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST("index.php?mobile=no&version=1&module=login&loginsubmit=yes&loginfield=auto&submodule=checkpost")
    Observable<LoginInfo> login(@Field("username") String username, @Field("password") String password);

    /**
     * 热门帖子
     */
    @Headers("Cache-Control: public, max-age=300")
    @GET("index.php?mobile=no&version=1&module=hotthread&orderby=dateline")
    Observable<HotPost> getHot();

    /**
     * 帖子列表
     */
    @GET("index.php?mobile=no&version=1&module=forumdisplay&orderby=dateline&submodule=checkpost&tpp=100")
    Observable<ThreadList> getThreadList(@Query("page") int page, @Query("fid") String fid);

    /**
     * 帖子内容
     */
    @GET("index.php?&module=viewthread&orderby=dateline&submodule=checkpost&ppp=30")
    Observable<ThreadInfo> getThreadInfo(@Query("page") int page, @Query("tid") String fid);

    /**
     * 回复
     */
    @GET("index.php?mobile=no&version=1&module=sendreply&replysubmit=yes&tid=")
    Observable<ThreadInfo> sendReply(@Query("page") int page, @Query("tid") String fid);

    /**
     * 发帖
     */
    @GET("index.php?mobile=no&version=1&module=newthread&topicsubmit=yes&fid=")
    Observable<ThreadInfo> sendNewThread(@Query("page") int page, @Query("tid") String fid);

    //	switch (i) {
//                    case SETTHREADS:
//                        if (setThreadsAndReviewsObject.getMessage().getMessageval().equals("post_newthread_succeed")) {
//                            MainApplication.getInstance().runOnUIThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(MainApplication.getInstance(), "非常感谢，帖子发布成功", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        } else {
//                            MainApplication.getInstance().runOnUIThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(MainApplication.getInstance(), "非常遗憾，帖子发布失败", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                        break;
//                    case SETREVIEWS:
//                        if (setThreadsAndReviewsObject.getMessage().getMessageval().equals("post_reply_succeed")) {
//                            MainApplication.getInstance().runOnUIThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(MainApplication.getInstance(), "非常感谢，回复发布成功", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        } else {
//                            MainApplication.getInstance().runOnUIThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(MainApplication.getInstance(), "非常遗憾，回复发布失败", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                        break;
//                }
}
