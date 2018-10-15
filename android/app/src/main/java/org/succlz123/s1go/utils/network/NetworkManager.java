package org.succlz123.s1go.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by succlz123 on 3/1/16.
 */
public class NetworkManager {
    // 无网络
    public final static short NETWORK_TYPE_UNCONNECTED = -1;
    // 未知网络
    public final static short NETWORK_TYPE_UNKNOWN = 0;
    // WIFI网络
    public final static short NETWORK_TYPE_WIFI = 1;
    // 2G网络
    public final static short NETWORK_TYPE_2G = 2;
    // 3G网络
    public final static short NETWORK_TYPE_3G = 3;
    // 4G网络
    public final static short NETWORK_TYPE_4G = 4;

    // 未知运营商
    public final static int MOBILE_UNKNOWN = 0;
    // 中国电信
    public final static int MOBILE_TELCOM = 3;
    // 中国联通
    public final static int MOBILE_UNICOM = 5;
    // 中国移动
    public final static int MOBILE_CHINAMOBILE = 4;

    private Context mApplicationContext;

    // 当前网络类型
    public int networkType;
    // 当前网络类型名字
    public String networkTypeStr;
    // 当前SP类型
    public int spType;
    // 当前SP类型名字
    public String spTypeStr;
    // IP地址
    public String ipAddress;
    // MAC地址
    public String macAddress;
    // 公网IP
    public String networkIpAddress;

    public NetworkManager(Context context) {
        if (context != null) {
            mApplicationContext = context.getApplicationContext();
        }
//        refresh();
    }

    public void refresh() {
        networkType = getNetworkType(mApplicationContext);
        switch (networkType) {
            case NETWORK_TYPE_UNCONNECTED:
            case NETWORK_TYPE_UNKNOWN:
                break;
            case NETWORK_TYPE_WIFI:
                ipAddress = getLocalIpAddress();
                macAddress = getRouteMac(mApplicationContext);
                spType = 0;
                spTypeStr = getWifiSSID(mApplicationContext);
                break;
            case NETWORK_TYPE_2G:
            case NETWORK_TYPE_3G:
            case NETWORK_TYPE_4G:
                spType = getSP(mApplicationContext);
                spTypeStr = sp2Str(spType);
                break;
        }
        networkTypeStr = networkType2Str(networkType);
    }

    public static boolean isNetWorkUp(Context context) {
        int networkType = getNetworkType(context);
        return !(networkType == NETWORK_TYPE_UNCONNECTED || networkType == MOBILE_UNKNOWN);
    }

    /**
     * @return -1为网络不可用 0为未知网络 1为WIFI 2为2G 3为3G 4为4G
     */
    public static int getNetworkType(Context context) {
        if (context == null) {
            return NETWORK_TYPE_UNCONNECTED;
        }

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return NETWORK_TYPE_UNKNOWN;
            }

            NetworkInfo activeNetInfo = connectivity.getActiveNetworkInfo();

            if (activeNetInfo == null) {
                return NETWORK_TYPE_UNCONNECTED;
            }
            if (!activeNetInfo.isAvailable() || !activeNetInfo.isConnected()) {
                return NETWORK_TYPE_UNCONNECTED;
            }

            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_TYPE_WIFI;
            } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (activeNetInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_CDMA:// ~ 14-64 kbps
                    case TelephonyManager.NETWORK_TYPE_IDEN:// ~25 kbps
                    case TelephonyManager.NETWORK_TYPE_1xRTT:// ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_EDGE:// ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_GPRS:// ~ 100 kbps
                        return NETWORK_TYPE_2G;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:// ~ 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:// ~ 400-7000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:// ~ 600-1400 kbps
                    case TelephonyManager.NETWORK_TYPE_HSPA:// ~ 700-1700 kbps
                    case TelephonyManager.NETWORK_TYPE_HSUPA:// ~ 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSDPA:// ~ 2-14 Mbps
                    case 15: // 对应TelephonyManager.NETWORK_TYPE_HSPAP: 在api level 13下没有此值，但存在此网络类型，下面直接用数值代替
                        return NETWORK_TYPE_3G;
                    case 13: // 对应TelephonyManager.NETWORK_TYPE_LTE
                        return NETWORK_TYPE_4G;
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        return NETWORK_TYPE_UNKNOWN;
                }
            } else {
                return NETWORK_TYPE_UNKNOWN;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NETWORK_TYPE_UNCONNECTED;
    }

    public static String getLocalIpAddress() {
        try {
            ArrayList<NetworkInterface> interList = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : interList) {
                ArrayList<InetAddress> iaList = Collections.list(ni.getInetAddresses());
                for (InetAddress address : iaList) {
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    public String getRouteMac(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo == null) {
                return "0";
            }
            return wifiInfo.getBSSID();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public String getSpId() {
        if (networkType == NETWORK_TYPE_WIFI) {
            return spTypeStr;
        }
        return String.valueOf(spType);
    }

    public static int getSP(Context context) {
        int code = MOBILE_UNKNOWN;

        if (context == null) {
            return code;
        }

        try {
            TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String operator = telManager.getSimOperator();

            if (operator != null) {
                if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007") || operator.equals("46020")) {
                    /** 中国移动 */
                    code = MOBILE_CHINAMOBILE;
                } else if (operator.equals("46001") || operator.equals("46006")) {
                    /** 中国联通 */
                    code = MOBILE_UNICOM;
                } else if (operator.equals("46003") || operator.equals("46005")) {
                    /** 中国电信 */
                    code = MOBILE_TELCOM;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String getWifiSSID(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();
            return info != null ? info.getSSID() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String networkType2Str(int type) {
        String string = "";
        switch (type) {
            case NETWORK_TYPE_UNCONNECTED:
                string = "无网络";
                break;
            case NETWORK_TYPE_UNKNOWN:
                string = "未知网络";
                break;
            case NETWORK_TYPE_WIFI:
                string = "WIFI网络";
                break;
            case NETWORK_TYPE_2G:
                string = "2G网络";
                break;
            case NETWORK_TYPE_3G:
                string = "3G网络";
                break;
            case NETWORK_TYPE_4G:
                string = "4G网络";
                break;
        }
        return string;
    }

    public static String sp2Str(int type) {
        String string = "";
        switch (type) {
            case MOBILE_UNKNOWN:
                string = "未知运营商";
                break;
            case MOBILE_TELCOM:
                string = "中国电信";
                break;
            case MOBILE_UNICOM:
                string = "中国联通";
                break;
            case MOBILE_CHINAMOBILE:
                string = "中国移动 ";
                break;
        }
        return string;
    }

    @Override
    public String toString() {
        String str = "";
        str += "当前网络类型ID:" + networkType + "\n";
        str += "当前网络类型名字:" + networkTypeStr + "\n\n";
        str += "当前服务商类型ID:" + spType + "\n";
        str += "当前服务商类型名字:" + spTypeStr + "\n\n";
        str += "内网IP:" + ipAddress + "\n";
        str += "公网IP:" + networkIpAddress + "\n";
        str += "当前MAC:" + macAddress + "\n\n";
        return str;
    }
}
