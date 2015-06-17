package org.succlz123.s1go.app.ui.fragment.left;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.bean.hostthreads.HotThreadsDate;
import org.succlz123.s1go.app.support.bean.hostthreads.HotThreadsObject;
import org.succlz123.s1go.app.support.utils.S1Fid;
import org.succlz123.s1go.app.support.biz.GetHostThreads;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * Created by fashi on 2015/4/12.
 */
public class HotThreadsFragment extends Fragment {
	private ListView mListView;
	private List<HotThreadsDate> mHotThreadsDateList;
	private View mView;
	private HotThreadsAdapter mHotThreadsAdapter;
 	private SwipyRefreshLayout mSwipyRefreshLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.hotpost_fragment, container, false);
		initViews();
		setSwipyRefreshLayout();
 		new HotThreadsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		return mView;
	}

	private void setSwipyRefreshLayout() {
//		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
		mSwipyRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				if (direction == SwipyRefreshLayoutDirection.TOP) {
					new HotThreadsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
		});
	}

	private void initViews() {
		mListView = (ListView) mView.findViewById(R.id.hostpost_listview);
		mSwipyRefreshLayout = (SwipyRefreshLayout) mView.findViewById(R.id.swipyrefreshlayout);
	}

	private class HotThreadsAdapter extends BaseAdapter {

		private class ViewHolder {
			private TextView mTitle;
			private TextView mFid;
			private TextView mName;
			private TextView mTime;
			private TextView mLastPoster;
			private TextView mLastTime;
			private TextView mViews;
			private TextView mReply;
		}

		@Override
		public int getCount() {
			if (mHotThreadsDateList != null) {
				return mHotThreadsDateList.size();
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
				convertView = getActivity().getLayoutInflater().inflate(R.layout.hotpost_listview_item, parent, false);
				holder = new ViewHolder();

				holder.mTitle = (TextView) convertView.findViewById(R.id.hotpost_listview_title);
				holder.mFid = (TextView) convertView.findViewById(R.id.hotpost_listview_fid);
				holder.mName = (TextView) convertView.findViewById(R.id.hotpost_listview_name);
				holder.mTime = (TextView) convertView.findViewById(R.id.hotpost_listview_time);
				holder.mLastPoster = (TextView) convertView.findViewById(R.id.hotpost_listview_last_poster);
				holder.mLastTime = (TextView) convertView.findViewById(R.id.hotpost_listview_last_post_time);
				holder.mViews = (TextView) convertView.findViewById(R.id.hotpost_listview_Views);
				holder.mReply = (TextView) convertView.findViewById(R.id.hotpost_listview_reply);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HotThreadsDate hotThreadsDate = mHotThreadsDateList.get(position);

			holder.mTitle.setText(hotThreadsDate.getSubject());
			holder.mFid.setText("[" + S1Fid.GetS1Fid(hotThreadsDate.getFid()) + "]");
			holder.mName.setText(hotThreadsDate.getAuthor());
			holder.mTime.setText(Html.fromHtml(hotThreadsDate.getDateline()));
			holder.mLastPoster.setText(hotThreadsDate.getLastposter());
			holder.mLastTime.setText(Html.fromHtml(hotThreadsDate.getLastpost()));
			holder.mViews.setText(hotThreadsDate.getViews());
			holder.mReply.setText(hotThreadsDate.getReplies());

			return convertView;
		}
	}

	private class HotThreadsAsyncTask extends AsyncTask<Void, Void, HotThreadsObject> {
		private HashMap<String, String> mHearders = new HashMap<String, String>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (MyApplication.getInstance().getUserInfo() == null) {
			} else {
				String cookie = MyApplication.getInstance().getUserInfo().getCookiepre();
				String auth = "auth=" + Uri.encode(MyApplication.getInstance().getUserInfo().getAuth());
				String saltkey = "saltkey=" + MyApplication.getInstance().getUserInfo().getSaltkey();
				this.mHearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
			}
		}

		@Override
		protected HotThreadsObject doInBackground(Void... params) {
			return GetHostThreads.getHostPost(mHearders);
		}

		@Override
		protected void onPostExecute(HotThreadsObject aVoid) {
			super.onPostExecute(aVoid);
			List hotThreadsDate = aVoid.getVariables().getHotPostDateList();
			Collections.sort(hotThreadsDate, new Comparator<HotThreadsDate>() {
				@Override
				public int compare(HotThreadsDate lhs, HotThreadsDate rhs) {
					return rhs.getDblastpost() - lhs.getDblastpost();
				}
			});
			HotThreadsFragment.this.mHotThreadsDateList = hotThreadsDate;
			mHotThreadsAdapter = new HotThreadsAdapter();
			mListView.setAdapter(mHotThreadsAdapter);
			mHotThreadsAdapter.notifyDataSetChanged();
			mSwipyRefreshLayout.setRefreshing(false);
		}
	}
}
