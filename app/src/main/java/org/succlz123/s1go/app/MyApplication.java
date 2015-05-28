package org.succlz123.s1go.app;

import android.app.Application;
import android.os.Handler;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.dao.db.UserDB;
import org.succlz123.s1go.app.dao.helper.S1FidIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fashi on 2015/4/13.
 */
public class MyApplication extends Application {
	public LoginVariables mUserInfo;
	private onUserListener mOnUserListener;
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
		//获得论坛板块名图标
		S1FidIcon.getS1FidImg();
	}

	public static interface onUserListener {
		public void onChanged(LoginVariables mUserInfo);
	}

	public void addUserListener(onUserListener onUserListener) {
		this.mOnUserListenerList.add(onUserListener);
	}

	public LoginVariables getUserInfo() {
		if (mUserInfo == null) {
			mUserInfo = UserDB.getInstance().execSelect();//获取数据库里的用户数据
		}
		return mUserInfo;
	}

	public void addUser(LoginVariables userInfo) {
		mUserInfo = userInfo;
		UserDB.getInstance().execInsert(userInfo);//插入用户登录数据
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
		UserDB.getInstance().execDelete();//删除
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
