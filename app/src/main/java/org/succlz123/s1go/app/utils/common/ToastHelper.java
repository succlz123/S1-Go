package org.succlz123.s1go.app.utils.common;

import org.succlz123.s1go.app.MainApplication;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by succlz123 on 16/6/4.
 */
public class ToastHelper {
    private static Toast toast;

    /**
     * 全局Toast
     */
    private synchronized static void globalToast(Context context, String tip, int duration) {
        if (toast != null) {
            toast.setText(tip);
            toast.setDuration(duration);
        } else {
            toast = Toast.makeText(context, tip, duration);
        }
        toast.show();
    }

    /**
     * 显示Toast 时间为short
     */
    public static void showShort(String tip) {
        if (TextUtils.isEmpty(tip)) {
            tip = "unknown error";
        }
        globalToast(MainApplication.getInstance().getApplicationContext(), tip, Toast.LENGTH_SHORT);
    }

    public static void showShort(int resStringId) {
        String tip = MainApplication.getInstance().getApplicationContext().getString(resStringId);
        if (TextUtils.isEmpty(tip)) {
            tip = "unknown error";
        }
        globalToast(MainApplication.getInstance().getApplicationContext(), tip, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast 时间为long
     */
    public static void showLong(int resStringId) {
        String tip = MainApplication.getInstance().getApplicationContext().getString(resStringId);
        if (TextUtils.isEmpty(tip)) {
            tip = "unknown error";
        }
        globalToast(MainApplication.getInstance().getApplicationContext(), tip, Toast.LENGTH_LONG);
    }

    public static void showLong(String tip) {
        if (TextUtils.isEmpty(tip)) {
            tip = "unknown error";
        }
        globalToast(MainApplication.getInstance().getApplicationContext(), tip, Toast.LENGTH_LONG);
    }
}
