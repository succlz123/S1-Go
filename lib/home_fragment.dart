import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:s1_go/utils/s1_fid.dart';

class HomeFragment extends StatefulWidget {
  final String title;

  HomeFragment({Key key, this.title}) : super(key: key);

  @override
  _HomeFragmentState createState() => new _HomeFragmentState();
}

class _HomeFragmentState extends State<HomeFragment> {
  var mainForum = [
    140,
    132,
    138,
    135,
    111,
    82,
    4,
    144,
    6,
    136,
    48,
    24,
    51,
    50,
    31,
    77,
    75,
    115,
    27
  ];

  static const _platform = const MethodChannel('org.succlz123.s1go/tid-name');

  var _tidMap = Map<int, String>();

  @override
  void initState() {
    super.initState();
    initData();
  }

  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;
    final double itemWidth = size.width / 2;
    final double itemHeight = itemWidth / 3.5;
    return new Scaffold(
        body: new GridView.builder(
      itemCount: _tidMap.length,
      itemBuilder: (BuildContext context, int position) {
        return getRow(position);
      },
      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        mainAxisSpacing: 10.0,
        crossAxisSpacing: 10.0,
        childAspectRatio: (itemWidth / itemHeight),
      ),
    ));
  }

  Widget getRow(int position) {
    double margin = 6;
    double left = 0;
    double right = 0;
    double top = 0;
    double bottom = 0;
    if (position == 0 || position == 1) {
      top = margin;
    } else if (position == mainForum.length - 1) {
      bottom = margin;
    }
    if (position % 2 == 0) {
      left = margin;
    } else {
      right = margin;
    }
    return new Padding(
        padding: new EdgeInsets.fromLTRB(left, top, right, bottom),
        child: new RaisedButton(
          color: Colors.white,
          child: new Text(
            _tidMap[(mainForum[position])],
            style: TextStyle(fontSize: 16, color: Colors.black),
          ),
          onPressed: () {
            go2NativeActivity((mainForum[position]));
          },
        ));
  }

  void go2NativeActivity(int tid) async {
    try {
      final String result =
          await _platform.invokeMethod('go2AreaActivity', {'tid': tid.toString()});
    } on PlatformException {}
  }

  Future<Null> initData() async {
    if (_tidMap.length > 0) {
      return;
    }
    for (var item in mainForum) {
      _tidMap[item] = S1Fid.allArea[item];
    }
    setState(() {});
  }
}
