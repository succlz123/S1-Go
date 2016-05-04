package org.succlz123.s1go.app.ui.send;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.config.S1GoConfig;
import org.succlz123.s1go.app.utils.common.SysUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by fashi on 2015/4/19.
 */
public class SendReplyActivity extends AppCompatActivity {
	public static final String TID = "tid";

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
		Intent intent = new Intent(context, SendReplyActivity.class);
		intent.putExtra(TID, tid);
		intent.putExtra(S1GoConfig.FORM_HASH, formhash);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setreviews);
		getStringExtra();
		initViews();
		setToolbar();
		setPostBtnOnClick();

		//				String cookie = MainApplication.getInstance().getLoginInfo().getCookiepre();
//				String auth = S1GoConfig.AUTH + "=" + Uri.encode(MainApplication.getInstance().getLoginInfo().getAuth());
//				String saltkey = S1GoConfig.SALT_KEY + "=" + MainApplication.getInstance().getLoginInfo().getSaltkey();

		String noticetrimstr = "";
		String message = mReviews;
		String mobiletype = "0";
//
//				this.mHearders.put(S1GoConfig.COOKIE, cookie + auth + ";" + cookie + saltkey + ";");
//				this.mBody.put(S1GoConfig.FORM_HASH, mFormhash);
//				this.mBody.put(S1GoConfig.NOTICE_TRIM_STR, noticetrimstr);
//				this.mBody.put(S1GoConfig.MESSAGE, message);
//				this.mBody.put(S1GoConfig.MOBILE_TYPE, mobiletype);


//		if (aVoid.getMessage().getMessageval().equals(S1GoConfig.POST_REPLY_SUCCEED)) {
//			Toast.makeText(SendReplyActivity.this, getString(R.string.set_succeed), Toast.LENGTH_SHORT).show();
//			finish();
//		} else {
//			Toast.makeText(SendReplyActivity.this, getString(R.string.set_failed), Toast.LENGTH_SHORT).show();
//		}
	}

	private void getStringExtra() {
		mTid = getIntent().getStringExtra(TID);
		mFormhash = getIntent().getStringExtra(S1GoConfig.FORM_HASH);
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mReviewsEdit = (EditText) findViewById(R.id.setreviews_content);
		mPostBtn = (Button) findViewById(R.id.setreviews_post);
	}

	private void setToolbar() {
		mToolbar.setTitle(getString(R.string.set_reviews));
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
						Toast.makeText(SendReplyActivity.this,
								getString(R.string.too_little_words),
								Toast.LENGTH_SHORT).show();
					} else {
						if (!SysUtils.isFastClick()) {
							dialog(TEXT_IS_NOT_EMPTY_AND_SET_REVIEWS);
						}
					}
				} else if (TextUtils.isEmpty(mReviews)) {
					Toast.makeText(SendReplyActivity.this,
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
