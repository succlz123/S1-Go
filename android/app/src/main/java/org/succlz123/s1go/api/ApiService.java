package org.succlz123.s1go.api;

import org.succlz123.s1go.bean.HotPost;
import org.succlz123.s1go.bean.SendInfo;
import org.succlz123.s1go.bean.ThreadInfo;
import org.succlz123.s1go.bean.ThreadList;
import org.succlz123.s1go.bean.UserInfo;

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
    @POST("index.php?mobile=no&module=login&loginsubmit=yes&loginfield=auto&submodule=checkpost")
    Observable<UserInfo> login(@Field("username") String username, @Field("password") String password);

    /**
     * 热门帖子
     */
    @Headers("Cache-Control: public, max-age=300")
    @GET("index.php?mobile=no&module=hotthread&orderby=dateline")
    Observable<HotPost> getHot();

    /**
     * 帖子列表
     */
    @GET("index.php?mobile=no&module=forumdisplay&orderby=dateline&submodule=checkpost&tpp=60")
    Observable<ThreadList> getThreadList(@Query("page") int page, @Query("fid") String fid);

    /**
     * 帖子内容
     */
    @GET("index.php?&module=viewthread&orderby=dateline&submodule=checkpost&ppp=30")
    Observable<ThreadInfo> getThreadInfo(@Query("page") int page, @Query("tid") String tid);

    /**
     * 回复
     */
    @FormUrlEncoded
    @POST("index.php?mobile=no&module=sendreply&replysubmit=yes&tid=")
    Observable<SendInfo> sendReply(@Query("tid") String tid, @Field("formhash") String formhash, @Field("message") String message);

    /**
     * 发帖
     */
    @FormUrlEncoded
    @POST("index.php?mobile=no&module=newthread&topicsubmit=yes&fid=")
    Observable<SendInfo> sendNewThread(@Query("fid") String fid, @Field("formhash") String formhash, @Field("subject") String subject, @Field("message") String message);
}
