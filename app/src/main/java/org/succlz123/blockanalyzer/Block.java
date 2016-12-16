package org.succlz123.blockanalyzer;

import org.succlz123.utils.ProcessUtils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class Block {
    static final SimpleDateFormat DATE_FORMATTER;
    static final String LINE_DIVIDER = "***************************************\r\n";
    static final String SEPARATOR = "\r\n";
    static final String PERCENT = "% ";
    static final String KV = " = ";
    static final String SLASH = "/";
    private static final String KEY_PROCESSNAME = "processName";
    private static final String KEY_COSTTIME = "costTime";
    private static final String KEY_THREADCOSTTIME = "threadCostTime";
    private static final String KEY_LOOPERLOG = "looperLog";
    private static final String KEY_SYSMEMORY = "sysMemory";
    private static final String KEY_HEAPMEMORY = "heapMemory";
    private static long sSysTotalMemory;
    private long mCostTime;
    private long mThreadCostTime;
    private long mSysFreeMemory;
    private long mHeapFreeMemory;
    private long mHeapTotalMemory;
    private String mBlockTime;
    private String mLooperLog;
    private List<BlockRecord> mBlockRecords;

    public static Block create() {
        return new Block();
    }

    private Block() {
        this.mSysFreeMemory = getFreeMemory(BlockAnalyzer.get().application);
        Runtime runtime = Runtime.getRuntime();
        this.mHeapFreeMemory = runtime.freeMemory();
        this.mHeapTotalMemory = runtime.totalMemory();
    }

    Block setTimes(long blockTime, long costTime, long threadCostTime) {
        this.mBlockTime = DATE_FORMATTER.format(Long.valueOf(blockTime));
        this.mCostTime = costTime;
        this.mThreadCostTime = threadCostTime;
        return this;
    }

    public Block setLooperLog(String looperLog) {
        this.mLooperLog = looperLog;
        return this;
    }

    public Block setBlockRecords(List<BlockRecord> blockRecords) {
        this.mBlockRecords = blockRecords;
        return this;
    }

    public String getBlockTime() {
        return this.mBlockTime;
    }

    private static String obtainProcessName(Context context) {
        return ProcessUtils.myProcName();
    }

    private static long getFreeMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / 1024L;
    }

    public static long getSysTotalMemory() {
        if (sSysTotalMemory == 0L) {
            String str1 = "/proc/meminfo";
            long initial_memory = -1L;
            FileReader localFileReader = null;

            try {
                localFileReader = new FileReader(str1);
                BufferedReader e = new BufferedReader(localFileReader, 8192);
                String str2 = e.readLine();
                if (str2 != null) {
                    String[] arrayOfString = str2.split("\\s+");
                    initial_memory = (long) Integer.valueOf(arrayOfString[1]).intValue();
                }

                e.close();
            } catch (IOException var15) {
                var15.printStackTrace();
            } finally {
                if (localFileReader != null) {
                    try {
                        localFileReader.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }

            }

            sSysTotalMemory = initial_memory;
        }

        return sSysTotalMemory;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("processName").append(" = ").append(obtainProcessName(BlockAnalyzer.get().application)).append("\r\n");
        sb.append("costTime").append(" = ").append(this.mCostTime).append("\r\n");
        sb.append("threadCostTime").append(" = ").append(this.mThreadCostTime).append("\r\n");
        sb.append("sysMemory").append(" = ").append(this.mSysFreeMemory).append("/").append(getSysTotalMemory()).append("\r\n");
        sb.append("heapMemory").append(" = ").append(this.mHeapFreeMemory).append("/").append(this.mHeapTotalMemory).append("\r\n");
        sb.append("looperLog").append(" = ").append(this.mLooperLog).append("\r\n");
        sb.append("\r\n").append("\r\n");
        if (this.mBlockRecords != null && !this.mBlockRecords.isEmpty()) {
            Iterator i$ = this.mBlockRecords.iterator();

            while (i$.hasNext()) {
                BlockRecord record = (BlockRecord) i$.next();
                sb.append(record.dump()).append("\r\n");
            }
        }

        return sb.toString();
    }

    static {
        DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS", Locale.US);
        sSysTotalMemory = 0L;
    }
}
