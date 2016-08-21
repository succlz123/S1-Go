package org.succlz123.s1go.app.ui.base;

import com.squareup.leakcanary.RefWatcher;

import org.succlz123.s1go.app.MainApplication;

import android.support.v4.app.Fragment;
import android.view.View;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by succlz123 on 2015/7/8.
 */
public abstract class BaseFragment extends Fragment {
    protected boolean isVisible;
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

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
        compositeSubscription.unsubscribe();
        RefWatcher refWatcher = MainApplication.getInstance().refWatcher;
        refWatcher.watch(this);
    }

    protected <T extends View> T f(View view, int resId) {
        return (T) view.findViewById(resId);
    }
}

