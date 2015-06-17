package org.succlz123.s1go.app.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.bean.set.SetThreadsAndReviewsObject;
import org.succlz123.s1go.app.support.biz.SetThreadsAndReviews;
import org.succlz123.s1go.app.support.utils.IsFastClickBtn;
import org.succlz123.s1go.app.support.utils.S1String;
import org.succlz123.s1go.app.ui.fragment.emoticon.EmoticonFragment;

import java.util.HashMap;


/**
 * Created by fashi on 2015/4/19.
 */
public class SetThreadsActivity extends AppCompatActivity {
	private static final int TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS = 0;
	private static final int TEXT_IS_NOT_EMPTY_AND_SET_THREADS = 1;

	private static final int THREADS_TITLE_MIN = 5;
	private static final int THREADS_TITLE_MAX = 80;
	private static final int THREADS_CONTENT_MIN = 5;
	private static final int THREADS_CONTENT_MAX = 10000;

	private Toolbar mToolbar;
	private EditText mTilteEdit;
	private EditText mContentEdit;

	private String mFid;
	private String mFormhash;
	private String mTitle;
	private String mContent;

	private boolean mEmoticonOk;

	private boolean mPostOk;

	private LinearLayout mMoveLinearLayout;
	private View mRootView;
	private FrameLayout mEmoticonView;
	private EmoticonFragment emoticonFragment;
	private int mMoveLLHeight;
	private View mDivideLinear;

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
		getChangeHeight();
		onEditTextChangedListener();
		setEditTextFocusChangeListener();
		setEditTextClickListener();
	}

	/**
	 * 获得系统各个 view 的高宽度
	 */
	private void getChangeHeight() {
		mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						mRootView.getWindowVisibleDisplayFrame(r);
						int screenHeight = mRootView.getRootView().getHeight();
						//键盘高度
						int heightDifference = screenHeight - (r.bottom - r.top);
						//状态栏高度
						int statusBarHight = r.top;
						//appbar高度
						int toolBarHight = r.height() - mRootView.getHeight();

						DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
						int emoticonHeight = displayMetrics.widthPixels / 8 * 4;
						mMoveLLHeight = displayMetrics.heightPixels
								- statusBarHight
								- toolBarHight
								- emoticonHeight;
					}
				});
	}

	/**
	 * 通过监听 mEmoticonView visibiliity 的状态 来判断是否隐藏和弹出软键盘
	 */
	private void onEmoticonItem() {
		if (mEmoticonView.getVisibility() == View.GONE) {
			popSoftKeyboard(false);
			mMoveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, mMoveLLHeight));

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.emoticon_fragment, emoticonFragment);
			transaction.commitAllowingStateLoss();
			mDivideLinear.setVisibility(View.VISIBLE);
			mEmoticonView.setVisibility(View.VISIBLE);
		} else if (mEmoticonView.getVisibility() == View.VISIBLE) {
			popSoftKeyboard(true);

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.remove(emoticonFragment);
			transaction.commitAllowingStateLoss();
			mEmoticonView.setVisibility(View.GONE);
			mDivideLinear.setVisibility(View.GONE);
		}
	}

	private void popSoftKeyboard(boolean wantPop) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (wantPop) {
//			mContentEdit.requestFocus();
			setMoveParamsMatchPARENT();
			imm.showSoftInput(mContentEdit, InputMethodManager.SHOW_IMPLICIT);
		} else {
			imm.hideSoftInputFromWindow(mContentEdit.getWindowToken(), 0);
		}
	}

	private void setMoveParamsMatchPARENT() {
		mMoveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
	}

	private void setEditTextFocusChangeListener() {
		mTilteEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					setMoveParamsMatchPARENT();
					mEmoticonView.setVisibility(View.GONE);
					mEmoticonOk = false;
					invalidateOptionsMenu();
				}
			}
		});
		mContentEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					setMoveParamsMatchPARENT();
					mEmoticonView.setVisibility(View.GONE);
					mEmoticonOk = true;
					invalidateOptionsMenu();
				}
			}
		});
	}

	private void setEditTextClickListener() {
		mTilteEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setMoveParamsMatchPARENT();
				mEmoticonView.setVisibility(View.GONE);
			}
		});
		mTilteEdit.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				setMoveParamsMatchPARENT();
				mEmoticonView.setVisibility(View.GONE);
				return false;
			}
		});
		mContentEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setMoveParamsMatchPARENT();
				mEmoticonView.setVisibility(View.GONE);
			}
		});
		mContentEdit.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				setMoveParamsMatchPARENT();
				mEmoticonView.setVisibility(View.GONE);
				return false;
			}
		});
	}

	private void getStringExtra() {
		mFid = getIntent().getStringExtra(S1String.FID);
		mFormhash = getIntent().getStringExtra(S1String.FORM_HASH);
	}

	private void initViews() {
		mRootView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mTilteEdit = (EditText) findViewById(R.id.setthreads_title);
		mContentEdit = (EditText) findViewById(R.id.setthreads_content);
		mMoveLinearLayout = (LinearLayout) findViewById(R.id.move_linearlayout);
		mEmoticonView = (FrameLayout) findViewById(R.id.emoticon_fragment);
		emoticonFragment = new EmoticonFragment();
		mDivideLinear = (View) findViewById(R.id.linear_view);
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

	private void onEditTextChangedListener() {
		mTilteEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {


			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				invalidateOptionsMenu();
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		mContentEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				invalidateOptionsMenu();
			}

			@Override
			public void afterTextChanged(Editable s) {

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
	public boolean onCreateOptionsMenu(Menu menu) {
		//表情 menu
		MenuItem emoticonItem = menu.add(Menu.NONE, 1, 100, "表情");
		Drawable emoticonDrawable = getDrawable(R.drawable.emoticon);
		emoticonDrawable.setTint(getResources().getColor(R.color.translucence_white));
		if (mEmoticonOk) {
			emoticonDrawable.setTint(getResources().getColor(R.color.white));
		}
		emoticonItem.setEnabled(mEmoticonOk);
		emoticonItem.setIcon(emoticonDrawable);
		emoticonItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		//发送 menu
		MenuItem postItem = menu.add(Menu.NONE, 2, 100, "发帖");
		Drawable postDrawable = getDrawable(R.drawable.ok);
		//drawable 染成半透明颜色
		postDrawable.setTint(getResources().getColor(R.color.translucence_white));
		mPostOk = false;
		//最后发送时 异步线程任务所需要的信息
		mTitle = mTilteEdit.getText().toString();
		mContent = mContentEdit.getText().toString();
		if (mTitle.length() > THREADS_TITLE_MIN && mTitle.length() < THREADS_TITLE_MAX
				&& mContent.length() > THREADS_CONTENT_MIN && mContent.length() < THREADS_CONTENT_MAX) {
			postDrawable.setTint(getResources().getColor(R.color.white));
			mPostOk = true;
		}
		postItem.setEnabled(mPostOk);
		postItem.setIcon(postDrawable);
		postItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case 1:
				onEmoticonItem();
				return true;
			case 2:
				if (!IsFastClickBtn.isFastClick()) {
					dialog(TEXT_IS_NOT_EMPTY_AND_SET_THREADS);
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mEmoticonView.getVisibility() == View.VISIBLE) {
			mDivideLinear.setVisibility(View.GONE);
			mEmoticonView.setVisibility(View.GONE);
			setMoveParamsMatchPARENT();
		}else {
			if (!TextUtils.isEmpty(mContentEdit.getText().toString())) {
				dialog(TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS);
			} else if (TextUtils.isEmpty(mContentEdit.getText().toString())) {
				super.onBackPressed();
			}
		}
	}
}
