package org.succlz123.s1go.app;

import android.app.Application;
import android.os.Handler;

import org.succlz123.s1go.app.support.bean.login.LoginVariables;
import org.succlz123.s1go.app.support.db.UserDB;
import org.succlz123.s1go.app.support.utils.S1FidIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fashi on 2015/4/13.
 */
public class MyApplication extends Application {
	public LoginVariables mUserInfo;
	private Handler mHandler = new Handler();
	private List<onUserListener> mOnUserListenerList = new ArrayList<onUserListener>();

	private static MyApplication instance;

	public static MyApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		S1FidIcon.initFidIcon();//initialize fid icons
	}

	public static interface onUserListener {
		public void onChanged(LoginVariables mUserInfo);
	}

	public void addUserListener(onUserListener onUserListener) {
		this.mOnUserListenerList.add(onUserListener);
	}

	public LoginVariables getUserInfo() {
		if (mUserInfo == null) {
			mUserInfo = UserDB.getInstance().execSelect();//select user info from database
		}
		return mUserInfo;
	}

	public void addUser(LoginVariables userInfo) {
		mUserInfo = userInfo;
		UserDB.getInstance().execInsert(userInfo);//insert user info to database
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				for (onUserListener onUserListener : mOnUserListenerList) {
					onUserListener.onChanged(mUserInfo);
				}
			}
		});
	}

	public void removeUser() {
		UserDB.getInstance().execDelete();//delete user info from database
		mUserInfo = null;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				for (onUserListener onUserListener : mOnUserListenerList) {
					onUserListener.onChanged(mUserInfo);
				}
			}
		});
	}

	public void runOnUIThread(Runnable runnable) {
		mHandler.post(runnable);//从子线程取出任务 放在ui线程执行
	}
}
