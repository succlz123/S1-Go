package org.succlz123.s1go.app.dao.Helper;

import java.lang.reflect.Field;

/**
 * Created by fashi on 2015/4/12.
 */
public class S1Fid {
    public static final String S134 = "热血魔兽";
    public static final String S138 = "DOTA";
    public static final String S118 = "真碉堡山 Diablo3";
    public static final String S132 = "炉石传说";
    public static final String S105 = "FF 14";
    public static final String S131 = "车";
    public static final String S69 = "怪物猎人";
    public static final String S76 = "车欠女未";
    public static final String S111 = "英雄联盟(LOL)";

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

    public static final String S27 = "内野";
    public static final String S9 = "仓库-游戏精华备份区";
    public static final String S15 = "本垒";

    public static String GetS1Fid(int fid) {
        String name = "S" + fid;
        Class<S1Fid> c = S1Fid.class;
        Field field = null;
        try {
            field = c.getDeclaredField(name);
            return field.get(null).toString();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;

//        switch (fid) {
//            case 134:
//                return S134;
//            case 138:
//                return S138;
//            case 118:
//                return S118;
//            case 132:
//                return S132;
//            case 105:
//                return S105;
//            case 131:
//                return S131;
//            case 69:
//                return S69;
//            case 76:
//                return S76;
//            case 111:
//                return S111;
//            case 4:
//                return S4;
//            case 97:
//                return S97;
//            case 102:
//                return S102;
//            case 135:
//                return S135;
//            case 6:
//                return S6;
//            case 83:
//                return S83;
//            case 130:
//                return S130;
//            case 136:
//                return S136;
//            case 48:
//                return S48;
//            case 24:
//                return S24;
//            case 51:
//                return S51;
//            case 80:
//                return S80;
//            case 124:
//                return S124;
//            case 50:
//                return S50;
//            case 120:
//                return S120;
//            case 31:
//                return S31;
//            case 77:
//                return S77;
//            case 75:
//                return S75;
//            case 123:
//                return S123;
//            case 93:
//                return S93;
//            case 74:
//                return S74;
//            case 101:
//                return S101;
//            case 87:
//                return S87;
//            case 109:
//                return S109;
//            case 71:
//                return S71;
//            case 100:
//                return S100;
//            case 133:
//                return S133;
//            case 82:
//                return S82;
//            case 110:
//                return S110;
//            case 53:
//                return S53;
//            case 66:
//                return S66;
//            case 78:
//                return S78;
//            case 79:
//                return S79;
//            case 94:
//                return S94;
//            case 56:
//                return S56;
//            case 36:
//                return S36;
//            case 116:
//                return S116;
//            case 113:
//                return S113;
//            case 115:
//                return S115;
//            case 122:
//                return S122;
//            case 125:
//                return S125;
//            case 85:
//                return S85;
//            case 26:
//                return S26;
//            case 45:
//                return S45;
//            case 42:
//                return S42;
//            case 41:
//                return S41;
//            case 43:
//                return S43;
//            case 46:
//                return S46;
//            case 33:
//                return S33;
//            case 49:
//                return S49;
//            case 61:
//                return S61;
//            case 60:
//                return S60;
//            case 27:
//                return S27;
//        }
//        return null;
    }
}
