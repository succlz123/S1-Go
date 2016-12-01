package org.succlz123.s1go.app.utils.fromhtml;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.utils.common.ToastUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by succlz123 on 2015/5/24.
 */
public class UrlDrawable extends BitmapDrawable {
    private Drawable mDrawable;

    public UrlDrawable(int size) {
        super(MainApplication.getInstance().getResources(), Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444));
    }

    public void setActualDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDrawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapdrawable = (BitmapDrawable) mDrawable;
            if (bitmapdrawable.getBitmap().isRecycled()) {
                return;
            }
        }
        if (mDrawable != null) {
            try {
                mDrawable.draw(canvas);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showToastLong(MainApplication.getContext(), "bad bitmap!!!");
            }
        } else {
            canvas.drawColor(Color.RED);
        }
    }

    @Override
    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
        if (mDrawable != null) {
            mDrawable.setBounds(bounds);
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        if (mDrawable != null) {
            mDrawable.setBounds(left, top, right, bottom);
        }
    }
}
