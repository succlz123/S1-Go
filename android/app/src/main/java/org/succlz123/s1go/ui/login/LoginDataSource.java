package org.succlz123.s1go.ui.login;

import org.succlz123.s1go.bean.UserInfo;
import org.succlz123.s1go.config.RetrofitManager;

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
