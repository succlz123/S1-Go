package org.succlz123.s1go.app.ui.login;

import org.succlz123.s1go.app.bean.UserInfo;
import org.succlz123.s1go.app.config.RetrofitManager;

import rx.Observable;

/**
 * Created by succlz123 on 2016/11/28.
 */

public class LoginDataSource implements LoginContract.DataSource {

    @Override
    public Observable<UserInfo> login(String username, String password) {
        return RetrofitManager.apiService().login(username, password);
    }
}
