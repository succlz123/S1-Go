package org.succlz123.s1go.app.support.utils;

/**
 * Created by fashi on 2015/4/21.
 */
public class S1UidToAvatarUrl {

    public static String getAvatar(String uid) {
        if (uid.length() < 9) {
            int count = 9 - uid.length();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < count; i++) {
                builder.append("0");
            }
            uid = builder.toString() + uid;
            uid = uid.substring(0, 3) + "/" + uid.substring(3, 5) + "/" + uid.substring(5, 7) + "/" + uid.substring(7, 9);
        }
        String url = S1Url.AVATAR_MIDDLE.replace("000/00/00/00", uid);
        return url;
    }
}
