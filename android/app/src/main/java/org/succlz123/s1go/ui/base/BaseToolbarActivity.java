package org.succlz123.s1go.ui.base;

import org.succlz123.s1go.R;

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

    public void ensureToolbar() {
        if (toolbar == null) {
            View findToolbar = findViewById(R.id.toolbar);
            if (findToolbar == null) {
                findToolbar = getLayoutInflater().inflate(R.layout.layout_toolbar, (ViewGroup) findViewById(android.R.id.content));
                toolbar = (Toolbar) findToolbar.findViewById(R.id.toolbar);
            } else {
                toolbar = (Toolbar) findToolbar;
            }
            toolbar.setContentInsetsAbsolute(0, 0);
            setSupportActionBar(toolbar);
        }
    }

    public void showBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wtf! why can dispatch click event on stopped activity?!
                if (isFragmentStateSaved()) {
                    return;
                }
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(null);
            toolbar = null;
        }
        super.onDestroy();
    }

    public void setCustomView(View view) {
        ensureToolbar();
        toolbar.setTitle(null);
        toolbar.removeAllViews();
        toolbar.addView(view);
    }
}
