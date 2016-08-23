package org.succlz123.s1go.app.utils.common;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by succlz123 on 16/5/4.
 */
public class FragmentUtils {

    public static void commit(Activity activity, FragmentManager manager, FragmentTransaction transaction) {
        if (activity.isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
            return;
        }
        transaction.commitAllowingStateLoss();
        manager.executePendingTransactions();
    }
}
