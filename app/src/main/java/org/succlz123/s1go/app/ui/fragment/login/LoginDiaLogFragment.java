package org.succlz123.s1go.app.ui.fragment.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
 * Created by fashi on 2015/4/12.
 */
public class LoginDiaLogFragment extends DialogFragment {
	private View mView;
	private EditText mUsername;
	private EditText mPassword;
	private LoginAsyncTask mLoginAsyncTask;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		initViews();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(mView)
				.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String un = mUsername.getText().toString();
						String pw = mPassword.getText().toString();
						if (!un.isEmpty() && !pw.isEmpty()) {
							mLoginAsyncTask = new LoginAsyncTask(un, pw);
							mLoginAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						} else if (un.isEmpty() || pw.isEmpty()) {
							Toast.makeText(getActivity(), getString(R.string.please_input_un_pw), Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(getString(R.string.cancel), null);
		return builder.create();
	}

	private void initViews() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mView = inflater.inflate(R.layout.fragment_logindialog, null);
		mUsername = (EditText) mView.findViewById(R.id.login_username);
		mPassword = (EditText) mView.findViewById(R.id.login_password);
	}

	public static class LoginAsyncTask extends AsyncTask<Void, Void, String> {
		private HashMap<String, String> paramss = new HashMap<String, String>();
		private String url;
		private String username;
		private String password;
		private LoginObject loginObject;
		private LoginVariables userInfo;
		private String messagestr;
		private String messageval;

		public LoginAsyncTask(String username, String password) {
			url = S1Url.LOGIN;
			this.username = username;
			this.password = password;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			paramss.put(S1String.USERNAME, username);
			paramss.put(S1String.PASSWORD, password);
			paramss.put(S1String.COOKIE_TIME, S1String.COOKIE_TIME_DIGIT);
		}

		@Override
		protected String doInBackground(Void... params) {
			return MyOkHttp.getInstance().login(url, paramss);
		}

		@Override
		protected void onPostExecute(String aVoid) {
			super.onPostExecute(aVoid);
			loginObject = LoginObject.parseJson(aVoid);
			userInfo = loginObject.getVariables();
			userInfo.setPassword(DeEnCode.code(password));

			//login succeed > messagestr = welcome back
			//login failed > messagestr = the number of you can try
			messagestr = loginObject.getMessage().getMessagestr();

			//login succeed >messageval = S1String.LOGIN_SUCCEED
			//login failed > messageval = S1String.LOGIN_FAILED
			messageval = loginObject.getMessage().getMessageval();

			if ((TextUtils.equals(messageval, S1String.LOGIN_SUCCEED))) {
				MyApplication.getInstance().addUser(userInfo);
				String hint = MyApplication.getInstance().getApplicationContext()
						.getResources().getString(R.string.welcome) + username;
				Toast.makeText(MyApplication.getInstance(),
						hint,
						Toast.LENGTH_SHORT).show();
			} else if ((TextUtils.equals(messageval, S1String.LOGIN_FAILED))) {
				Toast.makeText(MyApplication.getInstance(),
						messagestr,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
