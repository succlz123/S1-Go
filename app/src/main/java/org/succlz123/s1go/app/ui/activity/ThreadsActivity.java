package org.succlz123.s1go.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.bean.login.LoginVariables;
import org.succlz123.s1go.app.support.bean.threads.ThreadsList;
import org.succlz123.s1go.app.support.bean.threads.ThreadsObject;
import org.succlz123.s1go.app.support.biz.GetThreads;
import org.succlz123.s1go.app.support.utils.S1Fid;
import org.succlz123.s1go.app.support.utils.S1String;
import org.succlz123.s1go.app.support.widget.swingindicator.SwingIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by fashi on 2015/4/14.
 */
public class ThreadsActivity extends SwipeBackActivity {
	private ListView mListView;
	private String mFid;
	private ThreadsObject threadsObject;
	private ThreadsAdapter mThreadsAdapter;
	private Toolbar mToolbar;
	private Boolean isLogin;
	private String ToolbarTitle;
	private List<ThreadsList> mThreadsList;

	private SwingIndicator mSwingIndicator;
	private FloatingActionButton mFloatingActionButton;
	private SwipeBackLayout mSwipeBackLayout;
	private SwipyRefreshLayout mSwipyRefreshLayout;

	public static void actionStart(Context context, String fid) {
		Intent intent = new Intent(context, ThreadsActivity.class);
		intent.putExtra(S1String.FID, fid);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.threads_activity);
		initData();
		initViews();
		setToolbar();
		setFloatingActionButton();
		setSwipeBack();
		new GetThreadsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		setListView();
		setSwipyRefreshLayout();
	}

	private void setSwipyRefreshLayout() {
		mSwipyRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				if (direction == SwipyRefreshLayoutDirection.TOP) {
					new GetThreadsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
					Toast.makeText(ThreadsActivity.this, "cccc", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initData() {
		mFid = getIntent().getStringExtra(S1String.FID);
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mSwingIndicator = (SwingIndicator) findViewById(R.id.threads_progress);
		mFloatingActionButton = (FloatingActionButton) findViewById(R.id.thread_fab);
		mListView = (ListView) findViewById(R.id.threads_activity_listview);
		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
	}

	private void setSwipeBack() {
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
	}

	private void setToolbar() {
		ToolbarTitle = S1Fid.GetS1Fid(Integer.valueOf(mFid));
		mToolbar.setTitle(ToolbarTitle);
		mToolbar.setTitleTextColor(Color.WHITE);
		mToolbar.setSubtitleTextColor(Color.WHITE);
		mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void setFloatingActionButton() {
		mFloatingActionButton.setShadow(true);
		mFloatingActionButton.setType(FloatingActionButton.TYPE_NORMAL);
		mFloatingActionButton.setColorNormal(getResources().getColor(R.color.base));
		mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				SetThreadsActivity.actionStart(ThreadsActivity.this,mFid,threadsObject.getVariables().getFormhash());
				SetThreadsActivity.actionStart(ThreadsActivity.this, mFid, MyApplication.getInstance().getUserInfo().getFormhash());

			}
		});
		mFloatingActionButton.attachToListView(mListView);
	}

	private void setListView() {
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ReviewsActivity.actionStart(
						ThreadsActivity.this,
						mThreadsList.get(position).getTid(),
						mThreadsList.get(position).getSubject(),
						mThreadsList.get(position).getReplies());
			}
		});
	}

	private class ThreadsAdapter extends BaseAdapter {

		private class ViewHolder {
			private TextView mTitle;
			private TextView name;
			private TextView time;
			private TextView lastTime;
			private TextView lastPoster;
			private TextView reply;
			private TextView click;
			private TextView fid;
		}

		@Override
		public int getCount() {
			if (mThreadsList != null) {
				return mThreadsList.size();
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
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.threads_listview_item, parent, false);
				holder.mTitle = (TextView) convertView.findViewById(R.id.threads_listview_title);
				holder.name = (TextView) convertView.findViewById(R.id.threads_listview_name);
				holder.time = (TextView) convertView.findViewById(R.id.threads_listview_time);
				holder.lastTime = (TextView) convertView.findViewById(R.id.threads_listview_last_post_time);
				holder.lastPoster = (TextView) convertView.findViewById(R.id.threads_listview_last_poster);
				holder.reply = (TextView) convertView.findViewById(R.id.threads_listview_reply);
				holder.click = (TextView) convertView.findViewById(R.id.threads_listview_click);
				holder.fid = (TextView) convertView.findViewById(R.id.threads_listview_fid);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ThreadsList threadsList = mThreadsList.get(position);

			holder.mTitle.setText(threadsList.getSubject());
			holder.name.setText(threadsList.getAuthor());
			holder.time.setText(Html.fromHtml(threadsList.getDateline()));
			holder.lastTime.setText(Html.fromHtml(threadsList.getLastpost()));
			holder.lastPoster.setText(threadsList.getLastposter());
			holder.reply.setText(threadsList.getReplies());
			holder.click.setText(threadsList.getViews());
			holder.fid.setText(null);

			return convertView;
		}
	}

	private class GetThreadsAsyncTask extends AsyncTask<Void, Void, ThreadsObject> {
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
		protected ThreadsObject doInBackground(Void... params) {
			return GetThreads.getThreads(mFid, mHearders);
		}

		@Override
		protected void onPostExecute(ThreadsObject aVoid) {
			super.onPostExecute(aVoid);
			threadsObject = aVoid;
			isLogin = (threadsObject != null && threadsObject.getMessage() == null);
			if (!isLogin) {
				Toast.makeText(ThreadsActivity.this, getString(R.string.sorry), Toast.LENGTH_LONG).show();
			} else if (isLogin) {
				List threadsLists = aVoid.getVariables().getForum_threadlist();
//				Collections.sort(threadsLists, new Comparator<ThreadsList>() {
//					@Override
//					public int compare(ThreadsList lhs, ThreadsList rhs) {
//						return rhs.getDblastpost() - lhs.getDblastpost();
//					}
//				});
				List<Integer> xx= new ArrayList();
				Collections.sort(xx, new Comparator<Integer>() {
					@Override
					public int compare(Integer lhs, Integer rhs) {
						return 0;
					}
				});

				ThreadsActivity.this.mThreadsList = threadsLists;
				mThreadsAdapter = new ThreadsAdapter();
				mListView.setAdapter(mThreadsAdapter);
				mSwingIndicator.setVisibility(View.GONE);
				mFloatingActionButton.setVisibility(View.VISIBLE);
				mThreadsAdapter.notifyDataSetChanged();
				mSwipyRefreshLayout.setRefreshing(false);
			}
		}
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