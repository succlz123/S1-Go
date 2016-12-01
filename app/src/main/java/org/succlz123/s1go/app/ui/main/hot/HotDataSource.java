package org.succlz123.s1go.app.ui.main.hot;

import org.succlz123.s1go.app.bean.HotPost;
import org.succlz123.s1go.app.config.RetrofitManager;

import rx.Observable;

/**
 * Created by succlz123 on 2016/11/28.
 */

public class HotDataSource implements HotContract.DataSource {

    @Override
    public Observable<HotPost> getHot() {
        return RetrofitManager.apiService().getHot();
    }
}
