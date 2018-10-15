package org.succlz123.s1go.ui.base;

import com.squareup.leakcanary.RefWatcher;

import org.succlz123.s1go.MainApplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by succlz123 on 2015/7/8.
 */
public abstract class BaseFragment extends Fragment {
    public CompositeSubscription compositeSubscription;
    protected boolean isVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
        RefWatcher refWatcher = MainApplication.getInstance().refWatcher;
        refWatcher.watch(this);
    }
}

