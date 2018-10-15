package org.succlz123.htmlview.image;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by succlz123 on 2017/6/23.
 */

@SuppressWarnings("deprecation")
public class UrlDrawable extends BitmapDrawable {
    private static final String TAG = "UrlDrawable";
    private Drawable mDrawable;

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
                Log.e(TAG, "bad bitmap! " + e.toString());
            }
        }
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
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
