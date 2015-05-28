package org.succlz123.s1go.app.support.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.text.TextUtils;
import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.dao.db.ImageCacheDB;
import org.succlz123.s1go.app.support.utils.AppSize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fashi on 2015/4/22.
 */
public class AppImageLoader {

    public static interface Callback {
        public void onResult(String url, Bitmap bitmap);

        public void onError(String url, Exception exception);
    }

    private static AppImageLoader instance;

    public synchronized static AppImageLoader getInstance() {
        if (instance == null) {
            instance = new AppImageLoader();
        }
        return instance;
    }

    private ExecutorService loader;
    private HashMap<String, JobRunnable> loaderTasks = new HashMap<String, JobRunnable>();

    private AppImageLoader() {
        this.loader = Executors.newFixedThreadPool(5);
    }

    public void loadBitmap(final String url, AppSize size, final Callback callback) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalArgumentException("LoadBitmap must call on ui thread");
        }
        JobRunnable task = this.loaderTasks.get(url);
        if (task == null) {
            task = new JobRunnable(url, callback, size);
            this.loader.submit(task);
            this.loaderTasks.put(url, task);
        } else {
            task.addCallback(callback);
        }
    }

    private Bitmap readLocalCache(String url, AppSize size) {
        String localCachePath = ImageCacheDB.getInstance().execSelect(url);
        if (!TextUtils.isEmpty(localCachePath)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(localCachePath, options);
            if (options.outWidth > 0 && options.outHeight > 0) {
//                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = calculateInSampleSize(options, size.getWidth(), size.getHeight());
                options.inJustDecodeBounds = false;
                return BitmapFactory.decodeFile(localCachePath, options);
            }
        }
        return null;
    }

    private boolean canReadFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            if (options.outWidth > 0 && options.outHeight > 0) {
                return true;
            }
        }
        return false;
    }

    private Bitmap readNetworkt(String url) {
//        Log.e("AppImageLoader", "download url=" + url);
//        File tmpFileDir = CreateFile.createDirOnSDCard("piciture_tmp");
//        File cacheFileDir = CreateFile.createDirOnSDCard("picture_cache");
//        String tmpFileName = url.hashCode() + "_" + System.nanoTime() + ".jpg";
//        String tmpFile = tmpFileDir.getAbsolutePath() + File.separator + tmpFileName;
//
//        try {
//            new File(tmpFile).createNewFile();
//            MyOkHttp.getInstance().doImageGet(url, tmpFile);
//            if (canReadFile(tmpFile)) {
//                String cacheFile = cacheFileDir.getAbsolutePath() + File.separator + tmpFileName;
//                FileInputStream is = new FileInputStream(tmpFile);
//                FileOutputStream os = new FileOutputStream(cacheFile);
//                byte[] bt = new byte[2048];
//                int byteread;
//                while ((byteread = is.read(bt)) != -1) {
//                    os.write(bt, 0, byteread);
//                }
//                os.flush();
//                is.close();
//                os.close();
//                ImageCacheDB.getInstance().execInsert(url, new File(cacheFile));
//                return BitmapFactory.decodeFile(cacheFile);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (new File(tmpFile).exists()) {
//                new File(tmpFile).getAbsoluteFile().delete();
//            }
//        }

        return null;
    }

    private class JobRunnable implements Runnable {

        private String url;
        private AppSize size;
        private ArrayList<Callback> callbackList = new ArrayList<Callback>();

        public JobRunnable(String url, Callback callback, AppSize size) {
            this.url = url;
            this.size = size;
            this.callbackList.add(callback);
        }

        public void addCallback(Callback callback) {
            this.callbackList.add(callback);
        }

        @Override
        public void run() {
            Bitmap localCache = readLocalCache(url, size);
            if (localCache == null) {
//                localCache = readNetwork(url);
            }

            final Bitmap finalLocalCache = localCache;
            MyApplication.getInstance().runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    for (Callback callback : callbackList) {
                        if (finalLocalCache != null) {
                            callback.onResult(url, finalLocalCache);
                        } else {
                            callback.onError(url, null);
                        }
                    }
                    loaderTasks.remove(url);
                }
            });
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原始图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
