package org.succlz123.s1go.app.dao.Api;

import android.widget.Toast;
import com.squareup.okhttp.*;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.login.LoginObject;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.bean.setreviews.SetReviewsObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by fashi on 2015/4/12.
 */
public class OkHttpApi {
    private static OkHttpApi instance;

    public synchronized static OkHttpApi getInstance() {
        if (instance == null) {
            instance = new OkHttpApi();
        }
        return instance;
    }

    public String doGet(String adress) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(adress).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //登录
    public void login(String adress, HashMap<String, String> params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(adress);
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
    public String loginDoGet(String adress, HashMap<String, String> hearders) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(adress);
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
    //回帖
    public String setReviews(String adress, HashMap<String, String> hearders, HashMap<String, String> body) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(adress);

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
                SetReviewsObject setReviewsObject = SetReviewsObject.parseJson(json);
                if (setReviewsObject.getMessage().getMessageval().equals("post_reply_succeed")) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
