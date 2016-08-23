package org.succlz123.s1go.app.ui.login;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.api.bean.UserInfo;
import org.succlz123.s1go.app.config.RetrofitManager;
import org.succlz123.s1go.app.config.S1GoConfig;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.common.SysUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by succlz123 on 16/4/11.
 */
public class LoginActivity extends BaseToolbarActivity {

    public static void start(Context activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showBackButton();
        ensureToolbar();
        setTitle("登陆");

        final EditText usernameEt = (EditText) findViewById(R.id.login_username);
        final EditText passwordEt = (EditText) findViewById(R.id.login_password);
        Button action = (Button) findViewById(R.id.login);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SysUtils.isFastClick()) {
                    return;
                }
                String username = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    login(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.please_input_un_pw), Toast.LENGTH_SHORT).show();
                }
            }
        });

//        private void setFragment() {
//        mHotFragment = (HotFragment) getSupportFragmentManager().findFragmentByTag(HotFragment.TAG);
//        if (mHotFragment == null) {
//            mHotFragment = HotFragment.newInstance();
//            getSupportFragmentManager().beginTransaction().add(R.id.content, mHotFragment, HotFragment.TAG).commit();
//        } else {
//            getSupportFragmentManager().beginTransaction().hide(mForumAreaFragment).show(mHotFragment).commit();
//        }
//        mForumAreaFragment = (ForumAreaFragment) getSupportFragmentManager().findFragmentByTag(ForumAreaFragment.TAG);
//        if (mForumAreaFragment == null) {
//            mForumAreaFragment = ForumAreaFragment.newInstance();
//            getSupportFragmentManager().beginTransaction().hide(mHotFragment).add(R.id.content, mForumAreaFragment, ForumAreaFragment.TAG).commit();
//        } else {
//            getSupportFragmentManager().beginTransaction().hide(mHotFragment).show(mForumAreaFragment).commit();
//        }
//        }
//        mUserImg = (SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.img);
//        mUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
//        mUid = (TextView) navigationView.getHeaderView(0).findViewById(R.id.uid);
//        else {
//            mUserName.setText("点击头像登陆");
//            mUserImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LoginActivity.newInstance(MainActivity.this);
//                }
//            });
//        }
    }

    private void login(String username, final String password) {
        Observable<UserInfo> observable = RetrofitManager.apiService().login(username, password);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<UserInfo, Boolean>() {
                    @Override
                    public Boolean call(UserInfo loginInfo) {
                        return SysUtils.isActivityLive(LoginActivity.this);
                    }
                })
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo loginInfo) {
                        //login succeed > messageStr = welcome back
                        //login failed > messageStr = the number of you can try
                        String messageStr = loginInfo.Message.messagestr;
                        //login succeed >messageVal = location_login_succeed_mobile
                        //login failed > messageVal = login_invalid
                        //抱歉，密码空或包含非法字符     = profile_passwd_illegal
                        String messageVal = loginInfo.Message.messageval;

                        if ((TextUtils.equals(messageVal, S1GoConfig.LOGIN_SUCCEED))) {
                            String hint = getString(R.string.welcome) + loginInfo.Variables.member_username;
                            Toast.makeText(LoginActivity.this, hint, Toast.LENGTH_SHORT).show();
                            loginInfo.Variables.password = password;
                            MainApplication.getInstance().addUserInfo(loginInfo);
                            finish();
                        } else if ((TextUtils.equals(messageVal, S1GoConfig.LOGIN_FAILED))) {
                            Toast.makeText(LoginActivity.this, messageStr, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(LoginActivity.this, "网络异常,请重试!", Toast.LENGTH_LONG).show();
                    }
                });
        compositeSubscription.add(subscription);
    }
}
