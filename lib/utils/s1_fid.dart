class S1Fid {
  // 热门新区
  static Map<int, String> _hotArea;

  static Map<int, String> get hotArea {
    if (_hotArea == null) {
      _hotArea = Map();
      _hotArea[140] = S140;
      _hotArea[132] = S132;
      _hotArea[138] = S138;
      _hotArea[135] = S135;
      _hotArea[111] = S111;
      _hotArea[82] = S82;

      _hotArea[118] = S118;
      _hotArea[53] = S53;
      _hotArea[78] = S78;
      _hotArea[79] = S79;
      _hotArea[116] = S116;
      _hotArea[60] = S60;
    }
    return _hotArea;
  }

  static const String S140 = "页游S1官方联运";
  static const String S132 = "炉石传说";
  static const String S138 = "DOTA";
  static const String S135 = "手游页游";
  static const String S111 = "英雄联盟(LOL)";
  static const String S82 = "新网游综合讨论区";

  static const String S118 = "真碉堡山 Diablo3";
  static const String S53 = "魔兽世界";
  static const String S78 = "地下城勇士(DNF)";
  static const String S79 = "暴雪游戏专区";
  static const String S116 = "剑网3";
  static const String S60 = "激战2";

  // 主论坛
  static Map<int, String> _mainArea;

  static Map<int, String> get mainArea {
    if (_mainArea == null) {
      _mainArea = Map();
      _mainArea[4] = S4;
      _mainArea[97] = S97;
      _mainArea[69] = S69;

      _mainArea[144] = S144;

      _mainArea[6] = S6;
      _mainArea[83] = S83;

      _mainArea[136] = S136;
      _mainArea[48] = S48;
      _mainArea[24] = S24;

      _mainArea[51] = S51;
      _mainArea[80] = S80;

      _mainArea[50] = S50;
      _mainArea[120] = S120;

      _mainArea[31] = S31;
      _mainArea[77] = S77;

      _mainArea[75] = S75;
      _mainArea[93] = S93;
      _mainArea[74] = S74;
      _mainArea[101] = S101;
      _mainArea[131] = S131;
      _mainArea[76] = S76;
      _mainArea[109] = S109;
      _mainArea[123] = S123;
      _mainArea[100] = S100;

      _mainArea[115] = S115;
    }
    return _mainArea;
  }

  static const String S4 = "游戏论坛";
  static const List<int> L4 = [4, 97, 69];
  static const String S97 = "蛋头电玩贩卖区";
  static const String S69 = "怪物猎人";

  static const String S144 = "欧美动漫";

  static const String S6 = "动漫论坛";
  static const List<int> L6 = [6, 83];
  static const String S83 = "动漫投票鉴赏";

  static const String S136 = "模玩专区";
  static const String S48 = "影视论坛";
  static const String S24 = "音乐论坛";

  static const String S51 = "ＰＣ数码";
  static const List<int> L51 = [51, 80];
  static const String S80 = "摄影区";

  static const String S50 = "文史沙龙";
  static const List<int> L50 = [50, 120];
  static const String S120 = "读书会";

  static const String S31 = "彼岸文化";
  static const String S77 = "八卦体育";

  static const String S75 = "外野";
  static const List<int> L75 = [75, 93, 74, 101, 131, 76, 109, 123, 100];
  static const String S93 = "火暴";
  static const String S74 = "马叉虫";
  static const String S101 = "犭苗犭句";
  static const String S131 = "车";
  static const String S76 = "车欠女未";
  static const String S109 = "菠菜";
  static const String S123 = "吃货";
  static const String S100 = "哥欠";

  static const String S115 = "二手交易区";

  // 子论坛
  static Map<int, String> _subArea;

  static Map<int, String> get subArea {
    if (_subArea == null) {
      _subArea = Map();
      _subArea[27] = S27;
      _subArea[9] = S9;
      _subArea[15] = S15;
      _subArea[119] = S119;
    }
    return _subArea;
  }

  static const String S27 = "内野";
  static const List<int> L27 = [27, 9, 15, 119];
  static const String S9 = "仓库-游戏精华备份区";
  static const String S15 = "本垒";
  static const String S119 = "内有小电影";

  static Map<int, String> _allArea;

  static Map<int, String> get allArea {
    if (_allArea == null) {
      _allArea = Map();
      _allArea.addAll(hotArea);
      _allArea.addAll(mainArea);
      _allArea.addAll(subArea);
    }
    return _allArea;
  }

  static String getS1FidName(int fid) {
    return _allArea[fid];
  }

  //  static String[] getS1ChildFidName(String fid) {
  //     String name = "L" + fid;
  //     Class<S1Fid> c = S1Fid.class;
  //     Field field = null;
  //     try {
  //         field = c.getDeclaredField(name);
  //         return (String[]) field.get(null);
  //     } catch (NoSuchFieldException | IllegalAccessException e) {
  //         if (BuildConfig.DEBUG) {
  //             e.printStackTrace();
  //         }
  //     }
  //     return null;
  // }

  //  static String getS1ChildFidNumber(String fid) {
  //     String name = "L" + fid;
  //     Class<S1Fid> c = S1Fid.class;
  //     Field field = null;
  //     try {
  //         field = c.getDeclaredField(name);
  //         return field.get(null).toString();
  //     } catch (NoSuchFieldException | IllegalAccessException e) {
  //         if (BuildConfig.DEBUG) {
  //             e.printStackTrace();
  //         }
  //     }
  //     return null;
  // }

  //  static String getFidFormName(String name) {
  //     Class<S1Fid> c = S1Fid.class;
  //     Field[] fields = c.getFields();
  //     try {
  //         for (Field field : fields) {
  //             // fuck Instant Run
  //             if (TextUtils.equals(field.getName(), "$change")) {
  //                 continue;
  //             }
  //             Object o = field.get(null);
  //             if (o instanceof String) {
  //                 if (TextUtils.equals(name, (CharSequence) o)) {
  //                     return field.getName().replace("S", "");
  //                 }
  //             }
  //         }
  //     } catch (Exception e) {
  //         if (BuildConfig.DEBUG) {
  //             e.printStackTrace();
  //         }
  //     }
  //     return "1";
  // }
}
