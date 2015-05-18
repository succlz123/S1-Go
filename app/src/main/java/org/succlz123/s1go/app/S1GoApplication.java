package org.succlz123.s1go.app;

import android.app.Application;
import android.os.Handler;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.dao.helper.S1FidIcon;
import org.succlz123.s1go.app.dao.database.S1UserDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fashi on 2015/4/13.
 */
public class S1GoApplication extends Application {

	private static S1GoApplication instance;
	private LoginVariables loginVariables;
	private Handler handler = new Handler();

	public static interface UserInfoListener {
		public void onInfoChanged(LoginVariables loginVariables);
	}

	private List<UserInfoListener> userInfoListenerList = new ArrayList<UserInfoListener>();

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


	public void addUserInfo(final LoginVariables loginVariables) {
		this.loginVariables = loginVariables;
		S1UserDB.getInstance().execInsert(loginVariables);//插入用户登录数据
		handler.post(new Runnable() {
			@Override
			public void run() {
				for (UserInfoListener userInfoListener : userInfoListenerList) {
					userInfoListener.onInfoChanged(loginVariables);
				}
			}
		});
	}

	public void removeUserInfo() {
		S1UserDB.getInstance().execDelete();//删除
		this.loginVariables = null;
		handler.post(new Runnable() {
			@Override
			public void run() {
				for (UserInfoListener userInfoListener : userInfoListenerList) {
					userInfoListener.onInfoChanged(null);
				}
			}
		});
	}

	public void addUserInfoListener(UserInfoListener userInfoListener) {
		this.userInfoListenerList.add(userInfoListener);
	}

	public void removeUserInfoListener(UserInfoListener userInfoListener) {
		this.userInfoListenerList.remove(userInfoListener);
	}

	public LoginVariables getUserInfo() {
		if (this.loginVariables == null) {
			this.loginVariables = S1UserDB.getInstance().execSelect();//获取数据库里的用户数据
		}
		return this.loginVariables;
	}

	public void runOnUIThread(Runnable runnable) {
		handler.post(runnable);//从子线程取出toast 放在ui线程更新显示toast
	}
}
