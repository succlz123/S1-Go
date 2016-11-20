package org.succlz123.s1go.app.utils.s1;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.utils.AvatarHelper;
import org.succlz123.s1go.app.utils.network.NetworkManager;

import static org.succlz123.s1go.app.utils.AvatarHelper.ALL_BIG;
import static org.succlz123.s1go.app.utils.AvatarHelper.ALL_MIDDLE;
import static org.succlz123.s1go.app.utils.AvatarHelper.ALL_SMALL;
import static org.succlz123.s1go.app.utils.AvatarHelper.WIFI_BIG;
import static org.succlz123.s1go.app.utils.AvatarHelper.WIFI_MIDDLE;
import static org.succlz123.s1go.app.utils.AvatarHelper.WIFI_SMALL;
import static org.succlz123.s1go.app.utils.network.NetworkManager.NETWORK_TYPE_WIFI;

/**
 * Created by succlz123 on 2015/4/21.
 */
public class S1UidToAvatarUrl {
    public static final String USER_AVATAR = "http://bbs.saraba1st.com/2b/uc_server/avatar.php?uid=&size=small";
    public static final String AVATAR_SMALL = "http://bbs.saraba1st.com/2b/uc_server/data/avatar/000/00/00/00_avatar_small.jpg";
    public static final String AVATAR_MIDDLE = "http://bbs.saraba1st.com/2b/uc_server/data/avatar/000/00/00/00_avatar_middle.jpg";
    public static final String AVATAR_BIG = "http://bbs.saraba1st.com/2b/uc_server/data/avatar/000/00/00/00_avatar_big.jpg";

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
        int displayType = AvatarHelper.getAvatarType(MainApplication.getInstance().getApplicationContext());
        String url = "";
        switch (displayType) {
            case WIFI_BIG:
            case ALL_BIG:
                String big = AVATAR_BIG;
                url = big.replace("000/00/00/00", uid);
                break;
            case WIFI_MIDDLE:
            case ALL_MIDDLE:
                String middle = AVATAR_MIDDLE;
                url = middle.replace("000/00/00/00", uid);
                break;
            case WIFI_SMALL:
            case ALL_SMALL:
                String small = AVATAR_SMALL;
                url = small.replace("000/00/00/00", uid);
                break;
        }

        if (displayType <= 3 && NetworkManager.getNetworkType(MainApplication.getInstance().getApplicationContext()) != NETWORK_TYPE_WIFI) {
            url = "";
        }
        return url;
    }
}
