import 'dart:async';
import 'dart:core';

import 'package:flutter/material.dart';
import 'package:s1_go/my_bloc.dart';
import 'package:s1_go/utils/s1_fid.dart';
import 'package:s1_go/widget/nothing.dart';

class MyFragment extends StatefulWidget {
  List<HotThreadData> datas;

  MyFragment();

  @override
  _MyFragmentState createState() => new _MyFragmentState();
}

class _MyFragmentState extends State<MyFragment>
    with AutomaticKeepAliveClientMixin {
  List widgets = [];
  final HotThreadBloc _bloc = new HotThreadBloc();
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    print(888);
    // _bloc.getHotThread().then((list) {
    //   if (!mounted) {
    //     return;
    //   }
    //   setState(() {
    //     _isLoading = false;
    //   });
    // });
  }

  @override
  void dispose() {
    super.dispose();
    _bloc.dispose();
  }

  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: StreamBuilder<List<HotThreadData>>(
          stream: _bloc.hotThreadResult,
          initialData: List(),
          builder: (BuildContext context,
              AsyncSnapshot<List<HotThreadData>> snapshot) {
            if (!snapshot.hasData || snapshot.data.isEmpty) {
              return unSignin();
            }
            return new ListView.builder(
                itemCount: snapshot.data?.length,
                itemBuilder: (BuildContext context, int position) {
                  return Nothing();
                });
          }),
    );
//
//        body:
  }

  Widget unSignin() {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        getHeader(false),
        getNightTheme(),
        getAvaterDisplayType(),
        getBlock(false),
      ],
    );
  }

  Widget signin(int i) {
    switch (i) {
      case 0:
        return getHeader(true);
      case 1:
        return getBlock(false);
      default:
        return Nothing();
    }
  }

  Widget getHeader(bool isSigin) {
    if (isSigin) {
    } else {
      return Container(
          padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          decoration: BoxDecoration(
              border: Border(
                  bottom: BorderSide(color: Theme.of(context).dividerColor))),
          alignment: Alignment.center,
          height: 180,
          child: new ClipOval(
            child: new SizedBox(
              width: 100.0,
              height: 100.0,
              child: new Image.asset(
                "images/ic_user_img.png",
                width: 100.0,
                height: 100.0,
                fit: BoxFit.fill,
              ),
            ),
          ));
    }
  }

  Widget getBlock(bool isSigin) {
    return Padding(
        padding: EdgeInsets.fromLTRB(0, 10, 0, 10),
        child: MaterialButton(
          height: 60.0,
          minWidth: double.infinity,
          child: Text(
            "个人黑名单",
            style: TextStyle(fontSize: 18, color: Colors.black),
          ),
          onPressed: () {},
        ));
  }

  bool select = false;

  Widget getNightTheme() {
    return Container(
        padding: EdgeInsets.fromLTRB(10, 20, 0, 10),
        child: Row(
          children: <Widget>[
            Expanded(
                flex: 1,
                child: Text(
                  "夜间模式",
                  style: TextStyle(fontSize: 18, color: Colors.black),
                )),
            Expanded(flex: 1, child: Nothing()),
            Expanded(
              flex: 0,
              child: Checkbox(
                  value: select,
                  onChanged: (bool cb) {
                    setState(() {
                      select = cb;
                      print(select);
                    });
                  }),
            )
          ],
        ));
  }

  Widget getAvaterDisplayType() {
    return Container(
        padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
        child: Row(
          children: <Widget>[
            Expanded(
                flex: 1,
                child: Text(
                  "头像显示策略",
                  style: TextStyle(fontSize: 18, color: Colors.black),
                )),
            Expanded(flex: 1, child: Nothing()),
            Expanded(
                flex: 0,
                child: Text(
                  "WIFI加载头像小图",
                  style: TextStyle(fontSize: 18, color: Colors.grey),
                )),
          ],
        ));
  }

  Widget getRow(List<HotThreadData> data, int i) {
    var content = data[i];
    if (content == null) {
      return Nothing();
    }
    int fid = int.tryParse(content.fid);
    return Container(
        padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
        decoration: BoxDecoration(
            border: Border(
                bottom: BorderSide(color: Theme.of(context).dividerColor))),
        child: Column(
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Container(
                child: Text(
              "${content.subject}",
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
              textAlign: TextAlign.left,
              style: TextStyle(
                fontSize: 18,
                color: Colors.black,
              ),
            )),
            Container(
                padding: EdgeInsets.only(top: 0, bottom: 10),
                alignment: Alignment.centerRight,
                child: Text(
                  "[${S1Fid.getS1FidName(fid)}]",
                  textAlign: TextAlign.right,
                  style: TextStyle(fontSize: 12, color: Colors.blue),
                )),
            Row(
              children: <Widget>[
                Expanded(
                    flex: 0,
                    child: Padding(
                        padding: EdgeInsets.fromLTRB(0, 0, 6, 0),
                        child: Text(
                          "${content.lastposter}",
                          style: TextStyle(
                            fontSize: 12,
                            color: Colors.black,
                          ),
                        ))),
                Expanded(flex: 1, child: Nothing()),
                Expanded(
                    flex: 0,
                    child: Padding(
                        padding: EdgeInsets.fromLTRB(6, 0, 0, 0),
                        child: Text(
                          "回复：${content.replies}",
                          style: TextStyle(
                            fontSize: 12,
                            color: Colors.black,
                          ),
                        ))),
                Expanded(
                    flex: 0,
                    child: Padding(
                      padding: EdgeInsets.fromLTRB(6, 0, 0, 0),
                      child: Text(
                        "点击：${content.views}",
                        style: TextStyle(
                          fontSize: 12,
                          color: Colors.black,
                        ),
                      ),
                    ))
              ],
            ),
          ],
        ));
  }
}
