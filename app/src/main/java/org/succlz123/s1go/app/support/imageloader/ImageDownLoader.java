package org.succlz123.s1go.app.support.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.dao.api.CreateFile;
import org.succlz123.s1go.app.dao.api.MyOkHttp;
import org.succlz123.s1go.app.dao.database.S1ImageCacheDB;
import org.succlz123.s1go.app.support.AppSize;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fashi on 2015/5/11.
 */
public class ImageDownLoader {
    /**
     * 图片lrucache缓存
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 查询数据库的请求的线程队列
     */
    private HashMap<String, DataBaseRunnable> mDataBaseTaskQueue;
    /**
     * 访问网路下载的请求的线程队列
     */
    private HashMap<String, NetWorkRunnable> mNetWorkTaskQueue;
    /**
     * 查询数据库线程池
     */
    private ExecutorService mDataBaseThreadPoll;
    /**
     * 访问网路下载线程池
     */
    private ExecutorService mNetworkThreadPoll;
    /**
     * 线程池里线程的最大数量
     */
    private final static int THREADPOLL_NUM = 5;
    /**
     * 缓存文件夹路径
     */
    private File mCacherFileDir;
    /**
     * 缓存文件夹名
     */
    private static final String DIR_CACHE = "picture_cache";
    /**
     * 单例模式
     */
    private static ImageDownLoader instance;

    public synchronized static ImageDownLoader getInstance() {
        if (instance == null) {
            instance = new ImageDownLoader();
        }
        return instance;
    }

    private ImageDownLoader() {
        //获取系统分配给每个应用程序的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //给lrucache分配程序能获得的最大内存的1/8
        mLruCache = new LruCache<String, Bitmap>(maxMemory / 8);
        //创建线程队列
        mDataBaseTaskQueue = new HashMap<String, DataBaseRunnable>();
        mNetWorkTaskQueue = new HashMap<String, NetWorkRunnable>();
        //创建线程池
        mDataBaseThreadPoll = Executors.newFixedThreadPool(ImageDownLoader.THREADPOLL_NUM);
        mNetworkThreadPoll = Executors.newFixedThreadPool(ImageDownLoader.THREADPOLL_NUM);
        //创建缓存文件夹 PICTURE_CACHE
        mCacherFileDir = CreateFile.createFileDir(DIR_CACHE);
    }

    /**
     * 如果发现内存缓存中没有图片
     * 把bitmap放进缓存中
     */
    private void addLruCache(String url, AppSize appSize, Bitmap bitmap) {
        String key = url + "_" + appSize.getWidth() + "x" + appSize.getHeight();
        if (getBitmapFromMemCache(url, appSize) == null && bitmap != null) {
            mLruCache.put(key, bitmap);
        }
    }

    /**
     * 查询缓存中bitmap的bitmap
     * 没有返回null
     */
    private Bitmap getBitmapFromMemCache(String url, AppSize appSize) {
        String key = url + "_" + appSize.getWidth() + "x" + appSize.getHeight();
        return mLruCache.get(key);
    }

    /**
     * 查询数据库里bitmap的本地file缓存地址
     * 没有就返回null
     */
    private Bitmap getBitmapFromDataBase(String url, AppSize appSize) {
        String localCachePath = S1ImageCacheDB.getInstance().getCache(url);
        if (localCachePath != null) {
            if (!TextUtils.isEmpty(localCachePath)) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(localCachePath, options);
                options.inSampleSize = bitmapSampleSize(options, appSize);
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(localCachePath, options);
                if (bitmap != null) {
                    Log.e("localCachePath", localCachePath);
                } else {
                    Log.e("localCachePath error", localCachePath);
                }
                return bitmap;
            } else {
                Log.e("localCachePath is null", "图片从数据里读取的file地址为空");
            }
        }
        return null;
    }

    /**
     * 内存缓存和本地数据库缓存都没有 去网络获取
     * 用okhttp去网络下载图片 获得inputstream输入流
     * 把inputstream保存到本地PICTURE_CACHE文件夹
     * 把本地的inputstream的路径和文件大小信息写入数据库
     * 如果获取的inputstream
     * 不为空 返回 由本地的FileOutputStream转换成的bitmap
     * 为空 返回 null
     */
    private Bitmap getBitmapFromNetWork(String url) {
        InputStream is = MyOkHttp.getInstance().doImageGet(url);
        FileOutputStream os = null;
        if (is != null) {
            String caCheFileName = String.valueOf(url.hashCode()) + "_" + System.nanoTime() + ".jpg";
            String caCheFilePathName = mCacherFileDir.getAbsolutePath() + File.separator + caCheFileName;
            try {
                boolean iscreatecaCheFilePath = new File(caCheFilePathName).createNewFile();
                if (iscreatecaCheFilePath) {
                    os = new FileOutputStream(caCheFilePathName);
                    byte[] bt = new byte[2048];
                    int byteread;
                    while ((byteread = is.read(bt)) != -1) {
                        os.write(bt, 0, byteread);
                    }
                    os.flush();
                } else {
                    Log.e("createcaCheFilePath", "is not create");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            S1ImageCacheDB.getInstance().insertCache(url, new File(caCheFilePathName));
            return BitmapFactory.decodeFile(caCheFilePathName);
        }
        return null;
    }

    private class DataBaseRunnable implements Runnable {
        private String mUrl;
        private AppSize mAppSize;
        private ArrayList<CallBack> mDataBaseCallBackList = new ArrayList<>();

        public DataBaseRunnable(String url, AppSize appSize, CallBack callBack) {
            this.mUrl = url;
            this.mAppSize = appSize;
            mDataBaseCallBackList.add(callBack);
            Context context;
        }

        public DataBaseRunnable(String url, AppSize appSize) {
            this.mUrl = url;
            this.mAppSize = appSize;
        }

        private void addCallback(CallBack callBack) {
            mDataBaseCallBackList.add(callBack);
        }

        @Override
        public void run() {
            final Bitmap localCacheBitmap = getBitmapFromDataBase(mUrl, mAppSize);
            S1GoApplication.getInstance().runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (localCacheBitmap != null) {
                        addLruCache(mUrl, mAppSize, localCacheBitmap);
                    }
                    for (CallBack callBack : mDataBaseCallBackList) {
                        if (localCacheBitmap != null) {
                            callBack.onLoad(mUrl, localCacheBitmap);
                        } else {
                            NetWorkRunnable mNetWorkTask = mNetWorkTaskQueue.get(mUrl);
                            Log.e("ImageLoader", "networktask for " + mUrl + " result is " + (mNetWorkTask != null ? "have" : "not have"));
                            if (mNetWorkTask == null) {
                                Log.e("download", mUrl + " callback count " + mDataBaseCallBackList.size());
                                mNetWorkTask = new NetWorkRunnable(mUrl, mAppSize, callBack);
                                mNetworkThreadPoll.submit(mNetWorkTask);
                                mNetWorkTaskQueue.put(mUrl, mNetWorkTask);
                            } else {
                                mNetWorkTask.addCallback(callBack);
                            }
                        }
                    }
                    mDataBaseTaskQueue.remove(mUrl);
                }
            });
        }
    }

    private class NetWorkRunnable implements Runnable {
        private String mUrl;
        private AppSize mAppSize;
        private ArrayList<CallBack> mNetWorkCallBackList = new ArrayList<>();

        public NetWorkRunnable(String url, AppSize appSize, CallBack callBack) {
            this.mUrl = url;
            this.mAppSize = appSize;
            this.addCallback(callBack);
        }

        public void addCallback(CallBack callBack) {
            this.mNetWorkCallBackList.add(callBack);
        }

        @Override
        public void run() {
            Bitmap netWorkBitmap = null;
            //下载失败在重新下载次数为 2
            for (int i = 0; i < 2; i++) {
                netWorkBitmap = getBitmapFromNetWork(mUrl);
                if (netWorkBitmap != null) {
                    break;
                }
            }
            final Bitmap finalNetWorkBitmap = netWorkBitmap;
            S1GoApplication.getInstance().runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    DataBaseRunnable dataBaseTask = mDataBaseTaskQueue.get(mUrl);
                    if (dataBaseTask == null) {
                        dataBaseTask = new DataBaseRunnable(mUrl, mAppSize);
                        mDataBaseThreadPoll.submit(dataBaseTask);
                        mDataBaseTaskQueue.put(mUrl, dataBaseTask);
                    } else {
                        for (CallBack callBack : mNetWorkCallBackList) {
                            dataBaseTask.addCallback(callBack);
                        }
                    }
                    mNetWorkTaskQueue.remove(mUrl);
                    Log.e("ImageLoader", "remove network runnable url=" + mUrl);
                }
            });
        }
    }

    public static interface CallBack {
        void onLoad(String url, Bitmap bitmap);

        void onError(String url);
    }

    public void loadBitmap(String url, AppSize appSize, CallBack callBack) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalArgumentException("LoadBitmap must call on ui thread");
        }
        Bitmap mLocalCacheBitmap = getBitmapFromMemCache(url, appSize);
        if (mLocalCacheBitmap != null) {
            callBack.onLoad(url, mLocalCacheBitmap);
        } else {
            DataBaseRunnable dataBaseTask = mDataBaseTaskQueue.get(url);
            if (dataBaseTask == null) {
                dataBaseTask = new DataBaseRunnable(url, appSize, callBack);
                mDataBaseThreadPoll.submit(dataBaseTask);
                mDataBaseTaskQueue.put(url, dataBaseTask);
            } else {
                dataBaseTask.addCallback(callBack);
            }
        }
    }

    /*对bitmap进行裁剪*/
    private static int bitmapSampleSize(BitmapFactory.Options options, AppSize appSize) {
        int scale = 1;
        int heightRatio = (int) Math.ceil(options.outHeight
                / appSize.getHeight());
        int widthRatio = (int) Math.ceil(options.outWidth
                / appSize.getWidth());
        if (heightRatio > scale && widthRatio > scale) {
            int sampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
            return sampleSize;
        }
        return scale;
    }
}
