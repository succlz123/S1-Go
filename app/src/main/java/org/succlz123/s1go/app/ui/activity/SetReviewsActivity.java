package org.succlz123.s1go.app.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.bean.set.SetThreadsAndReviewsObject;
import org.succlz123.s1go.app.support.biz.SetThreadsAndReviews;
import org.succlz123.s1go.app.support.utils.IsFastClickBtn;
import org.succlz123.s1go.app.support.utils.S1String;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Created by fashi on 2015/4/19.
 */
public class SetReviewsActivity extends AppCompatActivity {
	private static final int TEXT_IS_NOT_EMPTY_AND_GIVE_UP_REVIEWS = 0;
	private static final int TEXT_IS_NOT_EMPTY_AND_SET_REVIEWS = 1;
	private static final int REVIEWS_MIN = 2;

	private EditText mReviewsEdit;
	private String mTid;
	private Toolbar mToolbar;
	private Button mPostBtn;
	private String mFormhash;
	private String mReviews;

	public static void actionStart(Context context, String tid, String formhash) {
		Intent intent = new Intent(context, SetReviewsActivity.class);
		intent.putExtra(S1String.TID, tid);
		intent.putExtra(S1String.FORM_HASH, formhash);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setreviews_activity);
		getStringExtra();
		initViews();
		setToolbar();
		setPostBtnOnClick();
	}

	private void getStringExtra() {
		mTid = getIntent().getStringExtra(S1String.TID);
		mFormhash = getIntent().getStringExtra(S1String.FORM_HASH);
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mReviewsEdit = (EditText) findViewById(R.id.setreviews_content);
		mPostBtn = (Button) findViewById(R.id.setreviews_post);
	}

	private void setToolbar() {
		mToolbar.setTitle(getString(R.string.set_reviews));
		mToolbar.setTitleTextColor(Color.WHITE);
		mToolbar.setSubtitleTextColor(Color.WHITE);
		mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void setPostBtnOnClick() {
		mPostBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mReviews = mReviewsEdit.getText().toString();
				if (!TextUtils.isEmpty(mReviews)) {
					if (mReviews.length() < REVIEWS_MIN) {
						Toast.makeText(SetReviewsActivity.this,
								getString(R.string.too_little_words),
								Toast.LENGTH_SHORT).show();
					} else {
						if (!IsFastClickBtn.isFastClick()) {
							dialog(TEXT_IS_NOT_EMPTY_AND_SET_REVIEWS);
						}
					}
				} else if (TextUtils.isEmpty(mReviews)) {
					Toast.makeText(SetReviewsActivity.this,
							getString(R.string.please_input_reviews),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * onBackPressed and postButton share one dialog
	 *
	 * @param message What calls (onBackPressed or postButton)
	 */
	private void dialog(final int message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
		if (message == TEXT_IS_NOT_EMPTY_AND_GIVE_UP_REVIEWS) {
			builder.setMessage(getString(R.string.confirmation_reviews_give_up));
		} else if (message == TEXT_IS_NOT_EMPTY_AND_SET_REVIEWS) {
			builder.setMessage(getString(R.string.confirmation_set));
		}

		builder.setTitle(getString(R.string.tips));

		builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (message == TEXT_IS_NOT_EMPTY_AND_GIVE_UP_REVIEWS) {
					finish();
				} else if (message == TEXT_IS_NOT_EMPTY_AND_SET_REVIEWS) {
					new SetReviewstAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					Toast.makeText(SetReviewsActivity.this,
							getString(R.string.wait_a_moment),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
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

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (MyApplication.getInstance().getUserInfo() == null) {
			} else {
				String cookie = MyApplication.getInstance().getUserInfo().getCookiepre();
				String auth = S1String.AUTH + "=" + Uri.encode(MyApplication.getInstance().getUserInfo().getAuth());
				String saltkey = S1String.SALT_KEY + "=" + MyApplication.getInstance().getUserInfo().getSaltkey();

				String noticetrimstr = "";
				String message = mReviews;
				String mobiletype = "0";

				this.mHearders.put(S1String.COOKIE, cookie + auth + ";" + cookie + saltkey + ";");
				this.mBody.put(S1String.FORM_HASH, mFormhash);
				this.mBody.put(S1String.NOTICE_TRIM_STR, noticetrimstr);
				this.mBody.put(S1String.MESSAGE, message);
				this.mBody.put(S1String.MOBILE_TYPE, mobiletype);
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
				if (aVoid.getMessage().getMessageval().equals(S1String.POST_REPLY_SUCCEED)) {
					Toast.makeText(SetReviewsActivity.this, getString(R.string.set_succeed), Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(SetReviewsActivity.this, getString(R.string.set_failed), Toast.LENGTH_SHORT).show();
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
			dialog(TEXT_IS_NOT_EMPTY_AND_GIVE_UP_REVIEWS);
		} else if (TextUtils.isEmpty(mReviewsEdit.getText().toString())) {
			super.onBackPressed();
		}
	}
}
