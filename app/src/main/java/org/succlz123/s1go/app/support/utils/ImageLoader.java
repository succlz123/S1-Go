package org.succlz123.s1go.app.support.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.text.TextUtils;
import android.util.LruCache;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.support.biz.MyOkHttp;
import org.succlz123.s1go.app.support.db.ImageCacheDB;
import org.succlz123.s1go.app.support.io.CreateFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fashi on 2015/5/11.
 */
public class ImageLoader {
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
	private static ImageLoader instance;

	public synchronized static ImageLoader getInstance() {
		if (instance == null) {
			instance = new ImageLoader();
		}
		return instance;
	}

	private HashMap<String, Integer> mFaildUrl;

	/**
	 * 在构造方法里初始化配置
	 */
	private ImageLoader() {
		//获取系统分配给每个应用程序的最大内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		//给lrucache分配程序能获得的最大内存的1/8
		mLruCache = new LruCache<String, Bitmap>(maxMemory / 8);
		//创建线程队列
		mDataBaseTaskQueue = new HashMap<String, DataBaseRunnable>();
		mNetWorkTaskQueue = new HashMap<String, NetWorkRunnable>();
		//创建线程池
		mDataBaseThreadPoll = Executors.newFixedThreadPool(ImageLoader.THREADPOLL_NUM);
		mNetworkThreadPoll = Executors.newFixedThreadPool(ImageLoader.THREADPOLL_NUM);
		//创建缓存文件夹 PICTURE_CACHE
		mCacherFileDir = CreateFile.createFileDir(DIR_CACHE);
		//无法访问的Url集合
		mFaildUrl = new HashMap<>();
	}

	/**
	 * 开始加载图片
	 */
	public void loadBitmap(String url, AppSize appSize, CallBack callBack) {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new IllegalArgumentException("LoadBitmap must call on ui thread");
		}

		Bitmap lruBitmap = getBitmapFromMemCache(url, appSize);
		if (lruBitmap != null) {
			callBack.onLoad(url, lruBitmap);
		} else {
			DataBaseRunnable dbTask = mDataBaseTaskQueue.get(url);
			if (dbTask == null) {
				dbTask = new DataBaseRunnable(url, appSize, callBack);
				mDataBaseThreadPoll.submit(dbTask);
				mDataBaseTaskQueue.put(url, dbTask);
			} else {
				dbTask.addCallback(callBack);
			}
		}
	}

	/**
	 * 如果发现lrucache中没有bitmap
	 * 把bitmap放进缓存中
	 */
	private void addLruCache(String url, AppSize appSize, Bitmap bitmap) {
		String key = url + "_" + appSize.getWidth() + "x" + appSize.getHeight();
		if (getBitmapFromMemCache(url, appSize) == null && bitmap != null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * 查询lrucache中的bitmap
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
 		String localCachePath = ImageCacheDB.getInstance().execSelect(url);
		if (!TextUtils.isEmpty(localCachePath)) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(localCachePath, options);
			options.inSampleSize = bitmapSampleSize(options, appSize);
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(localCachePath, options);
			return bitmap;
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
	private boolean getBitmapFromNetWork(String url) {
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
					LogUtil.e("createcaCheFilePath", "is not create");
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
			ImageCacheDB.getInstance().execInsert(url, new File(caCheFilePathName));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(caCheFilePathName, options);
			return options.outWidth > 0 && options.outHeight > 0;
		}
		return false;
	}

	private class DataBaseRunnable implements Runnable {
		private String mUrl;
		private AppSize mAppSize;
		private ArrayList<CallBack> mDataBaseCallBackList = new ArrayList<>();

		public DataBaseRunnable(String url, AppSize appSize, CallBack callBack) {
			this.mUrl = url;
			this.mAppSize = appSize;
			mDataBaseCallBackList.add(callBack);
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

			MyApplication.getInstance().runOnUIThread(new Runnable() {
				@Override
				public void run() {
					if (localCacheBitmap != null && getBitmapFromMemCache(mUrl, mAppSize) == null) {
						addLruCache(mUrl, mAppSize, localCacheBitmap);
					}
					for (CallBack callBack : mDataBaseCallBackList) {
						if (localCacheBitmap != null) {
							callBack.onLoad(mUrl, localCacheBitmap);
						} else {
							NetWorkRunnable nwTask = mNetWorkTaskQueue.get(mUrl);
							if (nwTask == null) {
								nwTask = new NetWorkRunnable(mUrl, mAppSize, callBack);
								mNetworkThreadPoll.submit(nwTask);
								mNetWorkTaskQueue.put(mUrl, nwTask);
							} else {
								nwTask.addCallback(callBack);
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
			boolean success = false;
			//下载失败在重新下载次数为 2
			Integer num = mFaildUrl.get(mUrl);
			if (num == null || num != 1) {
				for (int i = 0; i < 2; i++) {
					success = getBitmapFromNetWork(mUrl);
					if (success) {
						break;
					} else if (!success) {
						if (mUrl.startsWith("http://bbs.saraba1st.com/2b/uc_server/data/avatar/")) {
							mFaildUrl.put(mUrl, i);
						}
					}
				}
			}

			final boolean finalNetWorkBitmap = success;
			MyApplication.getInstance().runOnUIThread(new Runnable() {
				@Override
				public void run() {
					if(finalNetWorkBitmap) {
						DataBaseRunnable dbTask = mDataBaseTaskQueue.get(mUrl);
						if (dbTask == null) {
							dbTask = new DataBaseRunnable(mUrl, mAppSize);
							mDataBaseThreadPoll.submit(dbTask);
							mDataBaseTaskQueue.put(mUrl, dbTask);
						}
						for (CallBack callBack : mNetWorkCallBackList) {
							dbTask.addCallback(callBack);
						}
					}else{
						for (CallBack callBack : mNetWorkCallBackList) {
							callBack.onError(mUrl);
						}
					}
					mNetWorkTaskQueue.remove(mUrl);
				}
			});
		}
	}

	public interface CallBack {
		void onLoad(String url, Bitmap bitmap);

		void onError(String url);
	}

	/*对bitmap进行裁剪*/
	private static int bitmapSampleSize(BitmapFactory.Options options, AppSize appSize) {
		int scale = 1;
		float heightRatio = (float) Math.ceil(options.outHeight
				/ (float) appSize.getHeight());
		float widthRatio = (float) Math.ceil(options.outWidth
				/ (float) appSize.getWidth());
		if (heightRatio > scale || widthRatio > scale) {
			float sampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
			return (int) sampleSize;
		} else {
			return scale;
		}
	}
}
