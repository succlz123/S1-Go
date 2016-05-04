package org.succlz123.s1go.app.utils.s1;

import java.lang.reflect.Field;

/**
 * Created by fashi on 2015/4/12.
 */
public class S1Fid {
    /**
     * 热门新区
     */
    public static final String S134 = "热血魔兽";
    public static final String S138 = "DOTA";
    public static final String S118 = "真碉堡山 Diablo3";
    public static final String S132 = "炉石传说";
    public static final String S105 = "FF 14";
    public static final String S131 = "车";
    public static final String S69 = "怪物猎人";
    public static final String S76 = "车欠女未";
    public static final String S111 = "英雄联盟(LOL)";

    /**
     * 主论坛
     */
    public static final String S4 = "游戏论坛";
    public static final String S97 = "蛋头电玩贩卖区";
    public static final String S102 = "大众软件精华区";

    public static final String S135 = "手游页游";

    public static final String S6 = "动漫论坛";
    public static final String S83 = "动漫投票鉴赏";
    public static final String S130 = "番且炒弹";

    public static final String S136 = "模玩专区";
    public static final String S48 = "影视论坛";
    public static final String S24 = "音乐论坛";

    public static final String S51 = "ＰＣ数码";
    public static final String S80 = "摄影区";
    public static final String S124 = "开箱区";

    public static final String S50 = "文史沙龙";
    public static final String S120 = "读书会";

    public static final String S31 = "彼岸文化";
    public static final String S77 = "八卦体育";

    public static final String S75 = "外野";
    public static final String S123 = "吃货";
    public static final String S93 = "火暴";
    public static final String S74 = "马叉虫";
    public static final String S101 = "犭苗犭句";
    public static final String S87 = "圣？战！日>_";
    public static final String S109 = "菠菜";
    public static final String S71 = "火星";
    public static final String S100 = "哥欠";

    public static final String S133 = "剑灵";

    public static final String S82 = "网游综合讨论区";
    public static final String S110 = "洛奇英雄传";
    public static final String S53 = "魔兽世界";
    public static final String S66 = "光荣网游版";
    public static final String S78 = "地下城勇士(DNF)";
    public static final String S79 = "暴雪游戏专区";
    public static final String S94 = "永恒之塔";
    public static final String S56 = "梦幻之星 Phantasy Star";
    public static final String S36 = "仙境传说R O.";
    public static final String S116 = "剑网3";
    public static final String S113 = "第九大陆C9";

    public static final String S115 = "二手交易区";
    public static final String S119 = "内有小电影";

    /**
     * 主题公园
     */
    public static final String S122 = "将军";
    public static final String S125 = "热血海贼王";
    public static final String S85 = "生化危机共和国";
    public static final String S26 = "女神转生";
    public static final String S45 = "风来西林";
    public static final String S42 = "任天堂专区";
    public static final String S41 = "无双论坛";
    public static final String S43 = "幻水圣域";
    public static final String S46 = "重装机兵";
    public static final String S33 = "异度传说";
    public static final String S49 = "街";
    public static final String S61 = "LIVE A LIVE";
    public static final String S60 = "激战2";

    /**
     * 子论坛
     */
    public static final String S27 = "内野";
    public static final String S9 = "仓库-游戏精华备份区";
    public static final String S15 = "本垒";

    public static String getS1Fid(int fid) {
        String name = "S" + fid;
        Class<S1Fid> c = S1Fid.class;
        Field field = null;
        try {
            field = c.getDeclaredField(name);
            return field.get(null).toString();
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }

    public static String getS1Fid(String fid) {
        String name = "S" + fid;
        Class<S1Fid> c = S1Fid.class;
        Field field = null;
        try {
            field = c.getDeclaredField(name);
            return field.get(null).toString();
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }
}
