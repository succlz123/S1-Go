package org.succlz123.blockanalyzer;

import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class CpuTracker extends BlockTracker {
    private static final String SYS_CPU_FILE = "/proc/stat";
    private static final String PID_CPU_FILE = "/proc/" + Process.myPid() + "/stat";
    private static final int MAX_STAT_COUNT = 10;
    private final List<Stat> mCpuStats = new LinkedList();

    CpuTracker() {
    }

    protected void reset() {
        List var1 = this.mCpuStats;
        synchronized(this.mCpuStats) {
            this.mCpuStats.clear();
        }
    }

    protected boolean doTrace() {
        List var1 = this.mCpuStats;
        synchronized(this.mCpuStats) {
            if(this.mCpuStats.size() > 10) {
                this.mCpuStats.remove(0);
            }

            this.mCpuStats.add(new CpuTracker.Stat(readCpuStat("/proc/stat"), readCpuStat(PID_CPU_FILE)));
            return true;
        }
    }

    @MainThread
    @NonNull
    public BlockRecord getRecord() {
        List var2 = this.mCpuStats;
        synchronized(this.mCpuStats) {
            CpuTracker.CpuBlockRecord record = new CpuTracker.CpuBlockRecord((CpuTracker.Stat[])this.mCpuStats.toArray(new CpuTracker.Stat[this.mCpuStats.size()]));
            this.reset();
            return record;
        }
    }

    private static String readCpuStat(String file) {
        BufferedReader cpuReader = null;

        try {
            cpuReader = new BufferedReader(new FileReader(file), 1024);
            String e = cpuReader.readLine();
            if(e == null) {
                e = "";
            }

            String var3 = e;
            return var3;
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            if(cpuReader != null) {
                try {
                    cpuReader.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        return null;
    }

    private static CpuTracker.SysStat parseSysStat(String cpu) {
        String[] cpuInfo = cpu.split(" ");
        if(cpuInfo.length < 9) {
            return null;
        } else {
            CpuTracker.SysStat ss = new CpuTracker.SysStat();
            ss.user = Long.parseLong(cpuInfo[2]);
            ss.nice = Long.parseLong(cpuInfo[3]);
            ss.system = Long.parseLong(cpuInfo[4]);
            ss.idle = Long.parseLong(cpuInfo[5]);
            ss.ioWait = Long.parseLong(cpuInfo[6]);
            ss.irq = Long.parseLong(cpuInfo[7]);
            ss.softIrq = Long.parseLong(cpuInfo[8]);
            ss.total = ss.user + ss.nice + ss.system + ss.idle + ss.ioWait + ss.irq + ss.softIrq;
            return ss;
        }
    }

    private static CpuTracker.PidStat parseProcessStat(String cpu) {
        String[] cpuInfo = cpu.split(" ");
        if(cpuInfo.length < 17) {
            return null;
        } else {
            CpuTracker.PidStat ps = new CpuTracker.PidStat();
            ps.utime = Long.parseLong(cpuInfo[13]);
            ps.stime = Long.parseLong(cpuInfo[14]);
            ps.cutime = Long.parseLong(cpuInfo[15]);
            ps.cstime = Long.parseLong(cpuInfo[16]);
            ps.total = ps.utime + ps.stime + ps.cutime + ps.cstime;
            return ps;
        }
    }

    private class CpuBlockRecord extends BlockRecord {
        private CpuTracker.Stat[] mStats;

        public CpuBlockRecord(CpuTracker.Stat[] stats) {
            this.mStats = stats;
        }

        protected String getLabel() {
            return "Cpu Trace";
        }

        public String dump() {
            StringBuilder sb = (new StringBuilder("***************************************\r\n")).append(this.getLabel()).append("\r\n").append("***************************************\r\n");
            CpuTracker.SysStat lastSs = null;
            CpuTracker.PidStat lastPs = null;
            CpuTracker.Stat[] arr$ = this.mStats;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                CpuTracker.Stat stat = arr$[i$];
                CpuTracker.SysStat ss = CpuTracker.parseSysStat(stat.sysCpu);
                CpuTracker.PidStat ps = CpuTracker.parseProcessStat(stat.pidCpu);
                if(lastSs != null && lastPs != null && ss != null && ps != null) {
                    long totalTime = ss.total - lastSs.total;
                    long idleTime = ss.idle - lastSs.idle;
                    long appTotalTime = ps.total - lastPs.total;
                    long userTime = ss.user - lastSs.user;
                    long systemTime = ss.system - lastSs.system;
                    long ioWaitTime = ss.ioWait - lastSs.ioWait;
                    long irqTime = ss.irq - lastSs.irq;
                    long softIrqTime = ss.softIrq - lastSs.softIrq;
                    sb.append(stat.sysTime).append("\r\n");
                    sb.append("app:").append(appTotalTime * 100L / totalTime).append("% ");
                    sb.append("cpu:").append((totalTime - idleTime) * 100L / totalTime).append("% ");
                    sb.append("[");
                    sb.append("user:").append(userTime * 100L / totalTime).append("% ");
                    sb.append("system:").append(systemTime * 100L / totalTime).append("% ");
                    sb.append("ioWait:").append(ioWaitTime * 100L / totalTime).append("% ");
                    sb.append("irq:").append(irqTime * 100L / totalTime).append("% ");
                    sb.append("softIrq:").append(softIrqTime * 100L / totalTime).append("% ");
                    sb.append("]");
                    sb.append("\r\n");
                }

                lastSs = ss;
                lastPs = ps;
            }

            return sb.toString();
        }
    }

    private class Stat {
        long sysTime;
        String sysCpu;
        String pidCpu;

        public Stat(String sysCpu, String pidCpu) {
            this.sysTime = SystemClock.uptimeMillis() - CpuTracker.this.mTraceBeginTime;
            this.sysCpu = sysCpu;
            this.pidCpu = pidCpu;
        }
    }

    private static class PidStat {
        long utime;
        long stime;
        long cutime;
        long cstime;
        long total;

        private PidStat() {
        }
    }

    private static class SysStat {
        long user;
        long nice;
        long system;
        long idle;
        long ioWait;
        long irq;
        long softIrq;
        long total;

        private SysStat() {
        }
    }
}
