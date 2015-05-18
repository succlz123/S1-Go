package org.succlz123.s1go.app.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.set.SetThreadsAndReviewsObject;
import org.succlz123.s1go.app.dao.api.IsFastClickButton;
import org.succlz123.s1go.app.dao.interaction.SetThreadsAndReviews;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by fashi on 2015/4/19.
 */
public class SetReviewsActivity extends ActionBarActivity {
	private EditText mReviewsEdit;
	private String mTid;
	private Toolbar mToolbar;
	private Button mPostBtn;
	private String mFormhash;
	private String mPhoneInfo;
	private String mReviews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setreviews_activity);
		mTid = getIntent().getStringExtra("tid");
		mFormhash = getIntent().getStringExtra("formhash");
		mPhoneInfo = "\n \n \n ——发送自 Stage1st Go " + "[url=http://baidu.com]" +
				android.os.Build.MODEL + " Android " + android.os.Build.VERSION.RELEASE
				+ "[/url]";
		initViews();
		setToolbar();
		PostBtnOnClickListener postBtnOnClickListener = new PostBtnOnClickListener();
		mPostBtn.setOnClickListener(postBtnOnClickListener);
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mReviewsEdit = (EditText) findViewById(R.id.setreviews_content);
		mPostBtn = (Button) findViewById(R.id.setreviews_post);
	}

	private void setToolbar() {
		mToolbar.setTitle("回帖");
		mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
		mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private class PostBtnOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			mReviews = mReviewsEdit.getText().toString();
			if (!TextUtils.isEmpty(mReviews)) {
				if (mReviews.length() < 2) {
					Toast.makeText(SetReviewsActivity.this, "回复字数过少", Toast.LENGTH_SHORT).show();
				} else {
					if (!IsFastClickButton.isFastClick()) {
						dialog(1);
					}
				}
			} else if (TextUtils.isEmpty(mReviews)) {
				Toast.makeText(SetReviewsActivity.this, "请输入回复内容", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void dialog(final int message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
		if (message == 0) {
			builder.setMessage("确认放弃回复吗?");
		} else if (message == 1) {
			builder.setMessage("确认发送吗？");
		}
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (message == 0) {
					finish();
				} else if (message == 1) {
					new SetReviewstAsyncTask(mTid, mFormhash, mReviews, mPhoneInfo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					Toast.makeText(SetReviewsActivity.this, "正在发送,稍等片刻", Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}


	private class SetReviewstAsyncTask extends AsyncTask<Void, Void, SetThreadsAndReviewsObject> {
		private HashMap<String, String> mHearders = new HashMap<String, String>();
		private LinkedHashMap<String, String> mBody = new LinkedHashMap<String, String>();

		private String mTid;
		private String mFormhash;
		private String mReviews;
		private String mPhoneInfo;

		public SetReviewstAsyncTask(String mTid, String mFormhash, String mReviews, String mPhoneInfo) {
			super();
			this.mTid = mTid;
			this.mFormhash = mFormhash;
			this.mReviews = mReviews;
			this.mPhoneInfo = mPhoneInfo;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (S1GoApplication.getInstance().getUserInfo() == null) {
			} else {
				String cookie = S1GoApplication.getInstance().getUserInfo().getCookiepre();
				String auth = "auth=" + Uri.encode(S1GoApplication.getInstance().getUserInfo().getAuth());
				String saltkey = "saltkey=" + S1GoApplication.getInstance().getUserInfo().getSaltkey();

				String noticetrimstr = "";
				String message = mReviews + mPhoneInfo;
				String mobiletype = "0";

				this.mHearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
				this.mBody.put("formhash", mFormhash);
				this.mBody.put("noticetrimstr", noticetrimstr);
				this.mBody.put("message", message);
				this.mBody.put("mobiletype", mobiletype);
			}
		}

		@Override
		protected SetThreadsAndReviewsObject doInBackground(Void... params) {

			return SetThreadsAndReviews.SetReviews(mTid, mHearders, mBody);
		}

		@Override
		protected void onPostExecute(SetThreadsAndReviewsObject aVoid) {
			super.onPostExecute(aVoid);
			if (aVoid != null) {
				if (aVoid.getMessage().getMessageval().equals("post_reply_succeed")) {
					finish();
				}
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

	@Override
	public void onBackPressed() {
		if (!TextUtils.isEmpty(mReviewsEdit.getText().toString())) {
			dialog(0);
		} else if (TextUtils.isEmpty(mReviewsEdit.getText().toString())) {
			super.onBackPressed();
		}
	}
}
