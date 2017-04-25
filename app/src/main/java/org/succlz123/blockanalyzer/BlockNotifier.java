package org.succlz123.blockanalyzer;

import org.succlz123.s1go.app.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class BlockNotifier {

    public void notify(Context context, Block block, String path) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        String msg = "Block happen at " + block.getBlockTime();
        NotificationCompat.Builder builder = (new NotificationCompat.Builder(context))
                .setTicker(msg)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(msg)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_user_img)
                .setDefaults(3);
//                .setContentIntent(getPendingIntent(context, path));
        notificationManager.notify(block.getBlockTime().hashCode(), builder.build());
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    private static PendingIntent getPendingIntent(Context context, String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(path)), "text/plain");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
