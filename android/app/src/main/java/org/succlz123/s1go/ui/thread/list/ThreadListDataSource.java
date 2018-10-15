package org.succlz123.s1go.ui.thread.list;

import org.succlz123.s1go.bean.ThreadList;
import org.succlz123.s1go.config.RetrofitManager;

import rx.Observable;

/**
 * Created by succlz123 on 2016/11/28.
 */

public class ThreadListDataSource implements ThreadListContract.DataSource {

    @Override
    public Observable<ThreadList> loadThreadList(int pager, String fid) {
        return RetrofitManager.apiService().getThreadList(pager, fid);
    }
}
