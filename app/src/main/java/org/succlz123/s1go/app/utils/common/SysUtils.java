package org.succlz123.s1go.app.utils.common;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by succlz123 on 2015/7/6.
 */
public class SysUtils {

    /**
     * 是否有SDCard
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();

        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 通过activity得到屏幕信息
     */
    public static DisplayMetrics getScreenDisplayMetrics(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);

        return metric;
    }

    /**
     * 通过WINDOW_SERVICE获取display对象,然后获得高宽
     */
    public static String getScreenWxH(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        return width + "x" + height;
    }


    /**
     * 获取版本号和版本次数
     */
    public static String getVersionCode(Context context, int type) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            if (type == 1) {
                return String.valueOf(pi.versionCode);
            } else {
                return pi.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Activity高度
     */
    public static int getActivityHeight(Activity context) {
        if (context == null) {
            return 0;
        }
        return context.findViewById(android.R.id.content).getHeight();
    }

    /**
     * Activity宽度
     */
    public static int getActivityWidth(Activity context) {
        if (context == null) {
            return 0;
        }
        return context.findViewById(android.R.id.content).getWidth();
    }

    /**
     * ActionBar高度
     */
    public static int getActionBarSize(Activity context) {
        if (context == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();

        return actionBarSize;
    }

    /**
     * 获取设备信息
     */
    public static String getDeviceInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());

        return sb.toString();
    }

    /**
     * 获取设备android id
     */
    public static String getAndroidId(Context context) {
        String androidId
                = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        return androidId;
    }

    /**
     * 获取系统时间 格式为："yyyy/MM/dd"
     */
    public static String getCurrentDate() {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");

        return sf.format(d);
    }

    /**
     * 时间戳转换成字符窜
     */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        return sf.format(d);
    }

    public static String getDateToStringWithYDHM(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日 HH:mm");

        return sf.format(d);
    }

    /**
     * 将字符串转为时间戳
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 获取layoutInflater
     */
    public static LayoutInflater getLayoutInflater(Context context) {
        LayoutInflater inflater
                = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflater;
    }

    /**
     * 字符串拼接 by StringBuilder
     */
    public static String getStringByStringBuilder(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }

    /**
     * 字符串拼接 by StringBuffer
     */
    public static String getStringByStringBuffer(String... strings) {
        StringBuffer builder = new StringBuffer();
        for (String string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }

    /**
     * 高效拼接url
     */
    public static String getUrl(HashMap<String, String> params) {
        String url = null;
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url = sb.toString();
        }
        return url;
    }

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 检查网络
     */
    public static boolean checkNetState(Context context) {
        boolean netstate = false;
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        netstate = true;
                        break;
                    }
                }
            }
        }
        return netstate;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wiFiNetworkInfo
                    = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wiFiNetworkInfo != null) {
                return wiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断移动网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileNetworkInfo
                    = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetworkInfo != null) {
                return mobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                return networkInfo.getType();
            }
        }
        return -1;
    }

//	public static String getHandSetInfo() {
//		String handSetInfo =
//				"手机型号:" + android.os.Build.MODEL
//						+ ",系统版本:" + android.os.Build.VERSION.RELEASE;
//		return handSetInfo;
//
//	}

    /**
     * Base64 编码
     */
    public static String encodeByBase64(String string) {
        byte[] encode = Base64.encode(string.getBytes(), Base64.DEFAULT);
        String result = new String(encode);

        return result;
    }

    /**
     * Base64 解码
     */
    public static String decodeByBase64(String string) {
        byte[] decode = Base64.decode(string, Base64.DEFAULT);
        String result = new String(decode);

        return result;
    }

    /**
     * inputStream 转成 string
     */
    public static String inputStream2String(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int i; (i = in.read(buf)) != -1; ) {
            baos.write(buf, 0, i);
        }

        return baos.toString("UTF-8");
    }

    /**
     * 滑动改变亮度
     */
    public static float onBrightnessSlide(Activity activity, float change) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lpa = window.getAttributes();

        float brightness = lpa.screenBrightness;

        if (brightness <= 0.00f) {
            brightness = 0.50f;
        } else if (brightness < 0.01f) {
            brightness = 0.01f;
        }

        lpa.screenBrightness = brightness + change;

        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }

        window.setAttributes(lpa);

        return lpa.screenBrightness;
    }


    /**
     * 滑动改变音量
     */
    public static int onVolumeSlide(AudioManager audioManager, int stepVolume, float distanceY, float change) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

//        //无缓冲
//        int index = (int) (change * maxVolume) + currentVolume;
//        if (index > maxVolume) {
//            index = maxVolume;
//        } else if (index < 0) {
//            index = 0;
//        }
//
        //移动5dp以上才改变
        if (distanceY >= stepVolume) {
            if (currentVolume < maxVolume) {
                currentVolume++;
            }
        } else if (distanceY <= -stepVolume) {
            if (currentVolume > 0) {
                currentVolume--;
            }
        }

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);

        return (currentVolume * 100) / maxVolume;
    }

    /**
     * 滑动改变进度
     */
    public static long onProgressSlide(float distanceX, float distanceY, int setupDp, int changeSetup, long position, long duration) {
        // distanceX=lastScrollPositionX-currentScrollPositionX
        // 向左滑,正数
        // 向右滑,负数
        if (Math.abs(distanceX) > Math.abs(distanceY)) {
            // 横向移动大于纵向移动
            if (distanceX >= setupDp) {
                // 防止为负
                if (position > changeSetup) {
                    position -= changeSetup;
                } else {
                    position = 0;
                }
            } else if (distanceX <= -setupDp) {
                // 防止超过总时长
                if (position < duration - changeSetup) {
                    position += changeSetup;
                } else {
                    position = duration - changeSetup;
                }
            }
            if (position < 0) {
                position = 0;
            }
        }
        return position;
    }

    /**
     * 毫秒 转 小时:分钟:秒
     */
    public static String getTimeFromMillisecond(long Millisecond) {
        if (Millisecond <= 0) {
            return "00:00";
        }

        long days = Millisecond / (1000 * 60 * 60 * 24);
        long hours = (Millisecond % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (Millisecond % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (Millisecond % (1000 * 60)) / 1000;

        String hoursStr = hours >= 10 ? String.valueOf(hours) : "0" + String.valueOf(hours);
        String minutesStr = minutes >= 10 ? String.valueOf(minutes) : "0" + String.valueOf(minutes);
        String secondsStr = seconds >= 10 ? String.valueOf(seconds) : "0" + String.valueOf(seconds);

        if (hours > 0) {
            return hoursStr + ":" + minutesStr + ":" + secondsStr;
        }
        return minutesStr + ":" + secondsStr;
    }

    /**
     * 检查activity是否还存在
     */
    public static boolean isActivityLive(Activity activity) {
        return activity != null && !activity.isDestroyed();
    }

    /**
     * 检查fragment是否还存在
     */
    public static boolean isActivityLive(Fragment fragment) {
        return !fragment.isDetached() && isActivityLive(fragment.getActivity());
    }

    /**
     * 获得字体
     */
    public static Typeface getFontRoboto(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    }

    /**
     * 判断屏幕是否是竖屏
     */
    public static boolean isVertical(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (width > height) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查是否存在SD卡
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
            String caCheDir = MainApplication.getInstance().getExternalCacheDir().getAbsolutePath();
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

    public static String code(String code) {
        char[] array = code.toCharArray();//获取字符数组
        for (int i = 0; i < array.length; i++) {//历遍字符数组
            array[i] = (char) (array[i] ^ 10086);//对数组每个元素进行异或运算
        }
        return new String(array);
    }

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 700) {
            ToastUtils.showToastShort(MainApplication.getContext(), "骚年,你点太快了!");
            return true;
        }
        lastClickTime = time;
        return false;
    }
}