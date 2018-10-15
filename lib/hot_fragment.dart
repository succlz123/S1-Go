import 'dart:async';
import 'dart:core';

import 'package:flutter/material.dart';
import 'package:s1_go/hot_thread_bloc.dart';
import 'package:s1_go/utils/s1_fid.dart';
import 'package:s1_go/widget/nothing.dart';

class HotFragment extends StatefulWidget {
  List<HotThreadData> datas;

  HotFragment();

  @override
  _HotFragmentState createState() => new _HotFragmentState();
}

class _HotFragmentState extends State<HotFragment>
    with AutomaticKeepAliveClientMixin {
  List widgets = [];
  final HotThreadBloc _bloc = new HotThreadBloc();
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    print(888);
    _bloc.getHotThread().then((list) {
      if (!mounted) {
        return;
      }
      setState(() {
        _isLoading = false;
      });
    });
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
              return Nothing();
            }
            return new ListView.builder(
                itemCount: snapshot.data?.length,
                itemBuilder: (BuildContext context, int position) {
                  return getRow(snapshot.data, position);
                });
          }),
    );
//
//        body:
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
