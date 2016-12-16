package org.succlz123.s1go.app;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import org.qiibeta.bitmapview.image.TileImage;
import org.succlz123.blockanalyzer.BlockAnalyzer;
import org.succlz123.s1go.app.bean.UserInfo;
import org.succlz123.s1go.app.config.RetrofitManager;
import org.succlz123.s1go.app.database.UserDatabase;
import org.succlz123.s1go.app.utils.ThemeHelper;
import org.succlz123.s1go.app.utils.UserInfoChangeListener;
import org.succlz123.s1go.app.utils.image.ImageLoader;
import org.succlz123.s1go.app.utils.s1.S1Emoticon;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by succlz123 on 2015/4/13.
 */
public class MainApplication extends Application implements ThemeUtils.switchColor {
    public UserInfo.Variables loginInfo;
    public RefWatcher refWatcher;

    private static MainApplication sInstance;

    private List<UserInfoChangeListener> mLoginListenerList = new ArrayList<UserInfoChangeListener>();
    private Handler mHandler = new Handler();

    public static MainApplication getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return MainApplication.getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        sInstance = this;
        refWatcher = LeakCanary.install(this);
        Fresco.initialize(this);
        ImageLoader.init();
        ThemeUtils.setSwitchColor(this);

        Observable.fromCallable(new Callable<UserInfo.Variables>() {
            @Override
            public UserInfo.Variables call() throws Exception {
                S1Emoticon.initEmoticon();
                CrashReport.initCrashReport(MainApplication.getContext(), "900017373", BuildConfig.DEBUG);
                return getUserInfo();
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Action1<UserInfo.Variables>() {
            @Override
            public void call(UserInfo.Variables variables) {
                if (variables != null) {
                    Observable<UserInfo> observable = RetrofitManager.apiService().login(variables.member_username, variables.password);
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<UserInfo>() {
                                @Override
                                public void call(UserInfo loginInfo) {
                                    String messageVal = loginInfo.Message.messageval;
                                    if ((TextUtils.equals(messageVal, BuildConfig.LOGIN_SUCCEED))) {
                                        loginInfo.Variables.password = getUserInfo().password;
                                        MainApplication.getInstance().addUserInfo(loginInfo);
                                    } else if ((TextUtils.equals(messageVal, BuildConfig.LOGIN_FAILED))) {
                                        logout();
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    logout();
                                }
                            });
                }
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

    @Override
    public int replaceColorById(Context context, @ColorRes int colorId) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return context.getResources().getColor(colorId);
        }
        String theme = getTheme(context);
        if (theme != null) {
            colorId = getThemeColorId(context, colorId, theme);
        }
        return context.getResources().getColor(colorId);
    }

    @Override
    public int replaceColor(Context context, @ColorInt int color) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return color;
        }
        String theme = getTheme(context);
        int colorId = -1;

        if (theme != null) {
            colorId = getThemeColor(context, color, theme);
        }
        return colorId != -1 ? getResources().getColor(colorId) : color;
    }

    private String getTheme(Context context) {
        if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_STORM) {
            return "blue";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_HOPE) {
            return "purple";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_WOOD) {
            return "green";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_LIGHT) {
            return "green_light";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_THUNDER) {
            return "yellow";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAND) {
            return "orange";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_FIREY) {
            return "red";
        }
        return null;
    }

    @ColorRes
    private int getThemeColorId(Context context, int colorId, String theme) {
        switch (colorId) {
            case R.color.theme_color_primary:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case R.color.theme_color_primary_dark:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case R.color.theme_color_primary_trans:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
        }
        return colorId;
    }


    @ColorRes
    private int getThemeColor(Context context, int color, String theme) {
        switch (color) {
            case 0xfffb7299:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case 0xffb85671:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case 0x99f0486c:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
        }
        return -1;
    }
}
