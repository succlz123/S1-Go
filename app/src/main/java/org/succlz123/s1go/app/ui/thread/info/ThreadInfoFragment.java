package org.succlz123.s1go.app.ui.thread.info;


import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.api.bean.ThreadInfo;
import org.succlz123.s1go.app.api.bean.UserInfo;
import org.succlz123.s1go.app.config.RetrofitManager;
import org.succlz123.s1go.app.ui.base.BaseThreadRvFragment;
import org.succlz123.s1go.app.ui.login.LoginActivity;
import org.succlz123.s1go.app.utils.common.MyUtils;
import org.succlz123.s1go.app.utils.common.SysUtils;
import org.succlz123.s1go.app.utils.common.ToastHelper;

import android.graphics.Rect;
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
 * Created by succlz123 on 2015/6/13.
 */
public class ThreadInfoFragment extends BaseThreadRvFragment {
    public static final String BUNDLE_KEY_TID = "tid";
    public static final String BUNDLE_KEY_TOTAL_PAGER_NUM = "totalPagerNum";
    public static final String BUNDLE_KEY_CURRENT_PAGER_NUM = "currentPagerNum";

    private String mTid;
    private int mCurrentPagerNum;
    private int mTotalPagerNum;
    private ThreadInfoRvAdapter mThreadInfoRvAdapter;

    public static ThreadInfoFragment newInstance(String tid, int currentPagerNum, int totalPagerNum) {
        ThreadInfoFragment threadInfoFragment = new ThreadInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TID, tid);
        bundle.putInt(BUNDLE_KEY_CURRENT_PAGER_NUM, currentPagerNum);
        bundle.putInt(BUNDLE_KEY_TOTAL_PAGER_NUM, totalPagerNum);
        threadInfoFragment.setArguments(bundle);
        return threadInfoFragment;
    }

    @Override
    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(recyclerView, savedInstanceState);
        if (getArguments() == null) {
            return;
        }
        mTid = getArguments().getString(BUNDLE_KEY_TID);
        mCurrentPagerNum = getArguments().getInt(BUNDLE_KEY_CURRENT_PAGER_NUM);
        mTotalPagerNum = getArguments().getInt(BUNDLE_KEY_CURRENT_PAGER_NUM);
        if (mTid == null) {
            return;
        }
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
                int margin = MyUtils.dip2px(5);
                if (position == 0) {
                    outRect.set(0, margin, 0, margin);
                } else {
                    outRect.set(0, 0, 0, margin);
                }
            }
        });
        mThreadInfoRvAdapter = new ThreadInfoRvAdapter();
        recyclerView.setAdapter(mThreadInfoRvAdapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.Variables loginInfo = MainApplication.getInstance().loginInfo;
                if (loginInfo == null) {
                    LoginActivity.start(getActivity());
                } else {
                    ToastHelper.showShort("额,有空在说.");
//                    SendReplyActivity.start(getActivity(), mTid);
                }
            }
        });
        loadThreadInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThreadInfoRvAdapter = null;
    }

    private void loadThreadInfo() {
        Observable<ThreadInfo> observable = RetrofitManager.apiService().getThreadInfo(mCurrentPagerNum, mTid);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<ThreadInfo, Boolean>() {
                    @Override
                    public Boolean call(ThreadInfo threadInfo) {
                        return SysUtils.isActivityLive(ThreadInfoFragment.this);
                    }
                })
                .subscribe(new Action1<ThreadInfo>() {
                    @Override
                    public void call(ThreadInfo threadInfo) {
                        //每次刷新时获得的回帖数
                        int replies = threadInfo.Variables.thread.replies;

                        mThreadInfoRvAdapter.setData(threadInfo.Variables.postlist);
                        setRefreshCompleted();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        setRefreshError();
                        ToastHelper.showShort(R.string.sorry);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
