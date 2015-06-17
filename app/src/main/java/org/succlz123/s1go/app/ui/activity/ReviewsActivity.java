package org.succlz123.s1go.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.bean.reviews.ReviewsList;
import org.succlz123.s1go.app.support.bean.reviews.ReviewsObject;
import org.succlz123.s1go.app.support.utils.S1String;
import org.succlz123.s1go.app.ui.fragment.list.ReviewsListFragment;
import org.succlz123.s1go.app.ui.fragment.list.ViewPagerNumCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by fashi on 2015/4/15.
 */
public class ReviewsActivity extends SwipeBackActivity implements
		ViewPagerNumCallback {
	private String mTid;
	private String mToolbarTitle;
	private Toolbar mToolbar;
	private int mReply;
	private int mTotalPagerNum;

	private ReviewsObject reviewsObject;
	private List<ReviewsList> mReviewsList;

	private SwipeBackLayout mSwipeBackLayout;

	private ViewPager mViewPager;
	private int mViewPagerNum;
	private ReviewsViewPagerAdapter mReviewsViewPagerAdapter;
	private ReviewsListFragment mReviewsListFragment;
	private List<ReviewsListFragment> mFragmentList;

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
		setContentView(R.layout.reviews_activity);
		initData();
		initViews();
		setToolbar();
		setSwipeBack();

//		mFragmentList = new ArrayList<>();
//		mReviewsListFragment = ReviewsListFragment.newInstance(mTid);
//		mFragmentList.add(mReviewsListFragment);
//		mFragmentList.add(mReviewsListFragment);
//		for(int i=0;i<mViewPagerNum;i++){
//			mFragmentList.add(mReviewsListFragment);
//		}
List list=new ArrayList();
		Collections.sort();
		Collections.reverse();
		mViewPager.setAdapter(new ReviewsViewPagerAdapter(getSupportFragmentManager()));
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

			}
		});
//		mViewPager.notifyDataSetChanged;
	}

	private void setSwipeBack() {
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
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
		mToolbar.setTitleTextColor(Color.WHITE);
		mToolbar.setSubtitleTextColor(Color.WHITE);
		mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onPagerNum(int pagerNum) {
		this.mViewPagerNum = pagerNum;
	}

	private class ReviewsViewPagerAdapter extends FragmentPagerAdapter {

		public ReviewsViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		//返回当前分页数
		@Override
		public int getCount() {
			mTotalPagerNum = (int) Math.ceil((double) mReply / (double) 30);
			return mTotalPagerNum;
		}

		//得到每个item
		@Override
		public Fragment getItem(int position) {
			return ReviewsListFragment.newInstance(mTid, position + 1,mTotalPagerNum);
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

//	private class ReviewsListViewAdapter extends BaseAdapter {
//
//		private class ViewHolder {
//			private ImageView mAvatarImg;
//			private TextView mName;
//			private TextView mTime;
//			private TextView mNum;
//			private TextView mReviews;
//		}
//
//		@Override
//		public int getCount() {
//			if (reviewsObject != null) {
//				return reviewsObject.getVariables().getPostlist().size();
//			}
//			return 0;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder = null;
//			if (convertView == null) {
//				convertView = getLayoutInflater().inflate(R.layout.reviews_listview_item, parent, false);
//				holder = new ViewHolder();
//				holder.mAvatarImg = (ImageView) convertView.findViewById(R.id.reviews_listview_item_img);
//				holder.mName = (TextView) convertView.findViewById(R.id.reviews_listview_item_name);
//				holder.mTime = (TextView) convertView.findViewById(R.id.reviews_listview_item_time);
//				holder.mNum = (TextView) convertView.findViewById(R.id.reviews_listview_item_num);
//				holder.mReviews = (TextView) convertView.findViewById(R.id.reviews_listview_item_content);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			mReviewsList = new ArrayList<ReviewsList>();
//			mReviewsList = reviewsObject.getVariables().getPostlist();
//			final String mAvatarUrl = S1UidToAvatarUrl.getAvatar(mReviewsList.get(position).getAuthorid());
//			holder.mAvatarImg.setTag(mAvatarUrl);
//			holder.mAvatarImg.setImageResource(R.drawable.noavatar);
//			if (mAppSize != null) {
//				final ViewHolder finalHolder = holder;
//				ImageLoader.getInstance().loadBitmap(mAvatarUrl, mAppSize, new ImageLoader.CallBack() {
//					@Override
//					public void onLoad(String url, Bitmap bitmap) {
//						if (mAvatarUrl.equals(finalHolder.mAvatarImg.getTag())) {
//							finalHolder.mAvatarImg.setImageBitmap(bitmap);
//						}
//					}
//
//					@Override
//					public void onError(String url) {
//					}
//				});
//			}
//			holder.mName.setText(mReviewsList.get(position).getAuthor());
//			holder.mTime.setText(Html.fromHtml(mReviewsList.get(position).getDateline()));
//			if (position == 0) {
//				holder.mNum.setText("楼主");
//			} else if (position > 0) {
//				holder.mNum.setText("" + position + "楼");
//			}
//
//			holder.mReviews.setMovementMethod(ImageLinkParser.getInstance());
//			String reply = mReviewsList.get(position).getMessage();
//			if (TextUtils.isEmpty(reply)) {
//				holder.mReviews.setText(null);
//			} else {
//				Spanned spanned = Html.fromHtml(reply, new SpannedImageGetter(holder.mReviews, mAppSize), null);
//				holder.mReviews.setText(spanned);
//			}
//			return convertView;
//		}
//	}

//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		Point size = new Point();
//		getWindowManager().getDefaultDisplay().getSize(size);
//		int screenWidth = size.x;
//		int screenHeight = size.y / 2;
//		mAppSize = new AppSize(screenWidth, screenHeight);
//		Log.e("ReviewsActivity W_H", mAppSize.getWidth() + "_" + mAppSize.getHeight());
//	}

//	private class GetReviewsAsyncTask extends AsyncTask<Void, Void, ReviewsObject> {
//		private HashMap<String, String> mHearders = new HashMap<String, String>();
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			LoginVariables userInfo = MyApplication.getInstance().mUserInfo;
//			if (userInfo != null) {
//				String cookie = userInfo.getCookiepre();
//				String auth = S1String.AUTH + "=" + Uri.encode(userInfo.getAuth());
//				String saltKey = S1String.SALT_KEY + "=" + userInfo.getSaltkey();
//				this.mHearders.put(S1String.COOKIE, cookie + auth + ";" + cookie + saltKey + ";");
//			}
//
////			if (MyApplication.getInstance().getUserInfo() == null) {
////			} else {
////				String cookie = MyApplication.getInstance().getUserInfo().getCookiepre();
////				String auth = "auth=" + Uri.encode(MyApplication.getInstance().getUserInfo().getAuth());
////				String saltkey = "saltkey=" + MyApplication.getInstance().getUserInfo().getSaltkey();
////				this.hearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
////			}
//		}
//
//		@Override
//		protected ReviewsObject doInBackground(Void... params) {
//			return GetReviews.getReviews(mTid, mHearders);
//		}
//
//		@Override
//		protected void onPostExecute(ReviewsObject aVoid) {
//			super.onPostExecute(aVoid);
//			reviewsObject = aVoid;
//			mReviewsListViewAdapter = new ReviewsListViewAdapter();
//			mListView.setAdapter(mReviewsListViewAdapter);
//			mReviewsListViewAdapter.notifyDataSetChanged();
//			mSwingIndicator.setVisibility(View.GONE);
//			mFloatingActionButton.setVisibility(View.VISIBLE);
//		}
//	}

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
