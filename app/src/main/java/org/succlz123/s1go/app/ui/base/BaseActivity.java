package org.succlz123.s1go.app.ui.base;

import com.squareup.leakcanary.RefWatcher;

import org.succlz123.s1go.app.MainApplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by succlz123 on 2015/7/8.
 */
public class BaseActivity extends AppCompatActivity {
    public final String TAG = getClass().getName();

    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MainApplication.getInstance().refWatcher;
        refWatcher.watch(this);
        mCompositeSubscription.unsubscribe();
    }

    protected <T extends View> T f(int resId) {
        return (T) super.findViewById(resId);
    }
}
