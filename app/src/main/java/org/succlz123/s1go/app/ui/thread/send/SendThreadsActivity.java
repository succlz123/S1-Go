package org.succlz123.s1go.app.ui.thread.send;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.config.S1GoConfig;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.common.SysUtils;
import org.succlz123.s1go.app.utils.common.ToastUtils;
import org.succlz123.s1go.app.utils.common.ViewUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by succlz123 on 2015/4/19.
 */
public class SendThreadsActivity extends BaseToolbarActivity {
    private static final int TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS = 0;
    private static final int TEXT_IS_NOT_EMPTY_AND_SET_THREADS = 1;

    private static final int THREADS_TITLE_MIN = 5;
    private static final int THREADS_TITLE_MAX = 30;
    private static final int THREADS_CONTENT_MIN = 5;
    private static final int THREADS_CONTENT_MAX = 6000;

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
                    } else {
                        if (!SysUtils.isFastClick()) {
//                            dialog(TEXT_IS_NOT_EMPTY_AND_SET_REVIEWS);
                        }
                    }
                } else {
                    ToastUtils.showToastShort(v.getContext(), R.string.please_input_reviews);
                }
            }
        });
    }

    /**
     * 获得系统各个view的高宽度
     */
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
        //        if (aVoid.getMessage().getMessageval().equals("post_newthread_succeed")) {
//            finish();
//        }
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
