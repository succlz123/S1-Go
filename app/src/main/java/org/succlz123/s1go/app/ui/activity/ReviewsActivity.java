package org.succlz123.s1go.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.utils.S1String;
import org.succlz123.s1go.app.ui.fragment.listview.ReviewsListListener;
import org.succlz123.s1go.app.ui.fragment.listview.ReviewsListFragment;

/**
 * Created by fashi on 2015/4/15.
 */
public class ReviewsActivity extends AppCompatActivity implements ReviewsListListener {
	private String mTid;
	private String mToolbarTitle;
	private Toolbar mToolbar;
	private int mReply;
	private int mTotalPagerNum;
	private int mViewPagerNum = 1;
 	private ViewPager mViewPager;
	private ReviewsViewPagerAdapter mReviewsViewPagerAdapter;

	public static void actionStart(Context context, String tid, String title, String reply) {
		Intent intent = new Intent(context, ReviewsActivity.class);
		intent.putExtra(S1String.TID, tid);
		intent.putExtra(S1String.TITLE, title);
		intent.putExtra(S1String.TOTALPAGERNUM, reply);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviews);
		initData();
		initViews();
		setToolbar();

		mReviewsViewPagerAdapter = new ReviewsViewPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mReviewsViewPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

	private void initData() {
		mTid = getIntent().getStringExtra(S1String.TID);
		mToolbarTitle = getIntent().getStringExtra(S1String.TITLE);
		mReply = Integer.valueOf(getIntent().getStringExtra(S1String.TOTALPAGERNUM));
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mViewPager = (ViewPager) findViewById(R.id.reviews_activity_viewpager);
	}

	private void setToolbar() {
		mToolbar.setTitle(mToolbarTitle);
//		mToolbar.setTitleTextColor(Color.WHITE);

//		mToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//			@Override
//			public void onGlobalLayout() {
//				View view = mToolbar.getChildAt(0);
//				if (view != null && view instanceof TextView) {
//					TextView x = (TextView) view;
//					x.setMaxEms(1);
//					mToolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//				}
//			}
//		});
//		TextView x = (TextView) mToolbar.getChildAt(1);
//		x.setMaxEms(5);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onReplies(int replies) {
		this.mReply = replies;
		mReviewsViewPagerAdapter.notifyDataSetChanged();
		invalidateOptionsMenu();
	}

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
			return ReviewsListFragment.newInstance(mTid, position + 1, mTotalPagerNum);
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
		MenuItem item = menu.add(Menu.NONE, Menu.FIRST, 100, "" + mViewPagerNum+" / "+mTotalPagerNum);
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
