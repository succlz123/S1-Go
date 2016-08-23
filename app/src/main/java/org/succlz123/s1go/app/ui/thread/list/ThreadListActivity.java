package org.succlz123.s1go.app.ui.thread.list;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.s1.S1Fid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by succlz123 on 2015/4/14.
 */
public class ThreadListActivity extends BaseToolbarActivity {
    public static final String KEY_THREAD_ACTIVITY_FID = "key_activity_thread_fid";

    private String mFid;
    private ThreadListFragment mThreadListFragment;

    public static void start(Context context, String fid) {
        Intent intent = new Intent(context, ThreadListActivity.class);
        intent.putExtra(KEY_THREAD_ACTIVITY_FID, fid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_list);
        mFid = getIntent().getStringExtra(KEY_THREAD_ACTIVITY_FID);

        showBackButton();
        ensureToolbar();
        setTitle(S1Fid.getS1Fid(Integer.valueOf(mFid)));
        setUpFragment();
    }

    private void setUpFragment() {
        mThreadListFragment = (ThreadListFragment) getSupportFragmentManager().findFragmentByTag(ThreadListFragment.TAG);
        if (mThreadListFragment == null) {
            mThreadListFragment = ThreadListFragment.newInstance(mFid);
        }
        if (!mThreadListFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, mThreadListFragment, ThreadListFragment.TAG)
                    .commit();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuItem item = menu.add(Menu.NONE, Menu.FIRST, 100, "主题");
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}