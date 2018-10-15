package org.succlz123.blockanalyzer;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class BlockWriter {

    String write(String blockTime, String log) {
        File dir = new File(getPath());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, blockTime + ".log");
        FileWriter writer = null;

        try {
            writer = new FileWriter(file);
            writer.write(log);
            writer.flush();
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }

        }

        return file.getAbsolutePath();
    }

    private static String getPath() {
        return "mounted".equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().canWrite() ? Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "block" : Environment.getDataDirectory().getAbsolutePath() + File.separator + "block";
    }
}
