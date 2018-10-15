package org.succlz123.s1go.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.text.TextUtils;

/**
 * Created by succlz123 on 3/1/16.
 */
public class NetworkReceiver extends BroadcastReceiver {
    private NetworkManager mNetworkManager;

    public NetworkReceiver(NetworkManager networkManager) {
        mNetworkManager = networkManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (mNetworkManager != null) {
//                mNetworkManager.refresh();
            }
        }
    }

    public static IntentFilter createFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        return filter;
    }
}
