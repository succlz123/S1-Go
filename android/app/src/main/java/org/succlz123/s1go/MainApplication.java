package org.succlz123.s1go;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import org.qiibeta.bitmapview.image.TileImage;
import org.succlz123.blockanalyzer.BlockAnalyzer;
import org.succlz123.crash.CrashHelper;
import org.succlz123.s1go.bean.UserInfo;
import org.succlz123.s1go.config.RetrofitManager;
import org.succlz123.s1go.database.UserDatabase;
import org.succlz123.s1go.utils.UserInfoChangeListener;
import org.succlz123.s1go.utils.image.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.flutter.app.FlutterApplication;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by succlz123 on 2015/4/13.
 */
public class MainApplication extends FlutterApplication {
    public UserInfo.Variables loginInfo;
    public RefWatcher refWatcher;

    private static MainApplication sInstance;

    private List<UserInfoChangeListener> mLoginListenerList = new ArrayList<UserInfoChangeListener>();
    private Handler mHandler = new Handler();

    public static MainApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        sInstance = this;
        registerActivityLifecycleCallbacks(new MainActivityLifecycleCallback());
        refWatcher = LeakCanary.install(this);

        ImagePipelineConfig build = ImagePipelineConfig.newBuilder(this).setDownsampleEnabled(true).build();
        Fresco.initialize(this, build);
        CrashHelper.init(this);

        ImageLoader.init();

        Observable.fromCallable(() -> {
//                S1Emoticon.initEmoticon();
            CrashReport.initCrashReport(MainApplication.getInstance(), "900017373", BuildConfig.DEBUG);
            return getUserInfo();
        }).subscribeOn(Schedulers.io()).subscribe(variables -> {
            if (variables != null) {
                Observable<UserInfo> observable = RetrofitManager.apiService().login(variables.member_username, variables.password);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(loginInfo -> {
                            String messageVal = loginInfo.Message.messageval;
                            if ((TextUtils.equals(messageVal, BuildConfig.LOGIN_SUCCEED))) {
                                loginInfo.Variables.password = getUserInfo().password;
                                MainApplication.getInstance().addUserInfo(loginInfo);
                            } else if ((TextUtils.equals(messageVal, BuildConfig.LOGIN_FAILED))) {
                                logout();
                            }
                        }, throwable -> logout());
            }
        });
        BlockAnalyzer.install(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        TileImage.clearBitmapRecyclePool();
        ImageLoader.getInstance().clearMemoryCache();
    }

    // So sadly! This method will never be called in production device
    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstance = null;
        ImageLoader.getInstance().shutDown();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void addUserListener(UserInfoChangeListener UserInfoChangeListener) {
        this.mLoginListenerList.add(UserInfoChangeListener);
    }

    public UserInfo.Variables getUserInfo() {
        if (loginInfo == null) {
            loginInfo = UserDatabase.getInstance().execSelect();
        }
        return loginInfo;
    }

    public void addUserInfo(UserInfo userInfo) {
        this.loginInfo = userInfo.Variables;
        UserDatabase.getInstance().execInsert(userInfo);
        for (UserInfoChangeListener UserInfoChangeListener : mLoginListenerList) {
            UserInfoChangeListener.onChanged(MainApplication.this.loginInfo);
        }
    }

    public void logout() {
        UserDatabase.getInstance().execDelete();
        loginInfo = null;
    }

    public String getCookie() {
        UserInfo.Variables userInfo = MainApplication.getInstance().getUserInfo();
        if (userInfo == null) {
            return "";
        }
        String cookie = userInfo.cookiepre;
        String auth = BuildConfig.AUTH + "=" + Uri.encode(userInfo.auth);
        String saltKey = BuildConfig.SALT_KEY + "=" + userInfo.saltkey;
        return cookie + auth + ";" + cookie + saltKey + ";";
    }

    private static class MainActivityLifecycleCallback implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
