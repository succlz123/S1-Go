package org.succlz123.s1go.app.utils.s1;

/**
 * Created by fashi on 2015/4/21.
 */
public class S1UidToAvatarUrl {
    public static final String USER_AVATAR = "http://bbs.saraba1st.com/2b/uc_server/avatar.php?uid=&size=small";
    public static final String AVATAR_SMALL = "http://bbs.saraba1st.com/2b/uc_server/data/avatar/000/00/00/00_avatar_small.jpg";
    public static final String AVATAR_MIDDLE = "http://bbs.saraba1st.com/2b/uc_server/data/avatar/000/00/00/00_avatar_middle.jpg";

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
        return AVATAR_MIDDLE.replace("000/00/00/00", uid);
    }
}
