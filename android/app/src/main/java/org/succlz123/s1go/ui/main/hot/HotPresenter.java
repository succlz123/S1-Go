package org.succlz123.s1go.ui.main.hot;

import org.succlz123.s1go.bean.HotPost;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by succlz123 on 2016/11/28.
 */

public class HotPresenter implements HotContract.Presenter {
    private CompositeSubscription mCompositeSubscription;
    private final HotContract.DataSource mDataSource;
    private final HotContract.View mView;
    private boolean mIsLoading;

    public HotPresenter(HotContract.DataSource dataSource, HotContract.View view) {
        mDataSource = dataSource;
        mView = view;
        mView.setPresenter(this);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getHot() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        Subscription subscription = mDataSource.getHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<HotPost, Boolean>() {
                    @Override
                    public Boolean call(HotPost hotPost) {
                        mIsLoading = false;
                        return mView.isActive();
                    }
                })
                .subscribe(new Action1<HotPost>() {
                    @Override
                    public void call(HotPost hotPost) {
                        mView.onResponse(hotPost);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.onError();
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
    }
}
