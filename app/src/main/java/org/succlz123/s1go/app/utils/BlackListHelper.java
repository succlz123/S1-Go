package org.succlz123.s1go.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by succlz123 on 2016/11/13.
 */
public class BlackListHelper {
    private static final String CURRENT_BLACK_SET = "current_black_set";
    public static Set<String> sBlackList;

    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("black_list", Context.MODE_PRIVATE);
    }

    public static void setBlackList(Context context, String userName) {
        if (TextUtils.isEmpty(userName)) {
            return;
        }
        getBlackList(context).add(userName);
        HashSet<String> tepSet = new HashSet<>();
        for (String aSBlackList : sBlackList) {
            tepSet.add(aSBlackList);
        }
        getSharePreference(context).edit()
                .putStringSet(CURRENT_BLACK_SET, tepSet)
                .apply();
    }

    public static Set<String> getBlackList(Context context) {
        if (sBlackList != null) {
            return sBlackList;
        }
        sBlackList = new HashSet<>();
        sBlackList = getSharePreference(context).getStringSet(CURRENT_BLACK_SET, sBlackList);
        return sBlackList;
    }

    public static boolean delete(Context context, String userName) {
        boolean isSuccess;
        if (sBlackList != null) {
            return sBlackList.remove(userName);
        }
        sBlackList = new HashSet<>();
        sBlackList = getSharePreference(context).getStringSet(CURRENT_BLACK_SET, sBlackList);
        isSuccess = sBlackList.remove(userName);
        HashSet<String> tepSet = new HashSet<>();
        for (String aSBlackList : sBlackList) {
            tepSet.add(aSBlackList);
        }
        getSharePreference(context).edit()
                .putStringSet(CURRENT_BLACK_SET, tepSet)
                .apply();
        return isSuccess;
    }

    public static boolean isBlack(Context context, String userName) {
        if (sBlackList != null) {
            return sBlackList.contains(userName);
        }
        sBlackList = getBlackList(context);
        if (sBlackList == null) {
            sBlackList = new HashSet<>();
            return false;
        }
        return sBlackList.contains(userName);
    }
}
