package org.succlz123.s1go.app.ui.login;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.bean.UserInfo;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.common.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by succlz123 on 16/4/11.
 */
public class LoginActivity extends BaseToolbarActivity implements LoginContract.View {
    private EditText mUsernameEt;
    private EditText mPasswordEt;

    private LoginContract.Presenter mLoginPresenter;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showBackButton();
        ensureToolbar();
        setTitle("登陆");

        mUsernameEt = (EditText) findViewById(R.id.login_username);
        mPasswordEt = (EditText) findViewById(R.id.login_password);
        Button action = (Button) findViewById(R.id.login);

        new LoginPresenter(new LoginDataSource(), this);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.login(mUsernameEt.getText().toString(), mPasswordEt.getText().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.unSubscribe();
        super.onDestroy();
    }

    @Override
    public void onLoginSuccess(UserInfo userInfo) {
        String hint = getString(R.string.welcome) + userInfo.Variables.member_username;
        ToastUtils.showToastShort(this, hint);
        MainApplication.getInstance().addUserInfo(userInfo);
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onLoginFailed(String hint) {
        ToastUtils.showToastShort(this, hint);
    }

    @Override
    public boolean isActive() {
        return !isDestroyed() && !isFinishing();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mLoginPresenter = presenter;
    }
}
