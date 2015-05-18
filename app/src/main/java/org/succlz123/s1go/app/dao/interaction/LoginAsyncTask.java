package org.succlz123.s1go.app.dao.interaction;

import android.os.AsyncTask;
import android.widget.Toast;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.login.LoginObject;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.dao.api.MyOkHttp;
import org.succlz123.s1go.app.dao.helper.S1Url;

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
    private LoginVariables mLoginVariables;
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
        paramss.put("username", mUsername);
        paramss.put("password", mPassword);
        paramss.put("cookietime", "2592000");
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
        mLoginVariables = mLoginObject.getVariables();
        //登陆成功返回 欢迎回来 登陆失败返回 还可以登陆尝试的次数
        mMessagestr = mLoginObject.getMessage().getMessagestr();
        //登陆成功返回 location_login_succeed_mobile 登陆失败返回 login_invalid
        mMessageval = mLoginObject.getMessage().getMessageval();
        if ((mMessageval.equals("location_login_succeed_mobile"))) {
            S1GoApplication.getInstance().addUserInfo(mLoginVariables);
            String hint = S1GoApplication.getInstance().getApplicationContext()
                    .getResources().getString(R.string.welcome) + mUsername;
            Toast.makeText(S1GoApplication.getInstance(), hint, Toast.LENGTH_SHORT).show();
        } else if (mMessageval.equals("login_invalid")) {
            Toast.makeText(S1GoApplication.getInstance(), mMessagestr, Toast.LENGTH_SHORT).show();
        }
    }
}
