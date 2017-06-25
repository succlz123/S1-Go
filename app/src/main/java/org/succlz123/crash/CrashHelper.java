package org.succlz123.crash;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.succlz123.s1go.app.MainApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by succlz123 on 2017/6/23.
 */

public class CrashHelper implements Thread.UncaughtExceptionHandler {
    private static CrashHelper sInstance;
    private static Context sApplicationContext;

    public static CrashHelper getInstance(Context context) {
        if (sInstance == null) {
            sApplicationContext = context.getApplicationContext();
            sInstance = new CrashHelper();
        }
        return sInstance;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String logDir = sApplicationContext.getExternalFilesDir("crash").getAbsolutePath();
        File file = new File(logDir);
        boolean mkSuccess;
        if (!file.isDirectory()) {
            mkSuccess = file.mkdirs();
            if (!mkSuccess) {
                mkSuccess = file.mkdirs();
            }
        }
        try {
            FileWriter fw = new FileWriter(logDir + File.separator + "error.log", true);
            fw.write(new Date() + "\n");
            StackTraceElement[] stackTrace = e.getStackTrace();
            fw.write(e.getMessage() + "\n");
            for (int i = 0; i < stackTrace.length; i++) {
                fw.write("file:" + stackTrace[i].getFileName() + " class:" + stackTrace[i].getClassName()
                        + " method:" + stackTrace[i].getMethodName() + " line:" + stackTrace[i].getLineNumber()
                        + "\n");
            }
            fw.write("\n");
            fw.close();
        } catch (IOException e1) {
            Log.e("crash handler", "load file failed...", e1.getCause());
        }
        Toast.makeText(sApplicationContext, e.toString(), Toast.LENGTH_LONG).show();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void init(Context ctx) {
        Thread.setDefaultUncaughtExceptionHandler(CrashHelper.getInstance(ctx));
    }
}
