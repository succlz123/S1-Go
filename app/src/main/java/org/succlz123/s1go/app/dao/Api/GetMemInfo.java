package org.succlz123.s1go.app.dao.api;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import org.succlz123.s1go.app.S1GoApplication;

/**
 * Created by fashi on 2015/4/26.
 */
public class GetMemInfo {

    public static String getMemInfo() {
        Runtime rt = Runtime.getRuntime();
        long vmAlloc = rt.totalMemory() - rt.freeMemory();
        long nativeAlloc = Debug.getNativeHeapAllocatedSize();
        String vmAllocStr = "V" + formatMemoryText(vmAlloc);
//        String nativeAllocStr = "N" + formatMemoryText(nativeAlloc);
        ActivityManager am = (ActivityManager) S1GoApplication.getInstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        String result = "M" + Integer.toString(memoryClass) + "MB";
//        return vmAllocStr + "," + nativeAllocStr + "," + result;
        return vmAllocStr + "/" + result;
    }

    private static String formatMemoryText(long memory) {
        float memoryInMB = memory * 1f / 1024 / 1024;
        return String.format("%.1f MB", memoryInMB);
    }
}
