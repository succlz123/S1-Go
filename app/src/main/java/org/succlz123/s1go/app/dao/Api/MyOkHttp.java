package org.succlz123.s1go.app.dao.Api;

import android.widget.Toast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.login.LoginObject;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.bean.send.SetThreadsAndReviewsObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by fashi on 2015/4/12.
 */
public class MyOkHttp {
    private static MyOkHttp instance;

    public synchronized static MyOkHttp getInstance() {
        if (instance == null) {
            instance = new MyOkHttp();
        }
        return instance;
    }

    private MyOkHttp() {

    }

    /*图片下载*/
    public InputStream doImageGet(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().byteStream();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        return null;
    }

    //登录
    public void login(String url, HashMap<String, String> params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(url);
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            formEncodingBuilder.add(key, params.get(key));
        }
        builder.post(formEncodingBuilder.build());
        Request request = builder.build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                response.body().charStream();
                String json = response.body().string();
                LoginObject loginObject = LoginObject.parseJson(json);
                LoginVariables loginVariables = loginObject.getVariables();
                final String messagestr = loginObject.getMessage().getMessagestr();
                String messageval = loginObject.getMessage().getMessageval();
                if ((messageval.equals("location_login_succeed_mobile"))) {
                    S1GoApplication.getInstance().addUserInfo(loginVariables);
                } else if (messageval.equals("login_invalid")) {
                    S1GoApplication.getInstance().runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(S1GoApplication.getInstance(), messagestr, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //用auth和saltkey验证登录 获取有权限的板块
    public String doGet(String url, HashMap<String, String> hearders) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(url);
        Set<String> keys = hearders.keySet();
        for (String key : keys) {
            builder.header(key, hearders.get(key));
        }
        Request request = builder.build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // hearders 头文件 auth saltkey
    // body 内容文件 formhash allownoticeauthor message mobiletype
    //发帖 回帖
    public String SetThreadsAndReviews(String url, int i, HashMap<String, String> hearders, HashMap<String, String> body) {
        final int SETTHREADS = 0;
        final int SETREVIEWS = 1;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(url);
        Set<String> heardersKeys = hearders.keySet();
        for (String key : heardersKeys) {
            builder.header(key, hearders.get(key));
        }

        Set<String> bodyKeys = body.keySet();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        for (String key : bodyKeys) {
            formEncodingBuilder.add(key, body.get(key));
        }

        builder.post(formEncodingBuilder.build());
        Request request = builder.build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                response.body().charStream();
                String json = response.body().string();
                SetThreadsAndReviewsObject setThreadsAndReviewsObject = SetThreadsAndReviewsObject.parseJson(json);
                switch (i) {
                    case SETTHREADS:
                        if (setThreadsAndReviewsObject.getMessage().getMessageval().equals("post_newthread_succeed")) {
                            S1GoApplication.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(S1GoApplication.getInstance(), "非常感谢，帖子发布成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            S1GoApplication.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(S1GoApplication.getInstance(), "非常遗憾，帖子发布失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                    case SETREVIEWS:
                        if (setThreadsAndReviewsObject.getMessage().getMessageval().equals("post_reply_succeed")) {
                            S1GoApplication.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(S1GoApplication.getInstance(), "非常感谢，回复发布成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            S1GoApplication.getInstance().runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(S1GoApplication.getInstance(), "非常遗憾，回复发布失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                }
                return json;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
