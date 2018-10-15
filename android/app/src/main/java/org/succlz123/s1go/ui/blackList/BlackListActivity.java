package org.succlz123.s1go.ui.blackList;

import org.succlz123.s1go.R;
import org.succlz123.s1go.ui.base.BaseToolbarActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by succlz123 on 2016/11/20.
 */

public class BlackListActivity extends BaseToolbarActivity {
    private BlackListFragment mBlackListFragment;

    public static void start(Context context) {
        Intent intent = new Intent(context, BlackListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_list);
        showBackButton();
        ensureToolbar();
        setTitle("个人黑名单");
        setUpFragment();
    }

    private void setUpFragment() {
        mBlackListFragment = (BlackListFragment) getSupportFragmentManager().findFragmentByTag(BlackListFragment.TAG);
        if (mBlackListFragment == null) {
            mBlackListFragment = BlackListFragment.newInstance();
        }
        if (!mBlackListFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, mBlackListFragment, BlackListFragment.TAG)
                    .commit();
        }
    }
}