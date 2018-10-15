package org.succlz123.s1go.ui.login;

import org.succlz123.s1go.BaseDataSource;
import org.succlz123.s1go.BasePresenter;
import org.succlz123.s1go.BaseView;
import org.succlz123.s1go.bean.UserInfo;

import rx.Observable;

/**
 * Created by succlz123 on 2015/4/14.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void onLoginSuccess(UserInfo userInfo);

        void onLoginFailed(String hint);
    }

    interface Presenter extends BasePresenter {

        void login(String username, String password);

    }

    interface DataSource extends BaseDataSource {

        Observable<UserInfo> login(String username, String password);
    }
}
