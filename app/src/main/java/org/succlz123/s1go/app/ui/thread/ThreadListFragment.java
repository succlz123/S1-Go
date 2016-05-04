package org.succlz123.s1go.app.ui.thread;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.api.bean.ThreadList;
import org.succlz123.s1go.app.config.RetrofitManager;
import org.succlz123.s1go.app.ui.base.BaseRecyclerViewFragment;
import org.succlz123.s1go.app.utils.common.SysUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fashi on 2015/6/25.
 */
public class ThreadListFragment extends BaseRecyclerViewFragment {
    public static final String KEY_THREAD_FRAGMENT_FID = "key_fragment_thread_fid";

    private String mFid;
    private int mPage;

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
//        mProgressBar = (ProgressBar) recyclerView.findViewById(R.id.progressBar);

        if (getArguments() == null || TextUtils.equals(mFid = getArguments().getString(KEY_THREAD_FRAGMENT_FID), "0")) {
            return;
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ThreadListRvAdapter.ItemDecoration());

        final ThreadListRvAdapter threadListRvAdapter = new ThreadListRvAdapter();
        recyclerView.setAdapter(threadListRvAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (scrollState == SCROLL_STATE_IDLE &&
//                        view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                    mProgressBar.setVisibility(View.VISIBLE);
//                    mProgressBar.setLayoutParams(new AbsListView.LayoutParams(
//                            AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
//                    GetThreadsListAsyncTask getThreadsListAsyncTask = new GetThreadsListAsyncTask();
//                    getThreadsListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                }
            }
        });
//        LoginVariables loginInfo = MainApplication.getInstance().loginInfo;
//        if (loginInfo != null) {
//            String cookie = loginInfo.getCookiepre();
//            String auth = S1GoConfig.AUTH + "=" + Uri.encode(loginInfo.getAuth());
//            String saltKey = S1GoConfig.SALT_KEY + "=" + loginInfo.getSaltkey();
//            this.hearders.put(S1GoConfig.COOKIE, cookie + auth + ";" + cookie + saltKey + ";");
//        }
        Observable<ThreadList> observable = RetrofitManager.apiService().getThreadList(1, mFid);

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

                        threadListRvAdapter.setData(threadList.Variables.forum_threadlist);

//                        swipeRefreshLayout.setRefreshing(false);
//                        swipeRefreshLayout.setEnabled(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
//                        swipeRefreshLayout.setRefreshing(false);
//                        swipeRefreshLayout.setEnabled(true);
                        Toast.makeText(MainApplication.getInstance().getApplicationContext(),
                                MainApplication.getInstance().getApplicationContext().getString(R.string.sorry),
                                Toast.LENGTH_LONG).show();
                    }
                });
        mCompositeSubscription.add(subscription);
    }
}
