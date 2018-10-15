package org.succlz123.s1go.utils.common;


import org.succlz123.s1go.MainApplication;
import org.succlz123.s1go.ui.base.BaseActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.opengl.GLES10;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by succlz123 on 16/5/4.
 */
public class MyUtils {

    /**
     * dp转px
     */
    public static int dip2px(float dpVal) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, displayMetrics);
    }

    /**
     * sp转px
     */
    public static int sp2px(float spVal) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, displayMetrics);
    }

    /**
     * px转dp
     */
    public static float px2dip(float pxVal) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(float pxVal) {
        float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (pxVal / scaledDensity);
    }

    public static Drawable getDrawable(@DrawableRes int drawableResId) {
        return ContextCompat.getDrawable(MainApplication.getInstance().getApplicationContext(), drawableResId);
    }

    public static int getColor(@ColorRes int colorResId) {
        return ContextCompat.getColor(MainApplication.getInstance().getApplicationContext(), colorResId);
    }

    public static String getString(@StringRes int stringResId) {
        return MainApplication.getInstance().getApplicationContext().getString(stringResId);
    }

    public static Drawable createFillDrawable(@DimenRes int radiusDimenResId, @ColorRes int colorResId) {
        float radiusPx = DimenUtils.getDimen(radiusDimenResId);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radiusPx);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(getColor(colorResId));
        return drawable;
    }

    public static Drawable createBorderByStrokeDrawable(@DimenRes int radiusDimenResId, @DimenRes int strokeWidthDimenResId,
                                                        @ColorRes int backgroundColorResId, @ColorRes int StrokeColorResId) {
        float radiusPx = DimenUtils.getDimen(radiusDimenResId);
        int strokeWidthPx = DimenUtils.getDimensionPixelSize(strokeWidthDimenResId);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radiusPx);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(strokeWidthPx, getColor(StrokeColorResId));
        drawable.setColor(getColor(backgroundColorResId));
        return drawable;
    }

    public static BaseActivity getWrapperActivity(Context context) {
        if (context instanceof BaseActivity) {
            return (BaseActivity) context;
        }
        return null;
    }

    //added by Jack for handle exception "Bitmap too large to be uploaded into a texture".
    public boolean isNeedCloseHardwareAcceleration(int w, int h) {
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSize, 0);

        if (maxSize[0] < h || maxSize[0] < w) {
            return true;
        }

        return false;
    }
}
