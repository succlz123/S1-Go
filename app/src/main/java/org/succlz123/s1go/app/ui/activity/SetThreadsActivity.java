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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.set.SetThreadsAndReviewsObject;
import org.succlz123.s1go.app.dao.api.IsFastClickButton;
import org.succlz123.s1go.app.dao.interaction.SetThreadsAndReviews;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/19.
 */
public class SetThreadsActivity extends ActionBarActivity {
	private Toolbar mToolbar;
	private EditText mTilteEdit;
	private EditText mContentEdit;
	private Button mPostBtn;
	private String mFid;
	private String mFormhash;
	private String mPhoneInfo;
	private String mTitle;
	private String mContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setthreads_activity);
		mFid = getIntent().getStringExtra("fid");
		mFormhash = getIntent().getStringExtra("formhash");
		mPhoneInfo = "\n \n \n ——发送自 Stage1st Go " + "[url=http://baidu.com]" +
				android.os.Build.MODEL + " Android " + android.os.Build.VERSION.RELEASE
				+ "[/url]";
		initViews();
		setToolbar();
		mPostBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mTitle = mTilteEdit.getText().toString();
				mContent = mContentEdit.getText().toString();
				if (!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mContent)) {
					if (mContent.length() < 6 || mContent.length() > 10000) {
						Toast.makeText(SetThreadsActivity.this, "正文字数过多或少", Toast.LENGTH_SHORT).show();
					} else if (mTitle.length() < 6) {
						Toast.makeText(SetThreadsActivity.this, "标题字数过少", Toast.LENGTH_SHORT).show();
					} else if (mTitle.length() > 80) {
						Toast.makeText(SetThreadsActivity.this, "标题字数过多", Toast.LENGTH_SHORT).show();
					} else {
						if (!IsFastClickButton.isFastClick()) {
							dialog(1);
						}
					}
				} else if (TextUtils.isEmpty(mTitle) || TextUtils.isEmpty(mContent)) {
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
		Window window = getWindow();
		window.setFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
					new SetThreadstAsyncTask(mFid, mFormhash, mTitle, mContent, mPhoneInfo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					Toast.makeText(SetThreadsActivity.this, "正在发送,稍等片刻", Toast.LENGTH_SHORT).show();
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

	private class SetThreadstAsyncTask extends AsyncTask<Void, Void, SetThreadsAndReviewsObject> {
		private HashMap<String, String> mHearders = new HashMap<String, String>();
		private HashMap<String, String> mBody = new HashMap<String, String>();

		private String mFid;
		private String mTitle;
		private String mContent;
		private String mFormhash;
		private String mPhoneInfo;

		public SetThreadstAsyncTask(String mFid, String mFormhash, String mTitle, String mContent, String mPhoneInfo) {
			super();
			this.mFid = mFid;
			this.mFormhash = mFormhash;
			this.mTitle = mTitle;
			this.mContent = mContent;
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
				String subject = mTitle;
				String message = mContent + mPhoneInfo;
				String mobiletype = "0";

				this.mHearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
				this.mBody.put("formhash", mFormhash);
				this.mBody.put("noticetrimstr", noticetrimstr);
				this.mBody.put("subject", subject);
				this.mBody.put("message", message);
				this.mBody.put("mobiletype", mobiletype);
			}
		}

		@Override
		protected SetThreadsAndReviewsObject doInBackground(Void... params) {

			return SetThreadsAndReviews.SetThreads(mFid, mHearders, mBody);
		}

		@Override
		protected void onPostExecute(SetThreadsAndReviewsObject aVoid) {
			super.onPostExecute(aVoid);
			if (aVoid != null) {
				if (aVoid.getMessage().getMessageval().equals("post_newthread_succeed")) {
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
		if (!TextUtils.isEmpty(mContentEdit.getText().toString())) {
			dialog(0);
		} else if (TextUtils.isEmpty(mContentEdit.getText().toString())) {
			super.onBackPressed();
		}
	}
}
