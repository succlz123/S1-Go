package org.succlz123.s1go.app.utils.common;


import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.DimenRes;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

/**
 * Created by succlz123 on 16/5/4.
 */
public class DimenUtils {
    private static int sScreenWidth = -1;
    private static int sScreenHeight = -1;
    private static int sStatusBarHeight = -1;

    public static int getScreenWidth() {
        if (sScreenWidth == -1) {
            sScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        if (sScreenHeight == -1) {
            sScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        }
        return sScreenHeight;
    }

    public static int dip2px(float dpValue) {
        final float scale = MainApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = MainApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);

//        final DisplayMetrics displayMetrics = MainApplication.getInstance().getResources().getDisplayMetrics();
//        return TypedValue.complexToDimensionPixelSize((int) pxValue, displayMetrics);
    }

    public static float getDimen(@DimenRes int dimenResId) {
        return MainApplication.getInstance().getApplicationContext().getResources().getDimension(dimenResId);
    }

    public static int getDimensionPixelSize(@DimenRes int dimenResId) {
        return MainApplication.getInstance().getApplicationContext().getResources().getDimensionPixelSize(dimenResId);
    }

    public static int getAttrData(Activity activity, @AttrRes int attResId) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attResId, typedValue, true);
        return typedValue.data;
    }

    public static int getActionBarHeight(AppCompatActivity appCompatActivity) {
        int actionBarHeight = 0;
        if (appCompatActivity.getSupportActionBar() != null) {
            actionBarHeight = appCompatActivity.getSupportActionBar().getHeight();
            if (actionBarHeight != 0) {
                return actionBarHeight;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    getAttrData(appCompatActivity, R.attr.actionBarSize),
                    Resources.getSystem().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static int getStatusBarHeight(Context context) {
        if (sScreenHeight == -1) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return sStatusBarHeight;
    }

    public static int getItemSize(int spaceSize, int spanCount) {
        return (DimenUtils.getScreenWidth() - DimenUtils.dip2px(spaceSize) * (spanCount + 1)) / spanCount;
    }
}
