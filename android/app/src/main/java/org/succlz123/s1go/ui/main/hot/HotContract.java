package org.succlz123.s1go.ui.main.hot;

import org.succlz123.s1go.BaseDataSource;
import org.succlz123.s1go.BasePresenter;
import org.succlz123.s1go.BaseView;
import org.succlz123.s1go.bean.HotPost;

import rx.Observable;

/**
 * Created by succlz123 on 2015/4/14.
 */

public interface HotContract {

    interface View extends BaseView<Presenter> {

        void onResponse(HotPost hotPost);

        void onError();

    }

    interface Presenter extends BasePresenter {

        void getHot();

    }

    interface DataSource extends BaseDataSource {

        Observable<HotPost> getHot();
    }
}
