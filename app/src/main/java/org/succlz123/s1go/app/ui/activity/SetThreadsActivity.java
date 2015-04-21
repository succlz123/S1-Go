package org.succlz123.s1go.app.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.setreviews.SetReviewsObject;
import org.succlz123.s1go.app.dao.Api.SetReviewsApi;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/19.
 */
public class SetThreadsActivity extends ActionBarActivity {
	private Toolbar mToolbar;
	private EditText mTilteEdit;
	private EditText mContentEdit;
	private Button mPostBtn;
	private String mTid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setthreads_activity);
		mTid = getIntent().getStringExtra("tid");
		initViews();
		setToolbar();
		mPostBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = mTilteEdit.getText().toString();
				String content = mContentEdit.getText().toString();
				if (!title.isEmpty() && !content.isEmpty()) {
					if (content.length() < 4 || content.length() > 10000) {
						Toast.makeText(SetThreadsActivity.this, "正文字数过多或少", Toast.LENGTH_SHORT).show();
					} else if (title.length() > 80) {
						Toast.makeText(SetThreadsActivity.this, "标题字数过多", Toast.LENGTH_SHORT).show();
					} else {

					}
				} else if (title.isEmpty() || content.isEmpty()) {
					Toast.makeText(SetThreadsActivity.this, "请输入标题或者正文", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mTilteEdit = (EditText) findViewById(R.id.setthreads_title);
		mContentEdit = (EditText) findViewById(R.id.setthreads_content);
		mPostBtn = (Button) findViewById(R.id.setthreads_post);
	}

	private void setToolbar() {
		mToolbar.setTitle("发帖");
		mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
		mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private class SetReviewstAsyncTask extends AsyncTask<Void, Void, SetReviewsObject> {
		private HashMap<String, String> hearders = new HashMap<String, String>();
		private HashMap<String, String> body = new HashMap<String, String>();
		private String tid;

		private String title;
		private String contentEdit;

		public SetReviewstAsyncTask(String contentEdit) {
			super();
			this.contentEdit = contentEdit;
			this.tid = tid;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (S1GoApplication.getInstance().getUserInfo() == null) {
			} else {
				String cookie = S1GoApplication.getInstance().getUserInfo().getCookiepre();
				String auth = "auth=" + Uri.encode(S1GoApplication.getInstance().getUserInfo().getAuth());
				String saltkey = "saltkey=" + S1GoApplication.getInstance().getUserInfo().getSaltkey();

				String formhash = "formhash" + S1GoApplication.getInstance().getUserInfo().getFormhash();
				String allownoticeauthor = "&allownoticeauthor=";
				String message = "&message=" + Uri.encode(contentEdit);
				String mobiletype = "&mobiletype=0";

				this.hearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
				this.body.put("formhash", formhash);
				this.body.put("allownoticeauthor", allownoticeauthor);
				this.body.put("message", message);
				this.body.put("mobiletype", mobiletype);
			}
		}

		@Override
		protected SetReviewsObject doInBackground(Void... params) {

			return SetReviewsApi.SetReviews(tid, hearders, body);
		}

		@Override
		protected void onPostExecute(SetReviewsObject aVoid) {
			super.onPostExecute(aVoid);
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
