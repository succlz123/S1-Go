package org.succlz123.s1go.app.ui.login;

import org.succlz123.s1go.app.api.bean.UserInfo;

/**
 * Created by succlz123 on 16/4/24.
 */
public interface LoginInfoListener {
    void onChanged( UserInfo.Variables userInfo);
}

