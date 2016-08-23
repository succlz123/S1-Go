package org.succlz123.s1go.app.ui.thread.list;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.api.bean.ThreadList;
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
import android.text.TextUtils;
import android.view.View;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by succlz123 on 2015/6/25.
 */
public class ThreadListFragment extends BaseThreadRvFragment {
    public static final String TAG = "ThreadListFragment";
    public static final String KEY_THREAD_FRAGMENT_FID = "key_fragment_thread_fid";

    private ThreadListRvAdapter mThreadListRvAdapter;
    private String mFid;

    private int mPager = 1;
    private boolean mHasNextPage = true;
    private boolean mIsLoading;

    public static ThreadListFragment newInstance(String tid) {
        ThreadListFragment threadListFragment = new ThreadListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_THREAD_FRAGMENT_FID, tid);
        threadListFragment.setArguments(bundle);
        return threadListFragment;
    }

    @Override
    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(recyclerView, savedInstanceState);

        if (getArguments() == null || TextUtils.equals(mFid = getArguments().getString(KEY_THREAD_FRAGMENT_FID), "0")) {
            return;
        }

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
        mThreadListRvAdapter = new ThreadListRvAdapter();
        recyclerView.setAdapter(mThreadListRvAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mIsLoading && mHasNextPage && mThreadListRvAdapter != null) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    if (layoutManager.getChildCount() > 0
                            && lastVisibleItemPosition + 1 >= layoutManager.getItemCount() - 1
                            && layoutManager.getItemCount() > layoutManager.getChildCount()) {
                        loadThreadList();
                    }
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.Variables loginInfo = MainApplication.getInstance().loginInfo;
                if (loginInfo == null) {
                    LoginActivity.start(getActivity());
                } else {
                    ToastHelper.showShort("额,有空在说.");
//                    SendThreadsActivity.start(getActivity(), mFid);
                }
            }
        });
        loadThreadList();
        setRefreshing();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThreadListRvAdapter = null;
    }

    private void loadThreadList() {
        mIsLoading = true;
        Observable<ThreadList> observable = RetrofitManager.apiService().getThreadList(mPager, mFid);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<ThreadList, Boolean>() {
                    @Override
                    public Boolean call(ThreadList threadList) {
                        return SysUtils.isActivityLive(ThreadListFragment.this);
                    }
                })
                .subscribe(new Action1<ThreadList>() {
                    @Override
                    public void call(ThreadList threadList) {
                        mPager++;
                        mThreadListRvAdapter.setData(threadList.Variables.forum_threadlist);
                        mIsLoading = false;
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
