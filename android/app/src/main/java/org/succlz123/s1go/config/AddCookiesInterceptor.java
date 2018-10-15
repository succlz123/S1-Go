package org.succlz123.s1go.config;

import org.succlz123.s1go.MainApplication;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by succlz123 on 2016/11/27.
 */
public class AddCookiesInterceptor implements Interceptor {
    private Context context;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
//        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
//        Observable.just(sharedPreferences.getString("cookie", ""))
        Observable.just(MainApplication.getInstance().getCookie())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String cookie) {
                        builder.addHeader("Cookie", cookie);
                    }
                });
        return chain.proceed(builder.build());
    }
}
