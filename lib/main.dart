import 'dart:async';
import 'package:flutter/rendering.dart' show debugPaintSizeEnabled;

import 'package:flutter/material.dart';
import 'package:s1_go/home_fragment.dart';
import 'package:s1_go/hot_fragment.dart';
import 'package:s1_go/my_fragment.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    debugPaintSizeEnabled = false;
    return new MaterialApp(
        title: 'Stage1st Go',
        theme: new ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: MainPageWidget(title: 'Stage1st Go'));
  }
}

class MainPageWidget extends StatefulWidget {
  final String title;

  MainPageWidget({Key key, this.title}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return new MainPageState();
  }
}

class MainPageState extends State<MainPageWidget> {
  int _tabIndex = 0;
  var tabImages;
  var appBarTitles = ['节点', '热门', '我的'];
  var _bodys;

  @override
  Widget build(BuildContext context) {
    initData();
    return Scaffold(
      appBar: new AppBar(
        title: new Text(widget.title),
      ),
      body: Stack(
        children: List.generate(_bodys.length, (int i) {
          return Offstage(offstage: i != _tabIndex, child: _bodys[i]);
        }),
      ),
      bottomNavigationBar: new BottomNavigationBar(
        items: <BottomNavigationBarItem>[
          new BottomNavigationBarItem(
              icon: getTabIcon(0), title: getTabTitle(0)),
          new BottomNavigationBarItem(
              icon: getTabIcon(1), title: getTabTitle(1)),
          new BottomNavigationBarItem(
              icon: getTabIcon(2), title: getTabTitle(2)),
        ],
        type: BottomNavigationBarType.fixed,
        currentIndex: _tabIndex,
        onTap: (index) {
          setState(() {
            _tabIndex = index;
          });
        },
      ),
    );
  }

  void initData() {
    var selectColor = Colors.blue;
    var unselectColor = Colors.grey;

    tabImages = [
      [
        getTabImage(
            'images/ic_panorama_fish_eye_white_48dp.png', unselectColor),
        getTabImage('images/ic_panorama_fish_eye_white_48dp.png', selectColor)
      ],
      [
        getTabImage('images/ic_hdr_weak_white_48dp.png', unselectColor),
        getTabImage('images/ic_hdr_weak_white_48dp.png', selectColor)
      ],
      [
        getTabImage('images/ic_all_inclusive_white_48dp.png', unselectColor),
        getTabImage('images/ic_all_inclusive_white_48dp.png', selectColor)
      ]
    ];

    _bodys = [new HomeFragment(), new HotFragment(), new MyFragment()];
  }

  Image getTabImage(path, color) {
    return new Image.asset(
      path,
      width: 20.0,
      height: 20.0,
      color: color,
    );
  }

  Image getTabIcon(int curIndex) {
    if (curIndex == _tabIndex) {
      return tabImages[curIndex][1];
    }
    return tabImages[curIndex][0];
  }

  Text getTabTitle(int curIndex) {
    if (curIndex == _tabIndex) {
      return new Text(appBarTitles[curIndex],
          style: new TextStyle(color: Colors.blue));
    } else {
      return new Text(appBarTitles[curIndex],
          style: new TextStyle(color: Colors.grey));
    }
  }
}
