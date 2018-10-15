package org.succlz123.s1go.deprecated;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by succlz123 on 2015/4/12.
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
        } finally {

        }
        return null;
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
//        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
//        for (String key : bodyKeys) {
//            formEncodingBuilder.add(key, body.get(key));
//        }
//
//        builder.post(formEncodingBuilder.build());
//        Request request = builder.build();
//        try {
//            Response response = okHttpClient.newCall(request).execute();
//            if (response.isSuccessful()) {
//                response.body().charStream();
//                String json = response.body().string();
//                SendInfo setThreadsAndReviewsObject = SendInfo.parseJson(json);
//
//                return json;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
            return null;

//        return null;
    }
}
