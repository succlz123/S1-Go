package org.succlz123.s1go.app.ui.drawer.hot;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.api.bean.HotPost;
import org.succlz123.s1go.app.api.bean.LoginInfo;
import org.succlz123.s1go.app.config.RetrofitManager;
import org.succlz123.s1go.app.ui.base.BaseSwipeRecyclerFragment;
import org.succlz123.s1go.app.utils.common.SysUtils;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by fashi on 2015/4/12.
 */
public class HotFragment extends BaseSwipeRecyclerFragment {
    public static final String TAG = HotFragment.class.getName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HotRvAdapter mAdapter;

    public static HotFragment newInstance() {
        return new HotFragment();
    }

    @Override
    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(recyclerView, savedInstanceState);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HotRvAdapter.ItemDecoration());

        mAdapter = new HotRvAdapter();
        recyclerView.setAdapter(mAdapter);

        loadInfo();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        loadInfo();
    }

    private void loadInfo() {
        setRefreshing();

        LoginInfo.VariablesEntity loginInfo = MainApplication.getInstance().getLoginInfo();
        if (loginInfo != null) {
            String cookie = loginInfo.cookiepre;
            String auth = "auth=" + Uri.encode(loginInfo.auth);
            String saltkey = "saltkey=" + loginInfo.saltkey;
//        "cookie", cookie + auth + ";" + cookie + saltkey + ";";
        }
        Observable<HotPost> observable = RetrofitManager.apiService().getHot();
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<HotPost, Boolean>() {
                    @Override
                    public Boolean call(HotPost hotPost) {
                        return SysUtils.isActivityLive(HotFragment.this);
                    }
                })
                .subscribe(new Action1<HotPost>() {
                    @Override
                    public void call(HotPost hotPost) {
                        mAdapter.setData(hotPost);
                        setRefreshCompleted();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        setRefreshCompleted();
                    }
                });
        compositeSubscription.add(subscription);
    }
}
