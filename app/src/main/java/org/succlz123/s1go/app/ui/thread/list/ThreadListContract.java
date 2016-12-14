package org.succlz123.s1go.app.ui.thread.list;

import org.succlz123.s1go.app.BaseDataSource;
import org.succlz123.s1go.app.BasePresenter;
import org.succlz123.s1go.app.BaseView;
import org.succlz123.s1go.app.bean.ThreadList;

import java.util.List;

import rx.Observable;

/**
 * Created by succlz123 on 2015/4/14.
 */

public interface ThreadListContract {

    interface View extends BaseView<Presenter> {

        void setData(List<ThreadList.VariablesEntity.ForumThreadlistEntity> data);

        void setChange(List<ThreadList.VariablesEntity.ForumThreadlistEntity> data);

        void onFailed();
    }

    interface Presenter extends BasePresenter {

        void loadThreadList();

        String getFormHash();

        void onRefresh();
    }

    interface DataSource extends BaseDataSource {

        Observable<ThreadList> loadThreadList(int pager, String fid);
    }
}
