package org.succlz123.s1go.app.ui.picture;

import org.qiibeta.bitmapview.BitmapSource;
import org.qiibeta.bitmapview.GestureBitmapView;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.common.ToastUtils;
import org.succlz123.s1go.app.utils.common.ViewUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by succlz123 on 2015/4/17.
 */
public class PictureActivity extends BaseToolbarActivity implements PicContract.View {
    public static final String KEY_IMAGE_URL = "image_url";

    private String mUrl;
    private GestureBitmapView mBitmapView;
    private PicContract.Presenter mPicPresenter;

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, PictureActivity.class);
        starter.putExtra(KEY_IMAGE_URL, url);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ensureToolbar();
        showBackButton();
        setTitle("看图");
        mUrl = getIntent().getStringExtra(KEY_IMAGE_URL);
        mBitmapView = ViewUtils.f(this, R.id.img);
        new PicPresenter(new PicDataSource(), this);
        mPicPresenter.getBitmap(mUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, 100, 102, "保存图片到/SDCard/Pictures");
        item.setIcon(R.drawable.ic_near_me_white_48dp);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case 100:
                mPicPresenter.savePic(mUrl);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPicPresenter.unSubscribe();
        super.onDestroy();
    }

    @Override
    public void setPresenter(PicContract.Presenter presenter) {
        mPicPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return !isFinishing() && !isDestroyed();
    }

    @Override
    public void setBitmapSource(String file, Bitmap bitmap) {
        try {
            mBitmapView.setBitmapSource(BitmapSource.newInstance(file, bitmap));
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToastLong(this, "呃呃呃");
        }
    }

    @Override
    public void onSaveSuccess() {
        ToastUtils.showToastShort(this, "保存图片成功");
    }

    @Override
    public void onSaveFailed() {
        ToastUtils.showToastShort(this, "保存图片失败");
    }
}

