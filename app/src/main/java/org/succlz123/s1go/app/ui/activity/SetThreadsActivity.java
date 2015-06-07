package org.succlz123.s1go.app.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.bean.set.SetThreadsAndReviewsObject;
import org.succlz123.s1go.app.support.biz.SetThreadsAndReviews;
import org.succlz123.s1go.app.support.utils.IsFastClickBtn;
import org.succlz123.s1go.app.support.utils.S1String;

import java.util.HashMap;


/**
 * Created by fashi on 2015/4/19.
 */
public class SetThreadsActivity extends AppCompatActivity {
	private static final int TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS = 0;
	private static final int TEXT_IS_NOT_EMPTY_AND_SET_THREADS = 1;

	private static final int THREADS_TITLE_MIN = 6;
	private static final int THREADS_TITLE_MAX = 80;
	private static final int THREADS_CONTENT_MIN = 6;
	private static final int THREADS_CONTENT_MAX = 10000;

	private Toolbar mToolbar;
	private EditText mTilteEdit;
	private EditText mContentEdit;
	private Button mPostBtn;
	private ImageButton mEmoticonBtn;
	private ImageButton mPicBtn;
	private String mFid;
	private String mFormhash;
	private String mTitle;
	private String mContent;
	private LinearLayout mMoveLinearLayout;
//	private TextInputLayout mTextinput;

	private View mContentView;
	private int mScreenHeight;
	private int mHeightDifference;

	public static void actionStart(Context context, String fid, String formhash) {
		Intent intent = new Intent(context, SetThreadsActivity.class);
		intent.putExtra(S1String.FID, fid);
		intent.putExtra(S1String.FORM_HASH, formhash);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setthreads_activity);
		getStringExtra();
		initViews();
		setToolbar();
		setPicBtnOnClick();
		setPostBtnOnClick();
		mContentView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);

		mContentView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						mContentView.getWindowVisibleDisplayFrame(r);

						mScreenHeight = mContentView.getRootView().getHeight();

						mHeightDifference = mScreenHeight - (r.bottom - r.top);

						Log.e("Keyboard Size", "Size: " + mHeightDifference);
					}

				});
		mEmoticonBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				mContentView.setLayoutParams(new LinearLayout
						.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight - mHeightDifference));


//				InputMethodManager mInputMethodManager =
//						(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//				if (rlayout_emoji.getVisibility() == View.VISIBLE) {
//					iv_note_emoticon
//							.setImageResource(R.drawable.btn_emoticon_selector);
//					rlayout_emoji.setVisibility(View.GONE);
//					getWindow()
//							.setSoftInputMode(
//									WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;


//			}
//
			}
		});
	}

	private void getStringExtra() {
		mFid = getIntent().getStringExtra(S1String.FID);
		mFormhash = getIntent().getStringExtra(S1String.FORM_HASH);
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mTilteEdit = (EditText) findViewById(R.id.setthreads_title);
		mContentEdit = (EditText) findViewById(R.id.setthreads_content);
		mPostBtn = (Button) findViewById(R.id.setthreads_post);
		mEmoticonBtn = (ImageButton) findViewById(R.id.setthreads_emoticon);
		mPicBtn = (ImageButton) findViewById(R.id.setthreads_pic);
		mMoveLinearLayout = (LinearLayout) findViewById(R.id.move_linearlayout);
//		mTextinput = (TextInputLayout) findViewById(R.id.textinput);

		View xx = (View) findViewById(R.id.action_mode_bar);
	}

	private void setToolbar() {
		mToolbar.setTitle(getString(R.string.set_threads));
		mToolbar.setTitleTextColor(Color.WHITE);
		mToolbar.setSubtitleTextColor(Color.WHITE);
		mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

//	@Override
//	public void onSupportActionModeStarted(ActionMode mode) {
//		super.onSupportActionModeStarted(mode);
//		this.mToolbar.setVisibility(View.GONE);

//		LinearLayout.LayoutParams params = new LinearLayout
//				.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		params.setMargins(0, 0, 0, 0);
//		mMoveLinearLayout.setLayoutParams(params);
//	}

//	@Override
//	public void onSupportActionModeFinished(ActionMode mode) {
//		super.onSupportActionModeFinished(mode);
//		this.mToolbar.setVisibility(View.VISIBLE);

//		LinearLayout.LayoutParams params = new LinearLayout
//				.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//		float move = displayMetrics.density * 25;
//		params.setMargins(0, (int) move, 0, 0);
//		mMoveLinearLayout.setLayoutParams(params);
//	}

	private void setPicBtnOnClick() {
		mPicBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SetThreadsActivity.this,
						getString(R.string.set_pic),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setPostBtnOnClick() {
		mPostBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mTitle = mTilteEdit.getText().toString();
				mContent = mContentEdit.getText().toString();

				if (!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mContent)) {
					if (mContent.length() < THREADS_CONTENT_MIN || mContent.length() > THREADS_CONTENT_MAX) {
						Toast.makeText(SetThreadsActivity.this,
								getString(R.string.the_word_too_much_or_too_little),
								Toast.LENGTH_SHORT).show();
					} else if (mTitle.length() < THREADS_TITLE_MIN) {
						Toast.makeText(SetThreadsActivity.this,
								getString(R.string.title_word_too_much),
								Toast.LENGTH_SHORT).show();
					} else if (mTitle.length() > THREADS_TITLE_MAX) {
						Toast.makeText(SetThreadsActivity.this,
								getString(R.string.title_word_too_little),
								Toast.LENGTH_SHORT).show();
					} else {
						if (!IsFastClickBtn.isFastClick()) {
							dialog(TEXT_IS_NOT_EMPTY_AND_SET_THREADS);
						}
					}
				} else if (TextUtils.isEmpty(mTitle) || TextUtils.isEmpty(mContent)) {
					Toast.makeText(SetThreadsActivity.this,
							getString(R.string.please_intput_title_or_body),
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

		if (message == TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS) {
			builder.setMessage(getString(R.string.confirmation_reviews_give_up));
		} else if (message == TEXT_IS_NOT_EMPTY_AND_SET_THREADS) {
			builder.setMessage(getString(R.string.confirmation_set));
		}

		builder.setTitle(getString(R.string.tips));

		builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (message == TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS) {
					finish();
				} else if (message == TEXT_IS_NOT_EMPTY_AND_SET_THREADS) {
					new SetThreadstAsyncTask(
							mFid,// thread id
							mFormhash,// judge the lawfulness of a post thread
							mTitle,// thread title
							mContent)// thread content
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					Toast.makeText(SetThreadsActivity.this,
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

	/**
	 *
	 */
	private class SetThreadstAsyncTask extends AsyncTask<Void, Void, SetThreadsAndReviewsObject> {
		private HashMap<String, String> mHearders = new HashMap<String, String>();
		private HashMap<String, String> mBody = new HashMap<String, String>();

		private String mFid;
		private String mTitle;
		private String mContent;
		private String mFormhash;

		public SetThreadstAsyncTask(String fid, String formhash, String title, String content) {
			super();
			this.mFid = fid;
			this.mFormhash = formhash;
			this.mTitle = title;
			this.mContent = content;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (MyApplication.getInstance().getUserInfo() == null) {
			} else {
				String cookie = MyApplication.getInstance().getUserInfo().getCookiepre();
				String auth = "auth=" + Uri.encode(MyApplication.getInstance().getUserInfo().getAuth());
				String saltkey = "saltkey=" + MyApplication.getInstance().getUserInfo().getSaltkey();
				String formhash = MyApplication.getInstance().getUserInfo().getFormhash();

				String noticetrimstr = "";
				String subject = mTitle;
				String message = mContent;
				String mobiletype = "0";

				this.mHearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
				this.mBody.put("formhash", formhash);
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
			dialog(TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS);
		} else if (TextUtils.isEmpty(mContentEdit.getText().toString())) {
			super.onBackPressed();
		}
	}
}
