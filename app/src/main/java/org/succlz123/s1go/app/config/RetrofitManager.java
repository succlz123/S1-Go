package org.succlz123.s1go.app.config;


import org.succlz123.s1go.app.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by succlz123 on 15/8/14.
 */
public class RetrofitManager {
    public static final String BASE = "http://bbs.saraba1st.com/2b/api/mobile/";

    public static final String LOGIN = "http://bbs.saraba1st.com/2b/api/mobile/" +
            "index.php?mobile=no&version=1&module=login&loginsubmit=yes&loginfield=auto&submodule=checkpost";
    public static final String SET_GET = "http://bbs.saraba1st.com/2b/api/mobile/" +
            "index.php?mobile=no&version=1&module=secure&type=post";

    private Retrofit.Builder mBuilder;

    private RetrofitManager() {
        if (mBuilder == null) {
            mBuilder = new Retrofit.Builder();
        }
    }

    public static Retrofit.Builder getInstance() {
        return HelpHolder.INSTANCE.mBuilder;
    }

    private static class HelpHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    private static Retrofit getRetrofit(String url) {
        return RetrofitManager.getInstance()
                .client(OkHttpClientManager.getInstance().okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    public static Retrofit getRetrofit() {
        return getRetrofit(BASE);
    }

    public static ApiService apiService() {
        return RetrofitManager.getRetrofit().create(ApiService.class);
    }
}
