package org.succlz123.s1go.app.ui.thread.list;

import org.succlz123.s1go.app.bean.ThreadList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by succlz123 on 2016/11/28.
 */

public class ThreadListPresenter implements ThreadListContract.Presenter {
    private final CompositeSubscription mCompositeSubscription;
    private final ThreadListContract.DataSource mDataSource;
    private final ThreadListContract.View mView;

    private String mFid;
    private int mPager = 1;
    private boolean mHasNextPage = true;
    private boolean mIsLoading;

    private String mFormHash;

    public ThreadListPresenter(String fid, ThreadListContract.DataSource dataSource, ThreadListContract.View view) {
        mFid = fid;
        mDataSource = dataSource;
        mView = view;
        mView.setPresenter(this);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadThreadList() {
        if (mIsLoading && !mHasNextPage) {
            return;
        }
        mIsLoading = true;
        Observable<ThreadList> observable = mDataSource.loadThreadList(mPager, mFid);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThreadList>() {
                    @Override
                    public void call(ThreadList threadList) {
                        mFormHash = threadList.Variables.formhash;
                        if (mPager == 1) {
                            mView.setData(threadList.Variables.forum_threadlist);
                        } else {
                            mView.setChange(threadList.Variables.forum_threadlist);
                        }
                        mPager++;
                        mIsLoading = false;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mIsLoading = false;
                        mView.onFailed();
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public String getFormHash() {
        return mFormHash;
    }

    public void onRefresh() {
        mPager = 1;
        loadThreadList();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
    }
}
