package org.succlz123.s1go.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by succlz123 on 2016/11/13.
 */
public class AvatarHelper {
    private static final String CURRENT_SETTING_AVATER = "current_setting_avatar";
    public static int sSettingAvatar;

    public static final int WIFI_BIG = 1;
    public static final int WIFI_MIDDLE = 2;
    public static final int WIFI_SMALL = 3;
    public static final int ALL_BIG = 4;
    public static final int ALL_MIDDLE = 5;
    public static final int ALL_SMALL = 6;

    public static final String[] items = new String[]
            {
                    "WIFI加载头像大图",
                    "WIFI加载头像中图",
                    "WIFI加载头像小图",
                    "无限制加载头像大图",
                    "无限制加载头像中图",
                    "无限制加载头像小图"
            };

    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public static void setAvatarType(Context context, int type) {
        sSettingAvatar = type;
        getSharePreference(context).edit()
                .putInt(CURRENT_SETTING_AVATER, type)
                .apply();
    }

    public static int getAvatarType(Context context) {
        if (sSettingAvatar != 0) {
            return sSettingAvatar;
        }
        sSettingAvatar = getSharePreference(context).getInt(CURRENT_SETTING_AVATER, WIFI_SMALL);
        return sSettingAvatar;
    }

    public static String getDisplayText(Context context) {
        int type = AvatarHelper.getAvatarType(context);
        return items[type - 1];
    }
}
