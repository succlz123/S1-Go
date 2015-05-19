package org.succlz123.s1go.app;

import android.app.Application;
import android.os.Handler;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.dao.database.S1UserDB;
import org.succlz123.s1go.app.dao.helper.S1FidIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fashi on 2015/4/13.
 */
public class S1GoApplication extends Application {
    public LoginVariables mUserInfo;
    private UserListener mUserListener;
    private Handler mHandler = new Handler();
    private List<UserListener> mUserListenerList = new ArrayList<UserListener>();

    private static S1GoApplication instance;

    public static S1GoApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //获得论坛板块名图标
        S1FidIcon.getS1FidImg();
    }

    public static interface UserListener {
        public void onChanged(LoginVariables mUserInfo);
    }

    public void addUserListener(UserListener userListener) {
        this.mUserListenerList.add(userListener);
    }

    public synchronized LoginVariables getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = S1UserDB.getInstance().execSelect();//获取数据库里的用户数据
        }
        return mUserInfo;
    }

    public synchronized void addUser(LoginVariables userInfo) {
        mUserInfo = userInfo;
        S1UserDB.getInstance().execInsert(userInfo);//插入用户登录数据
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (UserListener userListener : mUserListenerList) {
                    userListener.onChanged(mUserInfo);
                }
            }
        });
    }

    public void removeUser() {
        S1UserDB.getInstance().execDelete();//删除
        mUserInfo = null;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (UserListener userListener : mUserListenerList) {
                    userListener.onChanged(mUserInfo);
                }
            }
        });
    }

    public void runOnUIThread(Runnable runnable) {
        mHandler.post(runnable);//从子线程取出任务 放在ui线程执行
    }
}
