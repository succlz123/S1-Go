package org.succlz123.s1go.app.support.asynctask;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.bean.login.LoginObject;
import org.succlz123.s1go.app.support.bean.login.LoginVariables;
import org.succlz123.s1go.app.support.biz.MyOkHttp;
import org.succlz123.s1go.app.support.utils.DeEnCode;
import org.succlz123.s1go.app.support.utils.S1String;
import org.succlz123.s1go.app.support.utils.S1Url;

import java.util.HashMap;

/**
 * Created by fashi on 2015/5/18.
 */
public class LoginAsyncTask extends AsyncTask<Void, Void, String> {
	private HashMap<String, String> paramss = new HashMap<String, String>();
	private String mUrl;
	private String mUsername;
	private String mPassword;
	private LoginObject mLoginObject;
	private LoginVariables mUserInfo;
	private String mMessagestr;
	private String mMessageval;

	public LoginAsyncTask(String username, String password) {
		mUrl = S1Url.LOGIN;
		this.mUsername = username;
		this.mPassword = password;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		paramss.put(S1String.USERNAME, mUsername);
		paramss.put(S1String.PASSWORD, mPassword);
		paramss.put(S1String.COOKIE_TIME, S1String.COOKIE_TIME_DIGIT);
	}

	@Override
	protected String doInBackground(Void... params) {
		return MyOkHttp.getInstance().login(mUrl, paramss);
	}

	@Override
	protected void onPostExecute(String aVoid) {
		super.onPostExecute(aVoid);
		mLoginObject = LoginObject.parseJson(aVoid);
		mUserInfo = mLoginObject.getVariables();
		mUserInfo.setPassword(DeEnCode.code(mPassword));

		//login succeed > mMessagestr = welcome back
		//login failed > mMessagestr = the number of you can try
		mMessagestr = mLoginObject.getMessage().getMessagestr();

		//login succeed >mMessageval = S1String.LOGIN_SUCCEED
		//login failed > mMessageval = S1String.LOGIN_FAILED
		mMessageval = mLoginObject.getMessage().getMessageval();

		if ((TextUtils.equals(mMessageval, S1String.LOGIN_SUCCEED))) {
			MyApplication.getInstance().addUser(mUserInfo);
			String hint = MyApplication.getInstance().getApplicationContext()
					.getResources().getString(R.string.welcome) + mUsername;
			Toast.makeText(MyApplication.getInstance(),
					hint,
					Toast.LENGTH_SHORT).show();
		} else if ((TextUtils.equals(mMessageval, S1String.LOGIN_FAILED))) {
			Toast.makeText(MyApplication.getInstance(),
					mMessagestr,
					Toast.LENGTH_SHORT).show();
		}
	}
}
