package org.succlz123.s1go.app.ui.activity;

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
import android.widget.*;
import com.melnykov.fab.FloatingActionButton;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.bean.threads.ThreadsList;
import org.succlz123.s1go.app.bean.threads.ThreadsObject;
import org.succlz123.s1go.app.dao.helper.S1Fid;
import org.succlz123.s1go.app.dao.interaction.GetThreads;
import org.succlz123.s1go.app.support.swingindicator.SwingIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fashi on 2015/4/14.
 */
public class ThreadsActivity extends SwipeBackActivity {
	private ListView mListView;
	private String mFid;
	private ThreadsObject threadsObject;
	private AppAdapet mApdater;
	private Toolbar mToolbar;
	private Boolean isLogin;
	private String ToolbarTitle;
	private List<ThreadsList> mThreadsList;
	private SwingIndicator mSwingIndicator;
	private FloatingActionButton mFloatingActionButton;
	private SwipeBackLayout mSwipeBackLayout;
	private SwipyRefreshLayout mSwipyRefreshLayout;
	private PtrFrameLayout mPtrFrame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.threads_activity);
		mFid = getIntent().getStringExtra("fid");
		initViews();
		setToolbar();
		setFloatingActionButton();
		setSwipeBack();
		new GetThreadsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ThreadsActivity.this, ReviewsActivity.class);
				intent.putExtra("tid", mThreadsList.get(position).getTid());
				intent.putExtra("title", mThreadsList.get(position).getSubject());
				startActivity(intent);
			}
		});

		mPtrFrame=(PtrFrameLayout)findViewById(R.id.ptr);
		mPtrFrame.setResistance(1.7f);
		mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
		mPtrFrame.setDurationToClose(200);
		mPtrFrame.setDurationToCloseHeader(1000);
// default is false
		mPtrFrame.setPullToRefresh(false);
// default is true
		mPtrFrame.setKeepHeaderWhenRefresh(true);
		mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
				mPtrFrame.refreshComplete();
				Toast.makeText(ThreadsActivity.this,"cccc",Toast.LENGTH_SHORT).show();
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
				return super.checkCanDoRefresh(frame, content, header);
			}
		});

//		mSwipyRefreshLayout.setColorSchemeResources(
//				android.R.color.holo_blue_light,
//				android.R.color.holo_red_light,
//				android.R.color.holo_orange_light,
//				android.R.color.holo_green_light);
//		mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
//			@Override
//			public void onRefresh(SwipyRefreshLayoutDirection direction) {
//				Log.d("MainActivity", "Refresh triggered at "
//						+ (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
//				if (direction == SwipyRefreshLayoutDirection.TOP) {
//					new GetThreadsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//				} else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
//
//				}
//			}
//		});
	}

	private void setSwipeBack() {
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mSwingIndicator = (SwingIndicator) findViewById(R.id.threads_progress);
		mFloatingActionButton = (FloatingActionButton) findViewById(R.id.thread_fab);
		mListView = (ListView) findViewById(R.id.threads_base_activity_listview);
//		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.ptr);
	}

	private void setToolbar() {
		ToolbarTitle = S1Fid.GetS1Fid(Integer.valueOf(mFid));
		mToolbar.setTitle(ToolbarTitle);
		mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
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
				Intent intent = new Intent(ThreadsActivity.this, SetThreadsActivity.class);
				intent.putExtra("fid", mFid);
				intent.putExtra("formhash", threadsObject.getVariables().getFormhash());
				startActivityForResult(intent, 1);
			}
		});
		mFloatingActionButton.attachToListView(mListView);
	}

	private class AppAdapet extends BaseAdapter {

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
			if (threadsObject != null) {
				return threadsObject.getVariables().getForum_threadlist().size();
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
				convertView = getLayoutInflater().inflate(R.layout.threads_listview_item, parent, false);
				holder = new ViewHolder();
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
			mThreadsList = new ArrayList<ThreadsList>();
			mThreadsList = threadsObject.getVariables().getForum_threadlist();
			holder.mTitle.setText(mThreadsList.get(position).getSubject());
			holder.name.setText(mThreadsList.get(position).getAuthor());
			holder.time.setText(Html.fromHtml(mThreadsList.get(position).getDateline()));
			holder.lastTime.setText(Html.fromHtml(mThreadsList.get(position).getLastpost()));
			holder.lastPoster.setText(mThreadsList.get(position).getLastposter());
			holder.reply.setText(mThreadsList.get(position).getReplies());
			holder.click.setText(mThreadsList.get(position).getViews());
			holder.fid.setText(null);

			return convertView;
		}
	}

	private class GetThreadsAsyncTask extends AsyncTask<Void, Void, ThreadsObject> {
		private HashMap<String, String> hearders = new HashMap<String, String>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (MyApplication.getInstance().getUserInfo() != null) {
				String cookie = MyApplication.getInstance().getUserInfo().getCookiepre();
				String auth = "auth=" + Uri.encode(MyApplication.getInstance().getUserInfo().getAuth());
				String saltkey = "saltkey=" + MyApplication.getInstance().getUserInfo().getSaltkey();
				this.hearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
			}
		}

		@Override
		protected ThreadsObject doInBackground(Void... params) {

			return GetThreads.getThreads(mFid, hearders);
		}

		@Override
		protected void onPostExecute(ThreadsObject aVoid) {
			super.onPostExecute(aVoid);
			threadsObject = aVoid;
			isLogin = (threadsObject != null && threadsObject.getMessage() == null);
			if (!isLogin) {
				Toast.makeText(ThreadsActivity.this, "抱歉，您尚未登录，没有权限访问该版块", Toast.LENGTH_LONG).show();
			} else if (isLogin) {
				mApdater = new AppAdapet();
				mListView.setAdapter(mApdater);
				mSwingIndicator.setVisibility(View.GONE);
				mFloatingActionButton.setVisibility(View.VISIBLE);
				mApdater.notifyDataSetChanged();
//				mSwipyRefreshLayout.setRefreshing(false);
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