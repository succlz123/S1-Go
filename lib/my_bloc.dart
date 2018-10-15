import 'dart:async';
import 'dart:convert';
import 'dart:io';

import "package:rxdart/rxdart.dart";

class HotThreadBloc {
  static const String base_url = "http://bbs.saraba1st.com/2b/api/mobile/";

  var _hotThread = BehaviorSubject<List<HotThreadData>>();

  Stream<List<HotThreadData>> get hotThreadResult => _hotThread.stream;

  Future<Null> getHotThread() async {
    var url =
        base_url + "index.php?mobile=no&module=hotthread&orderby=dateline";
    var httpClient = HttpClient();
    List result;
    try {
      httpClient.connectionTimeout = const Duration(seconds: 10);
      var request = await httpClient.getUrl(Uri.parse(url));
      var response = await request.close();
      if (response.statusCode == HttpStatus.ok) {
        var out = await response.transform(utf8.decoder).join();
        var json = jsonDecode(out);
        var jsonObj = json['Variables'];
        var data = jsonObj['data'];
        if (data is List) {
          result = List<HotThreadData>();
          data.forEach((f) {
            var threadData = HotThreadData();
            threadData.subject = f["subject"];
            threadData.lastposter = f["lastposter"];
            threadData.replies = f["replies"];
            threadData.views = f["views"];
            threadData.fid = f["fid"];
            result.add(threadData);
          });
        }
      }
    } catch (e) {
      _hotThread.addError(e);
    }
    _hotThread.add(result);
  }

  void dispose() {
    _hotThread.close();
  }
}

class HotThreadData {
  String tid;
  String fid;
  String posttableid;
  String typeid;
  String sortid;
  String readperm;
  String price;
  String author;
  String authorid;
  String subject;
  String dateline;
  String lastpost;
  String lastposter;
  String views;
  String replies;
  String displayorder;
  String highlight;
  String digest;
  String rate;
  String special;
  String attachment;
  String moderated;
  String closed;
  String stickreply;
  String recommends;
  String recommendAdd;
  String recommendSub;
  String heats;
  String status;
  String isgroup;
  String favtimes;
  String sharetimes;
  String stamp;
  String icon;
  String pushedaid;
  String cover;
  String replycredit;
  String relatebytag;
  String maxposition;
  String bgcolor;
  String comments;
  String lastposterenc;
  String multipage;
  String recommendicon;

  String newX;
  String heatlevel;
  String moved;
  String icontid;
  String folder;
  String weeknew;
  String istoday;
  String dbdateline;
  String dblastpost;
  String id;
  String rushreply;
}
