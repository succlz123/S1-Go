package org.succlz123.s1go.app.ui.thread.list;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.common.ToastUtils;
import org.succlz123.s1go.app.utils.s1.S1Fid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

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

    private Task mTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_list);
        mFid = getIntent().getStringExtra(KEY_THREAD_ACTIVITY_FID);

        showBackButton();
        ensureToolbar();
        setTitle(S1Fid.getS1Fid(Integer.valueOf(mFid)));
        setUpListFragment();

        mTask=  Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.currentThread().sleep(10000);
                return null;
            }
        }).onSuccessTask(new Continuation<Object, Task<Object>>() {
            @Override
            public Task<Object> then(Task<Object> task) throws Exception {
                Log.d("", "");
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

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
        mTask=null;
        super.onDestroy();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
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
        new ThreadListPresenter(mFid, new ThreadListDataSource(), mThreadListFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, Menu.FIRST, 100, "字号");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case 100:
                mThreadListFragment.xx();
                ToastUtils.showToastShort(this, "额,有空在说.");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}