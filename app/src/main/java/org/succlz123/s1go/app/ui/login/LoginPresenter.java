package org.succlz123.s1go.app.ui.login;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.bean.UserInfo;
import org.succlz123.s1go.app.config.S1GoConfig;
import org.succlz123.s1go.app.utils.schedulers.SchedulerProvider;

import android.text.TextUtils;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by succlz123 on 2016/11/28.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private CompositeSubscription mCompositeSubscription;
    private SchedulerProvider mSchedulerProvider;
    private final LoginContract.DataSource mDataSource;
    private final LoginContract.View mLoginView;
    private boolean mIsLoading;

    public LoginPresenter(LoginContract.DataSource dataSource, LoginContract.View view) {
        mDataSource = dataSource;
        mLoginView = view;
        mLoginView.setPresenter(this);
        mCompositeSubscription = new CompositeSubscription();
        mSchedulerProvider = SchedulerProvider.getInstance();
    }

    @Override
    public void login(final String username, final String password) {
        if (username.isEmpty() || password.isEmpty()) {
            if (mLoginView.isActive()) {
                mLoginView.onLoginFailed(MainApplication.getContext().getString(R.string.please_input_un_pw));
                return;
            }
        }
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        Subscription subscription = mDataSource.login(username, password)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .filter(new Func1<UserInfo, Boolean>() {
                    @Override
                    public Boolean call(UserInfo loginInfo) {
                        mIsLoading = false;
                        return mLoginView.isActive();
                    }
                })
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        //login succeed > messageStr = welcome back
                        //login failed > messageStr = the number of you can try
                        String messageStr = userInfo.Message.messagestr;
                        //login succeed >messageVal = location_login_succeed_mobile
                        //login failed > messageVal = login_invalid
                        //抱歉，密码空或包含非法字符     = profile_passwd_illegal
                        String messageVal = userInfo.Message.messageval;
                        if ((TextUtils.equals(messageVal, S1GoConfig.LOGIN_SUCCEED))) {
                            mLoginView.onLoginSuccess(userInfo);
                        } else if ((TextUtils.equals(messageVal, S1GoConfig.LOGIN_FAILED))) {
                            mLoginView.onLoginFailed(messageVal);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mLoginView.onLoginFailed("网络异常,请重试!");
                    }
                });
        mCompositeSubscription.add(subscription);
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
    }
}
