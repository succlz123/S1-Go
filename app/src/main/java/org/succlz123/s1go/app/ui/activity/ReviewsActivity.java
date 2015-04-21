package org.succlz123.s1go.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.melnykov.fab.FloatingActionButton;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.thread.ThreadObject;
import org.succlz123.s1go.app.bean.thread.ThreadPostlist;
import org.succlz123.s1go.app.dao.Api.GetAvatarApi;
import org.succlz123.s1go.app.dao.Api.ReviewsApi;
import org.succlz123.s1go.app.dao.ImageLinkParser;
import org.succlz123.s1go.app.dao.URLImageParser;
import org.succlz123.s1go.app.support.swingindicator.SwingIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fashi on 2015/4/15.
 */
public class ReviewsActivity extends ActionBarActivity {
	private String mTid;
	private String mToolbarTitle;
	private ListView mListView;
	private ThreadObject threadObject;
	private AppAdapet mApdater;
	private List<ThreadPostlist> mThreadPostlist;
	private Toolbar mToolbar;
	private SwingIndicator mSwingIndicator;

	private FloatingActionButton mFloatingActionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviews_activity);
		mTid = getIntent().getStringExtra("tid");
		mToolbarTitle = getIntent().getStringExtra("title");
		initViews();
		setToolbar();
		setFloatingActionButton();
		if (mTid != null) {
			new GetThreadsreviewsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mFloatingActionButton = (FloatingActionButton) findViewById(R.id.reviews_fab);
		mSwingIndicator = (SwingIndicator) findViewById(R.id.reviews_progress);
		mListView = (ListView) findViewById(R.id.reviews_activity_listview);
	}

	private void setToolbar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle(mToolbarTitle);
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
		mFloatingActionButton.attachToListView(mListView);//把listview和浮动imagebutton组合
		mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReviewsActivity.this, SetReviewsActivity.class);
				intent.putExtra("tid", mTid);
				intent.putExtra("formhash", threadObject.getVariables().getFormhash());
				startActivityForResult(intent, 2);
				overridePendingTransition(0, 0);
			}
		});
	}

	private class AppAdapet extends BaseAdapter {

		@Override
		public int getCount() {
			if (threadObject != null) {
				return threadObject.getVariables().getPostlist().size();
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
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.reviews_listview_item, parent, false);
			}

			ImageView img = (ImageView) convertView.findViewById(R.id.reviews_listview_item_img);
			TextView name = (TextView) convertView.findViewById(R.id.reviews_listview_item_name);
			TextView time = (TextView) convertView.findViewById(R.id.reviews_listview_item_time);
			TextView num = (TextView) convertView.findViewById(R.id.reviews_listview_item_num);
			TextView reviews = (TextView) convertView.findViewById(R.id.reviews_listview_item_content);

			mThreadPostlist = new ArrayList<ThreadPostlist>();
			mThreadPostlist = threadObject.getVariables().getPostlist();

			img.setImageBitmap(null);

			new GetAvatarApi.GetAvatar(mThreadPostlist.get(position).getAuthorid(), img).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			name.setText(mThreadPostlist.get(position).getAuthor());
			time.setText(Html.fromHtml(mThreadPostlist.get(position).getDateline()));
			if (position == 0) {
				num.setText("楼主");
			} else if (position > 0) {
				num.setText("" + position + "楼");
			}

			reviews.setText(Html.fromHtml(mThreadPostlist.get(position).getMessage(), new URLImageParser(reviews, mThreadPostlist.get(position).getMessage()), null));

			reviews.setMovementMethod(ImageLinkParser.getInstance());

			return convertView;
		}
	}

	private class GetThreadsreviewsAsyncTask extends AsyncTask<Void, Void, ThreadObject> {
		private HashMap<String, String> hearders = new HashMap<String, String>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (S1GoApplication.getInstance().getUserInfo() == null) {
			} else {
				String cookie = S1GoApplication.getInstance().getUserInfo().getCookiepre();
				String auth = "auth=" + Uri.encode(S1GoApplication.getInstance().getUserInfo().getAuth());
				String saltkey = "saltkey=" + S1GoApplication.getInstance().getUserInfo().getSaltkey();
				this.hearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
			}
		}

		@Override
		protected ThreadObject doInBackground(Void... params) {

			return ReviewsApi.getReviews(mTid, hearders);
		}

		@Override
		protected void onPostExecute(ThreadObject aVoid) {
			super.onPostExecute(aVoid);
			threadObject = aVoid;
			mApdater = new AppAdapet();
			mListView.setAdapter(mApdater);
			mApdater.notifyDataSetChanged();
			mSwingIndicator.setVisibility(View.GONE);
			mFloatingActionButton.setVisibility(View.VISIBLE);
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
