package org.succlz123.s1go.ui.picture;

import android.graphics.Bitmap;
import android.text.TextUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by succlz123 on 2016/11/28.
 */

public class PicPresenter implements PicContract.Presenter {
    private final CompositeSubscription mCompositeSubscription;
    private final PicContract.DataSource mDataSource;
    private final PicContract.View mView;

    public PicPresenter(PicContract.DataSource dataSource, PicContract.View view) {
        mDataSource = dataSource;
        mView = view;
        mView.setPresenter(this);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getBitmap(final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Subscription subscription = mDataSource.getBitmap(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    private Bitmap mBitmap;
                    private String mFile;

                    @Override
                    public void onCompleted() {
                        if (mView.isActive()) {
                            mView.setBitmapSource(mFile, mBitmap);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object o) {
                        if (o instanceof Bitmap) {
                            mBitmap = (Bitmap) o;
                        } else if (o instanceof String) {
                            mFile = (String) o;
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void savePic(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Subscription subscription = mDataSource.savePic(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (mView.isActive()) {
                            if (aBoolean) {
                                mView.onSaveSuccess();
                            } else {
                                mView.onSaveFailed();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (mView.isActive()) {
                            mView.onSaveFailed();
                        }
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
