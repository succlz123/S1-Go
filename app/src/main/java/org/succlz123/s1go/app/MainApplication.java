package org.succlz123.s1go.app;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.succlz123.s1go.app.api.bean.UserInfo;
import org.succlz123.s1go.app.database.UserDatabase;
import org.succlz123.s1go.app.ui.login.LoginInfoListener;
import org.succlz123.s1go.app.utils.image.ImageLoader;
import org.succlz123.s1go.app.utils.s1.S1Emoticon;

import android.app.Application;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by succlz123 on 2015/4/13.
 */
public class MainApplication extends Application {
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
        refWatcher = LeakCanary.install(this);
        Fresco.initialize(this);
        S1Emoticon.initEmoticon();
        ImageLoader.init();
        //        LoginVariables loginInfo = MainApplication.getInstance().loginInfo;
//        if (loginInfo != null) {
//            String cookie = loginInfo.getCookiepre();
//            String auth = S1GoConfig.AUTH + "=" + Uri.encode(loginInfo.getAuth());
//            String saltKey = S1GoConfig.SALT_KEY + "=" + loginInfo.getSaltkey();
//            this.mHearders.put(S1GoConfig.COOKIE, cookie + auth + ";" + cookie + saltKey + ";");
//        }
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

    public void removeUser() {
        UserDatabase.getInstance().execDelete();
        loginInfo = null;
    }

    public void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

}
