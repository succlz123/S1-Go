package org.succlz123.s1go.app.ui.fragment.list;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.bean.login.LoginVariables;
import org.succlz123.s1go.app.support.bean.reviews.ReviewsList;
import org.succlz123.s1go.app.support.bean.reviews.ReviewsObject;
import org.succlz123.s1go.app.support.biz.GetReviews;
import org.succlz123.s1go.app.support.biz.fromhtml.ImageLinkParser;
import org.succlz123.s1go.app.support.biz.fromhtml.SpannedImageGetter;
import org.succlz123.s1go.app.support.utils.AppSize;
import org.succlz123.s1go.app.support.utils.ImageLoader;
import org.succlz123.s1go.app.support.utils.S1String;
import org.succlz123.s1go.app.support.utils.S1UidToAvatarUrl;
import org.succlz123.s1go.app.support.widget.swingindicator.SwingIndicator;
import org.succlz123.s1go.app.ui.activity.SetReviewsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fashi on 2015/6/13.
 */
public class ReviewsListFragment extends Fragment {
	private String mTid;
	private int mCurrentpagerNum;
	private int mTotalPagerNum;

	private ListView mListView;
	private ReviewsObject reviewsObject;
	private ReviewsListViewAdapter mReviewsListViewAdapter;
	private List<ReviewsList> mReviewsList;
	private View mView;

	private AppSize mAppSize;
	private SwingIndicator mSwingIndicator;
	private FloatingActionButton mFloatingActionButton;
	private SwipyRefreshLayout mSwipyRefreshLayout;

	private ViewPagerNumCallback mViewPagerNumCallback;

	public static ReviewsListFragment newInstance(String tid, int currentpagerNum, int totalPagerNum) {
		ReviewsListFragment reviewsListFragment = new ReviewsListFragment();
		Bundle bundle = new Bundle();
		bundle.putString(S1String.TID, tid);
		bundle.putInt(S1String.CURRENTPAGERNUM, currentpagerNum);
		bundle.putInt(S1String.TOTALPAGERNUM, totalPagerNum);
		reviewsListFragment.setArguments(bundle);
		return reviewsListFragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.reviews_fragment_listview, container, false);
		initData();
		initViews();
		if (mTid != null) {
			new GetReviewsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		setFloatingActionButton();
		setSwipyRefreshLayout();

		return mView;
	}

	private void initData() {
		mTid = getArguments().getString(S1String.TID);
		mCurrentpagerNum = getArguments().getInt(S1String.CURRENTPAGERNUM);
		mTotalPagerNum = getArguments().getInt(S1String.TOTALPAGERNUM);
	}

	private void initViews() {
		mFloatingActionButton = (FloatingActionButton) mView.findViewById(R.id.reviews_fab);
		mSwingIndicator = (SwingIndicator) mView.findViewById(R.id.reviews_progress);
		mListView = (ListView) mView.findViewById(R.id.reviews_activity_listview);
		mSwipyRefreshLayout = (SwipyRefreshLayout) mView.findViewById(R.id.swipyrefreshlayout);
	}

	private void setFloatingActionButton() {
		mFloatingActionButton.setShadow(true);
		mFloatingActionButton.setType(FloatingActionButton.TYPE_NORMAL);
		mFloatingActionButton.setColorNormal(getResources().getColor(R.color.base));
		mFloatingActionButton.attachToListView(mListView);//把listview和浮动imagebutton组合
		mFloatingActionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SetReviewsActivity.actionStart(getActivity(), mTid, reviewsObject.getVariables().getFormhash());
			}
		});
	}

	private void setSwipyRefreshLayout() {
		mSwipyRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);

//		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
		mSwipyRefreshLayout.setEnabled(false);
//		mSwipyRefreshLayout.setRefreshing();
		if (mCurrentpagerNum == mTotalPagerNum){
			mSwipyRefreshLayout.setEnabled(true);

			mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
		}

		mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				if (direction == SwipyRefreshLayoutDirection.TOP) {
					Toast.makeText(getActivity(), "3333333", Toast.LENGTH_SHORT).show();
					mSwipyRefreshLayout.setRefreshing(false);
				} else
				if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
//					new GetReviewsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					Toast.makeText(getActivity(),"123123",Toast.LENGTH_SHORT).show();
					mSwipyRefreshLayout.setRefreshing(false);
				}
			}
		});
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y / 2;
		mAppSize = new AppSize(screenWidth, screenHeight);
		Log.e("ReviewsActivity W_H", mAppSize.getWidth() + "_" + mAppSize.getHeight());
	}

	private class ReviewsListViewAdapter extends BaseAdapter {

		private class ViewHolder {
			private ImageView mAvatarImg;
			private TextView mName;
			private TextView mTime;
			private TextView mNum;
			private TextView mReviews;
		}

		@Override
		public int getCount() {
			if (reviewsObject != null) {
				return reviewsObject.getVariables().getPostlist().size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.reviews_listview_item, parent, false);
				holder = new ViewHolder();
				holder.mAvatarImg = (ImageView) convertView.findViewById(R.id.reviews_listview_item_img);
				holder.mName = (TextView) convertView.findViewById(R.id.reviews_listview_item_name);
				holder.mTime = (TextView) convertView.findViewById(R.id.reviews_listview_item_time);
				holder.mNum = (TextView) convertView.findViewById(R.id.reviews_listview_item_num);
				holder.mReviews = (TextView) convertView.findViewById(R.id.reviews_listview_item_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			mReviewsList = new ArrayList<ReviewsList>();
			mReviewsList = reviewsObject.getVariables().getPostlist();
			final String mAvatarUrl = S1UidToAvatarUrl.getAvatar(mReviewsList.get(position).getAuthorid());
			holder.mAvatarImg.setTag(mAvatarUrl);
			holder.mAvatarImg.setImageResource(R.drawable.noavatar);
			if (mAppSize != null) {
				final ViewHolder finalHolder = holder;
				ImageLoader.getInstance().loadBitmap(mAvatarUrl, mAppSize, new ImageLoader.CallBack() {
					@Override
					public void onLoad(String url, Bitmap bitmap) {
						if (mAvatarUrl.equals(finalHolder.mAvatarImg.getTag())) {
							finalHolder.mAvatarImg.setImageBitmap(bitmap);
						}
					}

					@Override
					public void onError(String url) {
					}
				});
			}
			holder.mName.setText(mReviewsList.get(position).getAuthor());
			holder.mTime.setText(Html.fromHtml(mReviewsList.get(position).getDateline()));
			if (mCurrentpagerNum == 1) {
				if (position == 0) {
					holder.mNum.setText("楼主");
				} else if (position > 0) {
					holder.mNum.setText("" + position + "楼");
				}
			} else {
				holder.mNum.setText("" + ((30 * (mCurrentpagerNum - 1)) + position) + "楼");
			}
			holder.mReviews.setMovementMethod(ImageLinkParser.getInstance());
			String reply = mReviewsList.get(position).getMessage();
			if (TextUtils.isEmpty(reply)) {
				holder.mReviews.setText(null);
			} else {
				Spanned spanned = Html.fromHtml(reply, new SpannedImageGetter(holder.mReviews, mAppSize), null);
				holder.mReviews.setText(spanned);
			}
			return convertView;
		}
	}

	private class GetReviewsAsyncTask extends AsyncTask<Void, Void, ReviewsObject> {
		private HashMap<String, String> mHearders = new HashMap<String, String>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LoginVariables userInfo = MyApplication.getInstance().mUserInfo;
			if (userInfo != null) {
				String cookie = userInfo.getCookiepre();
				String auth = S1String.AUTH + "=" + Uri.encode(userInfo.getAuth());
				String saltKey = S1String.SALT_KEY + "=" + userInfo.getSaltkey();
				this.mHearders.put(S1String.COOKIE, cookie + auth + ";" + cookie + saltKey + ";");
			}
		}

		@Override
		protected ReviewsObject doInBackground(Void... params) {
			return GetReviews.getReviews(mTid, mCurrentpagerNum, mHearders);
		}

		@Override
		protected void onPostExecute(ReviewsObject aVoid) {
			super.onPostExecute(aVoid);
			reviewsObject = aVoid;
			mReviewsListViewAdapter = new ReviewsListViewAdapter();
			mListView.setAdapter(mReviewsListViewAdapter);
			mReviewsListViewAdapter.notifyDataSetChanged();

			ViewPagerNumCallback callback = (ViewPagerNumCallback) getActivity();


			mSwingIndicator.setVisibility(View.GONE);
			mFloatingActionButton.setVisibility(View.VISIBLE);
		}
	}
}
