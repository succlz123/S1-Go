package org.succlz123.s1go.app.ui.thread.send;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.api.bean.SendInfo;
import org.succlz123.s1go.app.config.RetrofitManager;
import org.succlz123.s1go.app.config.S1GoConfig;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.common.SysUtils;
import org.succlz123.s1go.app.utils.common.ToastUtils;
import org.succlz123.s1go.app.utils.common.ViewUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
public class SendThreadsActivity extends BaseToolbarActivity {
    private static final int TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS = 0;
    private static final int TEXT_IS_NOT_EMPTY_AND_SET_THREADS = 1;

    private static final int THREADS_TITLE_MIN = 5;
    private static final int THREADS_TITLE_MAX = 30;
    private static final int THREADS_CONTENT_MIN = 5;
    private static final int THREADS_CONTENT_MAX = 5000;

    public static final String KEY_FID = "key_post_fid";

    private EditText mTitleEdit;
    private EditText mContentEdit;
    private Button mPostBtn;

    private String mFid;
    private String mFormHash;

    private String mTitle;
    private String mContent;

    private View mRootView;

    public static void start(Context context, String fid, String formHash) {
        Intent intent = new Intent(context, SendThreadsActivity.class);
        intent.putExtra(KEY_FID, fid);
        intent.putExtra(S1GoConfig.FORM_HASH, formHash);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_threads);

        mFid = getIntent().getStringExtra(KEY_FID);
        mFormHash = getIntent().getStringExtra(S1GoConfig.FORM_HASH);
        mRootView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mTitleEdit = ViewUtils.f(this, R.id.title);
        mContentEdit = ViewUtils.f(this, R.id.content);
        mPostBtn = (Button) findViewById(R.id.post);

        showBackButton();
        setTitle(getString(R.string.set_threads));

        Button emoticonBtn = (Button) findViewById(R.id.emoticon);
        emoticonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(v.getContext(), "额,有空在说.");
            }
        });

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = mTitleEdit.getText().toString();
                mContent = mContentEdit.getText().toString();
                if (!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mContent)) {
                    if (mTitle.length() < THREADS_TITLE_MIN || mTitle.length() > THREADS_TITLE_MAX) {
                        ToastUtils.showToastShort(v.getContext(), "标题字数过多或过少！");
                    } else if (mContent.length() < THREADS_CONTENT_MIN || mContent.length() > THREADS_CONTENT_MAX) {
                        if (!SysUtils.isFastClick()) {
                            dialog(TEXT_IS_NOT_EMPTY_AND_SET_THREADS);
                        }
                    }
                } else {
                    ToastUtils.showToastShort(v.getContext(), R.string.please_input_reviews);
                }
            }
        });
    }

    private void getChangeHeight() {
//        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        Rect r = new Rect();
//                        mRootView.getWindowVisibleDisplayFrame(r);
//                        int screenHeight = mRootView.getRootView().getHeight();
//                        //键盘高度
//                        int heightDifference = screenHeight - (r.bottom - r.top);
//                        //状态栏高度
//                        int statusBarHight = r.top;
//                        //appbar高度
//                        int toolBarHight = r.height() - mRootView.getHeight();
//
//                        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//                        int emoticonHeight = displayMetrics.widthPixels / 8 * 4;
//                        mMoveLLHeight = displayMetrics.heightPixels
//                                - statusBarHight
//                                - toolBarHight
//                                - emoticonHeight;
//                    }
//                });
    }

    private void dialog(final int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    send();
                    ToastUtils.showToastShort(MainApplication.getContext(), "已发送，请耐心等待！");
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
        Observable<SendInfo> observable = RetrofitManager.apiService().sendNewThread(mFid, mFormHash, mTitle, mContent);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<SendInfo, Boolean>() {
                    @Override
                    public Boolean call(SendInfo sendInfo) {
                        return SysUtils.isActivityLive(SendThreadsActivity.this);
                    }
                })
                .subscribe(new Action1<SendInfo>() {
                    @Override
                    public void call(SendInfo sendInfo) {
                        SendInfo.Message message = sendInfo.Message;
                        if (message == null) {
                            ToastUtils.showToastShort(MainApplication.getContext(), R.string.sorry);
                        }
                        if (TextUtils.equals(S1GoConfig.POST_NEW_THREAD_SUCCEED, message.messageval)) {
                            ToastUtils.showToastShort(MainApplication.getContext(), R.string.set_succeed);
                            finish();
                        } else {
                            if (!TextUtils.isEmpty(message.messagestr)) {
                                ToastUtils.showToastShort(MainApplication.getContext(), message.messagestr);
                            } else {
                                Toast.makeText(SendThreadsActivity.this, getString(R.string.set_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtils.showToastShort(MainApplication.getContext(), R.string.sorry);
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
        if (!TextUtils.isEmpty(mContentEdit.getText().toString())) {
            dialog(TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS);
        } else if (TextUtils.isEmpty(mContentEdit.getText().toString())) {
            super.onBackPressed();
        }
    }
}
