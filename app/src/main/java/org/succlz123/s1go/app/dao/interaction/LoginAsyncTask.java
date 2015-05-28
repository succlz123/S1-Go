package org.succlz123.s1go.app.dao.interaction;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.bean.login.LoginObject;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.dao.api.DeEnCode;
import org.succlz123.s1go.app.dao.api.MyOkHttp;
import org.succlz123.s1go.app.dao.helper.S1Url;

import java.util.HashMap;

/**
 * Created by fashi on 2015/5/18.
 */
public class LoginAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String SUCCEED = "location_login_succeed_mobile";
    private static final String FAILED = "login_invalid";
    private static final String USER_NAME = "username";
    private static final String PASS_WORD = "password";
    private static final String COOKIE_TIME = "cookietime";
    private static final String COOKIE_TIME_DIGIT = "2592000";

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
        paramss.put(USER_NAME, mUsername);
        paramss.put(PASS_WORD, mPassword);
        paramss.put(COOKIE_TIME, COOKIE_TIME_DIGIT);
    }

    @Override
    protected String doInBackground(Void... params) {
        String json = MyOkHttp.getInstance().login(mUrl, paramss);
        return json;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        mLoginObject = LoginObject.parseJson(aVoid);
        mUserInfo = mLoginObject.getVariables();
        mUserInfo.setPassword(DeEnCode.code(mPassword));

        //登陆成功返回 欢迎回来
        //登陆失败返回 还可以尝试的次数
        mMessagestr = mLoginObject.getMessage().getMessagestr();

        //登陆成功返回 SUCCEED
        //登陆失败返回 FAILED
        mMessageval = mLoginObject.getMessage().getMessageval();

        if ((TextUtils.equals(mMessageval, SUCCEED))) {
            MyApplication.getInstance().addUser(mUserInfo);
            String hint = MyApplication.getInstance().getApplicationContext()
                    .getResources().getString(R.string.welcome) + mUsername;
            Toast.makeText(MyApplication.getInstance(), hint, Toast.LENGTH_SHORT).show();
        } else if ((TextUtils.equals(mMessageval, FAILED))) {
            Toast.makeText(MyApplication.getInstance(), mMessagestr, Toast.LENGTH_SHORT).show();
        }
    }
}
