package org.succlz123.s1go.app.config;

import org.succlz123.s1go.app.MainApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by succlz123 on 15/9/15.
 */
public class OkHttpClientManager {
    private static final String TAG = OkHttpClientManager.class.getName();
    protected OkHttpClient okHttpClient;

    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 30 * 1024 * 1024;

    private OkHttpClientManager() {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder();
            okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
            okHttpClientBuilder.readTimeout(15, TimeUnit.SECONDS);

            File baseDir = MainApplication.getInstance().getApplicationContext().getCacheDir();
            if (baseDir != null) {
                final File cacheDir = new File(baseDir, "HttpResponseCache");
                okHttpClientBuilder.cache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
            }

//            okHttpClientBuilder.networkInterceptors().add(new StethoInterceptor());

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
