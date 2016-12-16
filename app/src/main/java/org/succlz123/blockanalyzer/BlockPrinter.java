package org.succlz123.blockanalyzer;

import android.os.Debug;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.util.Printer;

import java.util.ArrayList;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class BlockPrinter implements Printer {
    private static final long BLOCK_THRESHOLD_MILLIS = 3000L;

    private boolean mStartedPrinting = false;
    private boolean mDebuggerConnected = false;
    private long mStartUpTime = 0L;
    private long mStartThreadTime = 0L;

    private String mLooperLog;
    private IBlockTracker[] mTraces;
    private BlockPrinter.BlockListener mBlockListener;

    public BlockPrinter(BlockPrinter.BlockListener listener) {
        this.mBlockListener = listener;
        this.mTraces = new IBlockTracker[]{new CpuTracker(), new MainStackTracker(), new AllStackTracker()};
    }

    @Override
    public void println(String x) {
        this.mStartedPrinting = !this.mStartedPrinting;
        if (this.mStartedPrinting) {
            this.mDebuggerConnected = Debug.isDebuggerConnected();
        }

        if (this.mDebuggerConnected) {
            if (this.mStartedPrinting) {
                this.mStartUpTime = SystemClock.uptimeMillis();
                this.mStartThreadTime = SystemClock.currentThreadTimeMillis();
                this.mLooperLog = x;
                this.beginTrace(this.mStartUpTime);
            } else {
                long costTime = SystemClock.uptimeMillis() - this.mStartUpTime;
                this.endTrace();
                if (this.isBlock(costTime)) {
                    this.notifyBlock(costTime);
                }
            }
        }
    }

    @MainThread
    private boolean isBlock(long costTime) {
        return costTime > BLOCK_THRESHOLD_MILLIS;
    }

    @MainThread
    private void beginTrace(long beginTime) {
        for (IBlockTracker trace : mTraces) {
            trace.beginTrace(beginTime);
        }
    }

    @MainThread
    private void endTrace() {
        for (IBlockTracker trace : mTraces) {
            trace.endTrace();
        }
    }

    @MainThread
    private void notifyBlock(long costTime) {
        ArrayList<BlockRecord> blockRecords = new ArrayList<>();
        for (IBlockTracker trace : mTraces) {
            blockRecords.add(trace.getRecord());
        }

        Block var8 = Block.create()
                .setTimes(System.currentTimeMillis(), costTime, SystemClock.currentThreadTimeMillis() - this.mStartThreadTime)
                .setLooperLog(this.mLooperLog)
                .setBlockRecords(blockRecords);
        this.mBlockListener.onBlock(var8);
    }

    public interface BlockListener {
        void onBlock(Block var1);
    }
}