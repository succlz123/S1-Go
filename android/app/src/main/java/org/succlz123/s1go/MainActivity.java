package org.succlz123.s1go;

import org.succlz123.s1go.config.RetrofitManager;
import org.succlz123.s1go.ui.thread.list.ThreadListActivity;
import org.succlz123.s1go.utils.s1.S1Fid;

import android.os.Bundle;
import android.util.Log;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

/**
 * @author succlz123
 */
public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        String CHANNEL = "org.succlz123.s1go/tid-name";
        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler((call, result) -> {
//            Log.d("cc", call.method);
//            Log.d("cc", call.argument("tid"));
            switch (call.method) {
                case "getTidName":
                    result.success(S1Fid.getS1FidName(call.argument("tid")));
                    break;
                case "go2AreaActivity":
                    ThreadListActivity.start(MainActivity.this, call.argument("tid"));
                    break;
                case "":
                    RetrofitManager.apiService().getHot();
                    break;
                default:
                    result.notImplemented();
                    break;
            }
        });
    }
}