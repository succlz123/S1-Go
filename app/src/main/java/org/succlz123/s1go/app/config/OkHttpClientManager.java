package org.succlz123.s1go.app.config;

import org.succlz123.s1go.app.MainApplication;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by succlz123 on 15/9/15.
 */
public class OkHttpClientManager {
    private static final String TAG = "OkHttpClientManager";
    protected OkHttpClient okHttpClient;

    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 30 * 1024 * 1024;

    private OkHttpClientManager() {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder();
            okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
            okHttpClientBuilder.readTimeout(15, TimeUnit.SECONDS);

            Context context = MainApplication.getContext();
            File baseDir = context.getCacheDir();
            if (baseDir != null) {
                final File cacheDir = new File(baseDir, "HttpResponseCache");
                okHttpClientBuilder.cache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
            }

            okHttpClientBuilder.addNetworkInterceptor(new AddCookiesInterceptor(context));
            okHttpClientBuilder.addNetworkInterceptor(new ReceivedCookiesInterceptor(context));
            okHttpClient = okHttpClientBuilder.build();
        }
    }

    public static OkHttpClientManager getInstance() {
        return HelpHolder.INSTANCE;
    }

    private static class HelpHolder {
        private static final OkHttpClientManager INSTANCE = new OkHttpClientManager();
    }
}
