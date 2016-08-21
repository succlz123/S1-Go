package org.succlz123.s1go.app.ui.thread.send;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.ui.emoticon.EmoticonFragment;
import org.succlz123.s1go.app.utils.common.SysUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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


/**
 * Created by fashi on 2015/4/19.
 */
public class SendThreadsActivity extends BaseToolbarActivity {
    private static final int TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS = 0;
    private static final int TEXT_IS_NOT_EMPTY_AND_SET_THREADS = 1;

    private static final int THREADS_TITLE_MIN = 5;
    private static final int THREADS_TITLE_MAX = 80;
    private static final int THREADS_CONTENT_MIN = 5;
    private static final int THREADS_CONTENT_MAX = 10000;

    public static final String KEY_FID = "key_post_fid";

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

    public static void start(Context context, String fid) {
        Intent intent = new Intent(context, SendThreadsActivity.class);
        intent.putExtra(KEY_FID, fid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setthreads);

        mFid = getIntent().getStringExtra(KEY_FID);
//        mFormhash = getIntent().getStringExtra(S1GoConfig.FORM_HASH);
//        intent.putExtra(S1GoConfig.FORM_HASH, formhash);

        mRootView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mTilteEdit = f(R.id.setthreads_title);
        mContentEdit = f(R.id.setthreads_content);
        mMoveLinearLayout = f(R.id.move_linearlayout);
        mEmoticonView = f(R.id.emoticon_fragment);
        emoticonFragment = new EmoticonFragment();
        mDivideLinear = f(R.id.linear_view);

        setCustomTitle(getString(R.string.set_threads));

        getChangeHeight();
        onEditTextChangedListener();
        setEditTextFocusChangeListener();
        setEditTextClickListener();

        //				String cookie = MainApplication.getInstance().getLoginInfo().getCookiepre();
//				String auth = "auth=" + Uri.encode(MainApplication.getInstance().getLoginInfo().getAuth());
//				String saltkey = "saltkey=" + MainApplication.getInstance().getLoginInfo().getSaltkey();
//				String formhash = MainApplication.getInstance().getLoginInfo().getFormhash();

        String noticetrimstr = "";
        String subject = mTitle;
        String message = mContent;
        String mobiletype = "0";

//				this.mHearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
//				this.mBody.put("formhash", formhash);
//				this.mBody.put("noticetrimstr", noticetrimstr);
//				this.mBody.put("subject", subject);
//				this.mBody.put("message", message);
//				this.mBody.put("mobiletype", mobiletype);

//        if (aVoid.getMessage().getMessageval().equals("post_newthread_succeed")) {
//            finish();
//        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //表情 menu
//        MenuItem emoticonItem = menu.add(Menu.NONE, 1, 100, "表情");
//        Drawable emoticonDrawable = getDrawable(0);
//        emoticonDrawable.setTint(getResources().getColor(R.color.translucence_white));
//        if (mEmoticonOk) {
//            emoticonDrawable.setTint(getResources().getColor(R.color.white));
//        }
//        emoticonItem.setEnabled(mEmoticonOk);
//        emoticonItem.setIcon(emoticonDrawable);
//        emoticonItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        //发送 menu
//        MenuItem postItem = menu.add(Menu.NONE, 2, 100, "发帖");
//        Drawable postDrawable = getDrawable(0);
//        //drawable 染成半透明颜色
//        postDrawable.setTint(getResources().getColor(R.color.translucence_white));
//        mPostOk = false;
//        //最后发送时 异步线程任务所需要的信息
//        mTitle = mTilteEdit.getText().toString();
//        mContent = mContentEdit.getText().toString();
//        if (mTitle.length() > THREADS_TITLE_MIN && mTitle.length() < THREADS_TITLE_MAX
//                && mContent.length() > THREADS_CONTENT_MIN && mContent.length() < THREADS_CONTENT_MAX) {
//            postDrawable.setTint(getResources().getColor(R.color.white));
//            mPostOk = true;
//        }
//        postItem.setEnabled(mPostOk);
//        postItem.setIcon(postDrawable);
//        postItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
                if (!SysUtils.isFastClick()) {
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
        } else {
            if (!TextUtils.isEmpty(mContentEdit.getText().toString())) {
                dialog(TEXT_IS_NOT_EMPTY_AND_GIVE_UP_THREADS);
            } else if (TextUtils.isEmpty(mContentEdit.getText().toString())) {
                super.onBackPressed();
            }
        }
    }
}
