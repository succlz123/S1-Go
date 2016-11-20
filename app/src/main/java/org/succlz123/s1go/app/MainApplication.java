package org.succlz123.s1go.app;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import org.qiibeta.bitmapview.image.TileImage;
import org.succlz123.s1go.app.api.bean.UserInfo;
import org.succlz123.s1go.app.database.UserDatabase;
import org.succlz123.s1go.app.ui.login.LoginInfoListener;
import org.succlz123.s1go.app.utils.ThemeHelper;
import org.succlz123.s1go.app.utils.image.ImageLoader;
import org.succlz123.s1go.app.utils.s1.S1Emoticon;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by succlz123 on 2015/4/13.
 */
public class MainApplication extends Application implements ThemeUtils.switchColor {
    public UserInfo.Variables loginInfo;
    public RefWatcher refWatcher;

    private static MainApplication sInstance;

    private List<LoginInfoListener> mLoginListenerList = new ArrayList<LoginInfoListener>();
    private Handler mHandler = new Handler();

    public static MainApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
        Fresco.initialize(this);
        S1Emoticon.initEmoticon();
        ImageLoader.init();
        ThemeUtils.setSwitchColor(this);
        CrashReport.initCrashReport(this, "900017373", BuildConfig.DEBUG);

        //        LoginVariables loginInfo = MainApplication.getInstance().loginInfo;
//        if (loginInfo != null) {
//            String cookie = loginInfo.getCookiepre();
//            String auth = S1GoConfig.AUTH + "=" + Uri.encode(loginInfo.getAuth());
//            String saltKey = S1GoConfig.SALT_KEY + "=" + loginInfo.getSaltkey();
//            this.mHearders.put(S1GoConfig.COOKIE, cookie + auth + ";" + cookie + saltKey + ";");
//        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        TileImage.clearBitmapRecyclePool();
    }

    public void addUserListener(LoginInfoListener LoginInfoListener) {
        this.mLoginListenerList.add(LoginInfoListener);
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
        for (LoginInfoListener LoginInfoListener : mLoginListenerList) {
            LoginInfoListener.onChanged(MainApplication.this.loginInfo);
        }
    }

    public void logout() {
        UserDatabase.getInstance().execDelete();
        loginInfo = null;
    }

    public void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
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
