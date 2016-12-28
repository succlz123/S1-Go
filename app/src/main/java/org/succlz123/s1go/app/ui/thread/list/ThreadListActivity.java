package org.succlz123.s1go.app.ui.thread.list;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.common.ToastUtils;
import org.succlz123.s1go.app.utils.s1.S1Fid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by succlz123 on 2015/4/14.
 */
public class ThreadListActivity extends BaseToolbarActivity {
    public static final String KEY_THREAD_ACTIVITY_FID = "key_activity_thread_fid";

    private String mFid;
    private String[] mChildFid;

    private ThreadListPresenter mPresenter;
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
        setTitle(S1Fid.getS1FidName(mFid));
        setUpListFragment();

        final GestureDetector detector = new GestureDetector(ThreadListActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mThreadListFragment != null) {
                    mThreadListFragment.goToTop();
                    ToastUtils.showToastShort(ThreadListActivity.this, "已经返回顶部");
                }
                return super.onDoubleTap(e);
            }
        });
        getToolbar().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mPresenter = null;
        super.onDestroy();
    }

    private void setUpListFragment() {
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
        mPresenter = new ThreadListPresenter(mFid, new ThreadListDataSource(), mThreadListFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mChildFid = S1Fid.getS1ChildFidName(mFid);
        if (mChildFid != null && mChildFid.length > 0) {
            for (int i = 0; i < mChildFid.length; i++) {
                String s = mChildFid[i];
                int position = i;
                menu.add(Menu.NONE, position++, 100, s);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                String s = mChildFid[id];
                if (mPresenter != null) {
                    mPresenter.setFid(S1Fid.getFidFormName(s));
                    mPresenter.loadThreadList();
                }
                ToastUtils.showToastShort(this, s);
//            case 100:
//                mThreadListFragment.xx();
//                ToastUtils.showToastShort(this, "额,有空在说.");
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}