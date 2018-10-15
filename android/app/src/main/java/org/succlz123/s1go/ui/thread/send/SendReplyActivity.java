package org.succlz123.s1go.ui.thread.send;

import org.succlz123.s1go.BuildConfig;
import org.succlz123.s1go.MainApplication;
import org.succlz123.s1go.R;
import org.succlz123.s1go.bean.SendInfo;
import org.succlz123.s1go.config.RetrofitManager;
import org.succlz123.s1go.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.utils.SettingHelper;
import org.succlz123.s1go.utils.common.SysUtils;
import org.succlz123.s1go.utils.common.ToastUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by succlz123 on 2015/4/19.
 */
public class SendReplyActivity extends BaseToolbarActivity {
    public static final String TID = "tid";

    private static final int TEXT_IS_NOT_EMPTY_AND_GIVE_UP_REVIEWS = 0;
    private static final int TEXT_IS_NOT_EMPTY_AND_SET_REVIEWS = 1;
    private static final int REVIEWS_MIN = 2;

    private EditText mReviewsEdit;
    private Button mPostBtn;

    private String mTid;
    private String mFormHash;
    private String mReviews;

    public static void start(Context context, String tid, String formHash) {
        Intent intent = new Intent(context, SendReplyActivity.class);
        intent.putExtra(TID, tid);
        intent.putExtra(BuildConfig.FORM_HASH, formHash);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reviews);

        mTid = getIntent().getStringExtra(TID);
        mFormHash = getIntent().getStringExtra(BuildConfig.FORM_HASH);

        mReviewsEdit = (EditText) findViewById(R.id.content);
        mPostBtn = (Button) findViewById(R.id.post);

        Button emoticonBtn = (Button) findViewById(R.id.emoticon);
        emoticonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(v.getContext(), "额,有空在说.");
            }
        });

        showBackButton();
        setTitle(getString(R.string.set_reviews));
        setPostBtnOnClick();
    }

    private void setPostBtnOnClick() {
        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReviews = mReviewsEdit.getText().toString();
                if (!TextUtils.isEmpty(mReviews)) {
                    if (mReviews.length() < REVIEWS_MIN) {
                        ToastUtils.showToastShort(v.getContext(), R.string.too_little_words);
                    } else {
                        if (!SysUtils.isFastClick()) {
                            dialog(TEXT_IS_NOT_EMPTY_AND_SET_REVIEWS);
                        }
                    }
                } else if (TextUtils.isEmpty(mReviews)) {
                    ToastUtils.showToastShort(v.getContext(), R.string.please_input_reviews);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    send();
                    ToastUtils.showToastShort(MainApplication.getInstance(), "已发送，请耐心等待！");
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

    private void send() {
        if (SettingHelper.isShowPhoneTail(this)) {
            mReviews += BuildConfig.PHONE_INFO;
        }
        Observable<SendInfo> observable = RetrofitManager.apiService().sendReply(mTid, mFormHash, mReviews);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<SendInfo, Boolean>() {
                    @Override
                    public Boolean call(SendInfo sendInfo) {
                        return SysUtils.isActivityLive(SendReplyActivity.this);
                    }
                })
                .subscribe(new Action1<SendInfo>() {
                    @Override
                    public void call(SendInfo sendInfo) {
                        SendInfo.Message message = sendInfo.Message;
                        if (message == null) {
                            ToastUtils.showToastShort(MainApplication.getInstance(), R.string.sorry);
                        }
                        if (TextUtils.equals(BuildConfig.POST_REPLY_SUCCEED, message.messageval)) {
                            ToastUtils.showToastShort(MainApplication.getInstance(), R.string.set_succeed);
                            finish();
                        } else {
                            if (!TextUtils.isEmpty(message.messagestr)) {
                                ToastUtils.showToastShort(MainApplication.getInstance(), message.messagestr);
                            } else {
                                Toast.makeText(SendReplyActivity.this, getString(R.string.set_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtils.showToastShort(MainApplication.getInstance(), R.string.sorry);
                    }
                });
        compositeSubscription.add(subscription);
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
