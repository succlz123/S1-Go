package org.succlz123.s1go.ui.thread.list;

import org.succlz123.s1go.BaseDataSource;
import org.succlz123.s1go.BasePresenter;
import org.succlz123.s1go.BaseView;
import org.succlz123.s1go.bean.ThreadList;

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

        void setFid(String fid);
    }

    interface DataSource extends BaseDataSource {

        Observable<ThreadList> loadThreadList(int pager, String fid);
    }
}
