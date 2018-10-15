package org.succlz123.s1go.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class SettingHelper {
    private static final String CURRENT_SETTING_PHONE_TAIL = "current_setting_phone_tail";
    private static boolean sSettingPhoneTail;

    private static final String CURRENT_SETTING_URL = "current_setting_url";
    private static boolean sSettingUrl;

    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public static boolean togglePhoneTail(Context context) {
        isShowPhoneTail(context);
        sSettingPhoneTail = !sSettingPhoneTail;
        getSharePreference(context).edit()
                .putBoolean(CURRENT_SETTING_PHONE_TAIL, sSettingPhoneTail)
                .apply();
        return sSettingPhoneTail;
    }

    public static boolean isShowPhoneTail(Context context) {
        if (sSettingPhoneTail) {
            return true;
        }
        sSettingPhoneTail = getSharePreference(context).getBoolean(CURRENT_SETTING_PHONE_TAIL, false);
        return sSettingPhoneTail;
    }

    public static boolean toggleUrl(Context context) {
        isUrlIp(context);
        sSettingUrl = !sSettingUrl;
        getSharePreference(context).edit()
                .putBoolean(CURRENT_SETTING_URL, sSettingUrl)
                .apply();
        return sSettingUrl;
    }

    public static boolean isUrlIp(Context context) {
        if (sSettingUrl) {
            return true;
        }
        sSettingUrl = getSharePreference(context).getBoolean(CURRENT_SETTING_URL, false);
        return sSettingUrl;
    }
}
