package org.succlz123.s1go.app.ui.thread;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

/**
 * Created by fashi on 2015/4/15.
 */
public class ThreadInfoActivity extends BaseToolbarActivity  {

    public static final String TID = "tid";
    public static final String TITLE = "title";
    public static final String TOTAL_PAGER_NUM = "totalPagerNum";

    private String mTid;
    private String mToolbarTitle;
    private Toolbar mToolbar;
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
        setCustomTitle(getIntent().getStringExtra(TITLE));


        mViewPager = (ViewPager) findViewById(R.id.reviews_activity_viewpager);
        mReviewsViewPagerAdapter = new ReviewsViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mReviewsViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
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
    }

//    @Override
//    public void onReplies(int replies) {
//        this.mReply = replies;
//        mReviewsViewPagerAdapter.notifyDataSetChanged();
//        invalidateOptionsMenu();
//    }

    private class ReviewsViewPagerAdapter extends FragmentPagerAdapter {

        public ReviewsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //返回当前分页数
        @Override
        public int getCount() {
            if (mReply == 0) {
                return 1;
            }
            mTotalPagerNum = (int) Math.ceil((double) mReply / (double) 30);
            return mTotalPagerNum;
        }

        //得到每个item
        @Override
        public Fragment getItem(int position) {
            return ThreadInfoFragment.newInstance(mTid, position + 1, mTotalPagerNum);
        }

        //初始化每个页卡选项 PagerAapter选择哪个对象放在当前的ViewPager中
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }


//		//判断是否由该对象生成界面
//		@Override
//		public boolean isViewFromObject(View view, Object object) {
//			return false;
//		}
//
//		//从viewPager中移动当前的view
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
//		}
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
}
