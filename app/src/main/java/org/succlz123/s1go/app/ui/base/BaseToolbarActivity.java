package org.succlz123.s1go.app.ui.base;

import org.succlz123.s1go.app.R;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseToolbarActivity extends BaseActivity {
    private static final int[] themeAttrs = new int[]{R.attr.windowActionBar};
    private boolean mHasActionBar;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypedArray a = obtainStyledAttributes(themeAttrs);
        mHasActionBar = a.getBoolean(0, false);
        if (mHasActionBar) {
            Log.e("BaseToolbarActivity", "The theme you applied seems will have a WindowDecorActionBar! "
                    + "set attribute 'windowActionBar' to false in your theme!");
        }
        a.recycle();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public ActionBar getSupportActionBar() {
        if (!mHasActionBar) {
            ensureToolbar();
        }
        return super.getSupportActionBar();
    }

    public Toolbar getToolbar() {
        ensureToolbar();
        return toolbar;
    }

    private void ensureToolbar() {
        if (toolbar == null) {
            View toolbar = findViewById(R.id.toolbar);
            if (toolbar == null) {
                toolbar = getLayoutInflater().inflate(R.layout.layout_toolbar, (ViewGroup) findViewById(android.R.id.content));
                this.toolbar = (Toolbar) toolbar.findViewById(R.id.toolbar);
            } else {
                this.toolbar = (Toolbar) toolbar;
            }
            this.toolbar.setContentInsetsAbsolute(0, 0);
            setSupportActionBar(this.toolbar);
        }
    }

    public void showBackButton() {
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        //创建返回键，并实现打开开关监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        layout_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(null);
            toolbar = null;
        }
        super.onDestroy();
    }

    public void setCustomTitle(CharSequence title) {
        ensureToolbar();
        getSupportActionBar().setTitle(title);
    }

    public void setCustomView(View view) {
        ensureToolbar();
        toolbar.setTitle(null);
        toolbar.removeAllViews();
        toolbar.addView(view);
    }
}
