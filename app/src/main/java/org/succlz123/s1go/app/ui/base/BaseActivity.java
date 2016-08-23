package org.succlz123.s1go.app.ui.base;

import com.squareup.leakcanary.RefWatcher;

import org.succlz123.s1go.app.MainApplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by succlz123 on 2015/7/8.
 */
public class BaseActivity extends AppCompatActivity {
    public CompositeSubscription compositeSubscription;

    private boolean mFragmentStateSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFragmentStateSaved = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFragmentStateSaved = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
        RefWatcher refWatcher = MainApplication.getInstance().refWatcher;
        refWatcher.watch(this);
    }

    public boolean isFragmentStateSaved() {
        return mFragmentStateSaved;
    }
}
