package org.succlz123.s1go.utils.common;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ToastUtils {
    private static WeakReference<Toast> sLastToast;
    
    public static void showToast(Context context, int textResId, int duration) {
        if (context != null)
            showToast(context, context.getString(textResId), duration);
    }

    public static void showToast(Context context, String text, int duration) {
        if (context != null){
            if (sLastToast != null) {
                Toast toast = sLastToast.get();
                if (toast != null)
                    toast.cancel();
            }
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            sLastToast = new WeakReference<Toast>(toast);
        }
    }

    public static void cancel(){
        if (sLastToast != null) {
            Toast toast = sLastToast.get();
            if (toast != null)
                toast.cancel();
        }
    }

    public static void showToastLong(Context context, int textResId) {
        showToast(context, textResId, Toast.LENGTH_LONG);
    }

    public static void showToastLong(Context context, String text) {
        showToast(context, text, Toast.LENGTH_LONG);
    }

    public static void showToastShort(Context context, int textResId) {
        showToast(context, textResId, Toast.LENGTH_SHORT);
    }

    public static void showToastShort(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showViewToast(Context context, int layoutId, int duration) {
        if (context != null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(layoutId, null);
            showViewToast(context, layout, duration);
        }
    }

    public static void showViewToast(Context context, View view, int duration) {
        if (context != null) {
            Toast toast = new Toast(context);
            toast.setDuration(duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(view);
            toast.show();
        }
    }

    public static void showViewToastShort(Context context, View view) {
        showViewToast(context, view, Toast.LENGTH_SHORT);
    }

    public static void showViewToastLong(Context context, View view) {
        showViewToast(context, view, Toast.LENGTH_LONG);
    }

    public static void showViewToastShort(Context context, int layoutId) {
        showViewToast(context, layoutId, Toast.LENGTH_SHORT);
    }

    public static void showViewToastLong(Context context, int layoutId) {
        showViewToast(context, layoutId, Toast.LENGTH_LONG);
    }
}
