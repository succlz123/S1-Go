package org.succlz123.utils;

import org.succlz123.s1go.app.utils.common.IOUtils;

import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.VisibleForTesting;
import android.system.Os;
import android.system.OsConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class ProcessUtils {

    /**
     * {@code /proc/self/cmdline}.
     * See <a href="http://man7.org/linux/man-pages/man5/proc.5.html">proc(5)</a>.
     */
    public static String myProcName() {
        return procName(Process.myPid());
    }

    @VisibleForTesting
    static String procNameOld(int pid) {
        StringBuffer cmdline = new StringBuffer();
        FileInputStream is = null;
        try {
            is = new FileInputStream("/proc/" + pid + "/cmdline");
            for (; ; ) {
                int c = is.read();
                if (c < 0)
                    break;
                cmdline.append((char) c);
            }
        } catch (Exception ignored) {
        } finally {
            IOUtils.closeQuietly(is);
        }
        // trim the last '\0'
        return StringUtils.trim(cmdline.toString());
    }

    /**
     * Gets the specified process name with pid.
     * See <a href="http://man7.org/linux/man-pages/man5/proc.5.html">proc(5)</a>.
     * {@code /proc/[pid]/cmdline}
     */
    public static String procName(int pid) {
        return StringUtils.trimToEmpty(FileUtils.string("/proc/" + pid + "/cmdline"));
    }

    /**
     * {@code /proc/[pid]/stat}.
     * See <a href="http://man7.org/linux/man-pages/man5/proc.5.html">proc(5)</a>.
     */
    public static String[] procStat(int pid) {
        return StringUtils.split(FileUtils.string("/proc/" + pid + "/stat"));
    }

    /**
     * {@code /proc/self/stat}.
     * See <a href="http://man7.org/linux/man-pages/man5/proc.5.html">proc(5)</a>.
     */
    public static String[] myProcStat() {
        return procStat(Process.myPid());
    }

    /**
     * {@code ps}
     */
    public static String dumpProcStatus() {
        String[] stat = myProcStat();
        if (stat.length < 27) {
            return "unknown stat string:" + Arrays.toString(stat);
        }
        StringBuilder builder = new StringBuilder(128);
        builder.append("uptime=").append(SystemClock.elapsedRealtime()).append('\t');
        if (Build.VERSION.SDK_INT >= 21) {
            builder.append("CLK_TCK=").append(Os.sysconf(OsConstants._SC_CLK_TCK)).append("HZ\t");
        }
        builder.append("pid=").append(stat[0]).append(',')
                .append("utime=").append(stat[13]).append(',')
                .append("stime=").append(stat[14]).append(',')
                .append("num_threads=").append(stat[19]).append(',')
                .append("starttime=").append(stat[21]);
        return builder.toString();
    }

    /**
     * @param procName Process name. null == 'main process'
     * @see #isMainProcess()
     */
    public static boolean isInProcess(String procName) {
        if (procName == null) {
            return isMainProcess();
        }
        return StringUtils.equals(procName, myProcName());
    }

    /**
     * dump opening file descriptors
     * See <a href="http://man7.org/linux/man-pages/man5/proc.5.html">proc(5)</a>.
     */
    public static String dumpOpeningFds() {
        File[] files = listOpeningFiles();
        if (files.length == 0) {
            return StringUtils.EMPTY;
        }
        final String fd = "/proc/" + Process.myPid() + "/fd/";
        List<String> list = new ArrayList<>(files.length);
        for (File file : files) {
            try {
                String canonicalPath = file.getCanonicalPath();
                int pos = canonicalPath.indexOf(fd);
                // remove 'fd'
                if (pos == 0) {
                    canonicalPath = canonicalPath.substring(fd.length());
                }
                list.add(file.getName() + " -> " + canonicalPath);
            } catch (IOException e) {
                // fall through
                list.add(file.getName() + " -> " + file.getAbsolutePath());
            }
        }
        int size = list.size();
        if (size == 0) return StringUtils.EMPTY;
        return "list " + fd + " :  " + files.length + "\n    " + StringUtils.join(list, "\n    ");
    }

    /**
     * lsof
     */
    public static File[] listOpeningFiles() {
        final String fd = "/proc/" + Process.myPid() + "/fd/";
        File proc = new File(fd);
        if (!proc.isDirectory()) {
            return ArrayUtils.emptyArray(File.class);
        }
        File[] files = proc.listFiles();
        if (files == null) {
            return ArrayUtils.emptyArray(File.class);
        }
        return files;
    }

    /**
     * ps -p {@code self} -t
     * See http://man7.org/linux/man-pages/man1/ps.1.html
     */
    public static Thread[] getAllThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup system = null;
        do {
            system = group;
            group = group.getParent();
        } while (group != null);
        int count = system.activeCount();
        final Thread[] threads = new Thread[count << 1];
        count = system.enumerate(threads);
        return count == threads.length ? threads : ArrayUtils.subarray(threads, 0, count);
    }

    /**
     * cat /proc/sys/fs/file-nr
     *
     * @return Array that contains 3 numbers, -1 means read error
     */
    public static long[] getFileHandlesUsage() {
        String str = StringUtils.trimToEmpty(FileUtils.string("/proc/sys/fs/file-nr"));
        String[] arr = StringUtils.splitByWholeSeparator(str, null);
        if (arr.length < 3) return new long[]{-1, -1, -1};
        long[] handles = new long[3];
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            try {
                handles[i] = Long.parseLong(s);
            } catch (NumberFormatException e) {
                handles[i] = -1;
            }
        }
        return handles;
    }

    /**
     * cat /proc/sys/fs/file-max
     *
     * @return -1 means read error
     */
    public static long getMaxFileHandles() {
        String str = StringUtils.trimToEmpty(FileUtils.string("/proc/sys/fs/file-max"));
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Check current process is 'main process'
     */
    public static boolean isMainProcess() {
        return StringUtils.indexOf(myProcName(), ':') == -1;
    }

    /**
     * kill -9 {@code self}
     */
    public static void suicide() {
        Process.killProcess(Process.myPid());
    }
}
