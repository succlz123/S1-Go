package org.succlz123.s1go.app.ui.hot;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.api.bean.HotPost;
import org.succlz123.s1go.app.api.bean.UserInfo;
import org.succlz123.s1go.app.config.RetrofitManager;
import org.succlz123.s1go.app.ui.base.BaseSwipeRefreshRvFragment;
import org.succlz123.s1go.app.utils.common.MyUtils;
import org.succlz123.s1go.app.utils.common.SysUtils;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by succlz123 on 2015/4/12.
 */
public class HotFragment extends BaseSwipeRefreshRvFragment {
    private HotRvAdapter mHotRvAdapter;

    public static HotFragment newInstance() {
        return new HotFragment();
    }

    @Override
    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(recyclerView, savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        int dp5 = MyUtils.dip2px(5);
        recyclerView.setPadding(dp5, dp5, dp5, 0);
        recyclerView.setClipToPadding(false);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
                int margin = MyUtils.dip2px( 5);
                outRect.set(0, 0, 0, margin);
            }
        });
        mHotRvAdapter = new HotRvAdapter();
        recyclerView.setAdapter(mHotRvAdapter);

        getHotInfo();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getHotInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHotRvAdapter = null;
    }

    private void getHotInfo() {
        setRefreshing();

        UserInfo.Variables loginInfo = MainApplication.getInstance().getUserInfo();
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
                        mHotRvAdapter.setData(hotPost);
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
