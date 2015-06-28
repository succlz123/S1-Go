package org.succlz123.s1go.app.ui.fragment.listview;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.adapter.ThreadsListAdapter;
import org.succlz123.s1go.app.support.bean.login.LoginVariables;
import org.succlz123.s1go.app.support.bean.threads.ThreadsList;
import org.succlz123.s1go.app.support.bean.threads.ThreadsObject;
import org.succlz123.s1go.app.support.biz.GetThreads;
import org.succlz123.s1go.app.support.utils.S1String;
import org.succlz123.s1go.app.support.widget.swingindicator.SwingIndicator;
import org.succlz123.s1go.app.ui.activity.ReviewsActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fashi on 2015/6/25.
 */
public class ThreadsListFragment extends Fragment {
	private String mFid;
	private View mView;
	private ListView mListView;
	private ThreadsListAdapter mThreadsListAdapter;
	private List<ThreadsList> mThreadsList;
	private ThreadsListListener mThreadsListListener;
	private int mPage;

	private SwingIndicator mSwingIndicator;
	private SwipyRefreshLayout mSwipyRefreshLayout;
	private ProgressBar mProgressBar;

	public static ThreadsListFragment newInstance(String tid) {
		ThreadsListFragment threadsListFragment = new ThreadsListFragment();
		Bundle bundle = new Bundle();
		bundle.putString(S1String.FID, tid);
		threadsListFragment.setArguments(bundle);
		return threadsListFragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_threads_listview, container, false);
		initData();
		initViews();
		initListView();
		setSwipyRefreshLayout();

		return mView;
	}

	private void initData() {
		mFid = getArguments().getString(S1String.FID);
	}

	private void initViews() {
		mSwingIndicator = (SwingIndicator) mView.findViewById(R.id.threads_progress);
		mListView = (ListView) mView.findViewById(R.id.threads_activity_listview);
		mSwipyRefreshLayout = (SwipyRefreshLayout) mView.findViewById(R.id.swipyrefreshlayout);
		//回调给threadsActivity里的FloatActionButton用于绑定的listview
		mThreadsListListener.onListViewListener(mListView);
	}

	private void initListView() {
		mThreadsListAdapter = new ThreadsListAdapter();
		mListView.setAdapter(mThreadsListAdapter);
		GetThreadsListAsyncTask getThreadsListAsyncTask = new GetThreadsListAsyncTask();
		getThreadsListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		View view = getActivity().getLayoutInflater().inflate(R.layout.progressbar, mListView, false);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		mListView.addFooterView(mProgressBar);
		mProgressBar.setLayoutParams(new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, 0));

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				List<ThreadsList> threadsList = mThreadsListAdapter.getmThreadsList();

				ReviewsActivity.actionStart(
						getActivity(),
						threadsList.get(position).getTid(),
						threadsList.get(position).getSubject(),
						threadsList.get(position).getReplies());
			}
		});
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE &&
						view.getLastVisiblePosition() == (view.getCount() - 1)) {
					mProgressBar.setVisibility(View.VISIBLE);
					mProgressBar.setLayoutParams(new AbsListView.LayoutParams(
							AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
					GetThreadsListAsyncTask getThreadsListAsyncTask = new GetThreadsListAsyncTask();
					getThreadsListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mThreadsListListener = (ThreadsListListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "must implement threadsListCallback");
		}
	}

	private void setSwipyRefreshLayout() {
		mSwipyRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
		mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				if (direction == SwipyRefreshLayoutDirection.TOP) {
					mPage = 0;
					new GetThreadsListAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
//				else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
//					mPage++;
//					GetThreadsListAsyncTask getThreadsListAsyncTask = new GetThreadsListAsyncTask();
//					getThreadsListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//				}
			}
		});
	}

	private class GetThreadsListAsyncTask extends AsyncTask<Void, Void, ThreadsObject> {
		private HashMap<String, String> hearders = new HashMap<String, String>();
		private Boolean isLogin;
		private int pageNum;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LoginVariables userInfo = MyApplication.getInstance().mUserInfo;
			if (userInfo != null) {
				String cookie = userInfo.getCookiepre();
				String auth = S1String.AUTH + "=" + Uri.encode(userInfo.getAuth());
				String saltKey = S1String.SALT_KEY + "=" + userInfo.getSaltkey();
				this.hearders.put(S1String.COOKIE, cookie + auth + ";" + cookie + saltKey + ";");
			}
			pageNum = mPage + 1;
		}

		@Override
		protected ThreadsObject doInBackground(Void... params) {
			return GetThreads.getThreads(mFid, pageNum, hearders);
		}

		@Override
		protected void onPostExecute(ThreadsObject aVoid) {
			super.onPostExecute(aVoid);
			isLogin = (aVoid != null && aVoid.getMessage() == null);
			if (!isLogin) {
				Toast.makeText(MyApplication.getInstance().getApplicationContext(),
						MyApplication.getInstance().getApplicationContext().getString(R.string.sorry),
						Toast.LENGTH_LONG).show();
			} else if (isLogin) {
				mPage++;
				mThreadsList = aVoid.getVariables().getForum_threadlist();
				if (pageNum == 1) {
					mThreadsListAdapter.initmThreadsList(mThreadsList);
				} else {
					mThreadsListAdapter.setmThreadsList(mThreadsList);
				}
				mThreadsListAdapter.notifyDataSetChanged();

				mProgressBar.setLayoutParams(new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT, 0));

				mSwingIndicator.setVisibility(View.GONE);
				mSwipyRefreshLayout.setRefreshing(false);
				mThreadsListListener.onFloatingActionButton(true);
			}
		}
	}
}
