package org.succlz123.s1go.app.dao.api;

import android.os.Environment;
import android.util.Log;
import org.succlz123.s1go.app.S1GoApplication;

import java.io.File;

/**
 * Created by fashi on 2015/4/22.
 */
public class CreateFile {
    /**
     * 检查是否存在SD卡
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建图片缓存目录
     */
    public static File createFileDir(String dirName) {
        String filePath = null;
        // 如SD卡已存在，则存储；反之存在data目录下
        if (hasSdcard()) {
            // 通过context来获得SD卡路径
//            filePath = Environment.getExternalStorageDirectory() + File.separator + dirName;
            //通过application来获取环境变量
            String caCheDir = S1GoApplication.getInstance().getExternalCacheDir().getAbsolutePath();
            filePath = caCheDir + File.separator + dirName + File.separator;
        } else {
//            filePath = context.getCacheDir().getPath() + File.separator
//                    + dirName;
        }
        File dirFile = new File(filePath);
        //不存在缓存目录时 新建它
        if (!dirFile.exists()) {
            //可以在不存在的目录中创建文件夹 mkdirs可以创建多级目录
            boolean isCreate = dirFile.mkdirs();
            Log.v("createDirOnSDCard", dirFile.getAbsolutePath());
        }
        return dirFile;
    }
}
