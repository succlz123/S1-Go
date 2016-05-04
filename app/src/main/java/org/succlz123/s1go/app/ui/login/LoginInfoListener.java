package org.succlz123.s1go.app.ui.login;

import org.succlz123.s1go.app.api.bean.LoginInfo;

/**
 * Created by succlz123 on 16/4/24.
 */
public interface LoginInfoListener {
    void onChanged( LoginInfo.VariablesEntity userInfo);
}

