package org.succlz123.s1go.app.ui.thread.info;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by succlz123 on 2015/4/15.
 */
public class ThreadInfoActivity extends BaseToolbarActivity {
    public static final String TID = "tid";
    public static final String TITLE = "title";
    public static final String TOTAL_PAGER_NUM = "totalPagerNum";

    private String mTid;
    private int mReply;
    private int mTotalPagerNum;
    private int mViewPagerNum = 1;
    private ViewPager mViewPager;
    private ReviewsViewPagerAdapter mReviewsViewPagerAdapter;

    public static void newInstance(Context context, String tid, String title, String reply) {
        Intent intent = new Intent(context, ThreadInfoActivity.class);
        intent.putExtra(TID, tid);
        intent.putExtra(TITLE, title);
        intent.putExtra(TOTAL_PAGER_NUM, reply);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_info);

        if (getIntent() == null) {
            return;
        }
        mTid = getIntent().getStringExtra(TID);
        mReply = Integer.valueOf(getIntent().getStringExtra(TOTAL_PAGER_NUM));

        showBackButton();
        setTitle(getIntent().getStringExtra(TITLE));

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mReviewsViewPagerAdapter = new ReviewsViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mReviewsViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    mViewPagerNum = mViewPager.getCurrentItem() + 1;
                    invalidateOptionsMenu();
                }
            }
        });

//        final GestureDetector detector = new GestureDetector(ThreadInfoActivity.this, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return super.onSingleTapUp(e);
//            }
//
//            @Override
//            public boolean onDoubleTapEvent(MotionEvent e) {
//                return super.onDoubleTapEvent(e);
//            }
//
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                if (mThreadListFragment != null) {
//                    mThreadListFragment.goToTop();
//                    ToastUtils.showToastShort(ThreadInfoActivity.this, "已经返回顶部");
//                }
//                return super.onDoubleTap(e);
//            }
//        });
//        getToolbar().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return detector.onTouchEvent(event);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, Menu.FIRST, 100, "" + mViewPagerNum + " / " + mTotalPagerNum);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReviewsViewPagerAdapter = null;
    }

    private class ReviewsViewPagerAdapter extends FragmentStatePagerAdapter {

        public ReviewsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            if (mReply == 0) {
                return 1;
            }
            mTotalPagerNum = (int) Math.ceil((double) mReply / (double) 30);
            return mTotalPagerNum;
        }

        @Override
        public Fragment getItem(int position) {
            return ThreadInfoFragment.newInstance(mTid, position + 1, mTotalPagerNum);
        }
    }
}
