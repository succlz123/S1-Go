package org.succlz123.s1go.app;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.succlz123.s1go.app.api.bean.LoginInfo;
import org.succlz123.s1go.app.database.UserDatabase;
import org.succlz123.s1go.app.ui.login.LoginInfoListener;
import org.succlz123.s1go.app.utils.s1.S1Emoticon;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fashi on 2015/4/13.
 */
public class MainApplication extends Application {
    public LoginInfo.VariablesEntity loginInfo;
    public RefWatcher refWatcher;

    private static MainApplication sInstance;

    private List<LoginInfoListener> mLoginListenerList = new ArrayList<LoginInfoListener>();

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
    }

    public void addUserListener(LoginInfoListener LoginInfoListener) {
        this.mLoginListenerList.add(LoginInfoListener);
    }

    public LoginInfo.VariablesEntity getLoginInfo() {
        if (loginInfo == null) {
            loginInfo = UserDatabase.getInstance().execSelect();
        }
        return loginInfo;
    }

    public void addLoginInfo(LoginInfo userInfo) {
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
}
