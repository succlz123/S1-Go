package org.succlz123.s1go.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by succlz123 on 2016/11/13.
 */
public class PicHelper {
    private static final String CURRENT_SETTING_AVATAR = "current_setting_avatar";
    private static final String CURRENT_SETTING_PIC = "current_setting_pic";

    public static int sSettingAvatar;
    public static int sSettingPic;

    public static final int AVATAR_WIFI_BIG = 1;
    public static final int AVATAR_WIFI_MIDDLE = 2;
    public static final int AVATAR_WIFI_SMALL = 3;
    public static final int AVATAR_ALL_BIG = 4;
    public static final int AVATAR_ALL_MIDDLE = 5;
    public static final int AVATAR_ALL_SMALL = 6;
    public static final int AVATAR_NOT_DISPLAY = 7;

    public static final int PIC_WIFI = 1;
    public static final int PIC_ALL = 2;
    public static final int PIC_NOT_DISPLAY = 3;

    public static final String[] avatarItems = new String[]
            {
                    "WIFI加载头像大图",
                    "WIFI加载头像中图",
                    "WIFI加载头像小图",
                    "无限制加载头像大图",
                    "无限制加载头像中图",
                    "无限制加载头像小图",
                    "不加载头像"
            };

    public static final String[] picItems = new String[]
            {
                    "WIFI加载图片",
                    "无限制加载图片",
                    "不加载图片"
            };

    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public static void setAvatarType(Context context, int type) {
        sSettingAvatar = type;
        getSharePreference(context).edit()
                .putInt(CURRENT_SETTING_AVATAR, type)
                .apply();
    }

    public static int getAvatarType(Context context) {
        if (sSettingAvatar != 0) {
            return sSettingAvatar;
        }
        sSettingAvatar = getSharePreference(context).getInt(CURRENT_SETTING_AVATAR, AVATAR_WIFI_SMALL);
        return sSettingAvatar;
    }

    public static String getAvatarText(Context context) {
        int type = PicHelper.getAvatarType(context);
        return avatarItems[type - 1];
    }

    public static void setPicType(Context context, int type) {
        sSettingPic = type;
        getSharePreference(context).edit()
                .putInt(CURRENT_SETTING_PIC, type)
                .apply();
    }

    public static int getPicType(Context context) {
        if (sSettingPic != 0) {
            return sSettingPic;
        }
        sSettingPic = getSharePreference(context).getInt(CURRENT_SETTING_PIC, PIC_WIFI);
        return sSettingPic;
    }

    public static String getPicText(Context context) {
        int type = PicHelper.getPicType(context);
        return picItems[type - 1];
    }
}
