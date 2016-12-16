package org.succlz123.blockanalyzer;

import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by succlz123 on 2016/12/16.
 */

public abstract class BlockTracker implements IBlockTracker {
    protected static final int DEFAULT_TRACE_INTERVAL_MILLIS = 300;
    protected static final String THREAD_TRACE = "thread_block_trace";
    private AtomicBoolean mIsTracing;
    private long mTraceDelayMillis;
    private long mTraceIntervalMillis;
    private Handler mTraceHandler;
    protected long mTraceBeginTime;
    private Runnable mRunnable;

    protected BlockTracker() {
        this(0L, 0L);
    }

    protected BlockTracker(long traceDelayMillis) {
        this(traceDelayMillis, 0L);
    }

    protected BlockTracker(long traceDelayMillis, long traceIntervalMillis) {
        this.mIsTracing = new AtomicBoolean(false);
        this.mRunnable = new Runnable() {
            public void run() {
                if (!BlockTracker.this.doTrace()) {
                    BlockTracker.this.endTrace();
                }

                if (BlockTracker.this.mIsTracing.get()) {
                    BlockTracker.this.mTraceHandler.postDelayed(BlockTracker.this.mRunnable, BlockTracker.this.mTraceIntervalMillis);
                }

            }
        };
        if (traceIntervalMillis <= 0L) {
            traceIntervalMillis = 300L;
        }

        this.mTraceIntervalMillis = traceIntervalMillis;
        if (traceDelayMillis <= 0L) {
            traceDelayMillis = 1500L;
        }

        this.mTraceDelayMillis = 3000L > this.mTraceIntervalMillis ? Math.min(traceDelayMillis, 3000L - this.mTraceIntervalMillis) : 0L;
        this.mTraceHandler = HandlerThreads.getHandler("thread_block_trace");
    }

    @MainThread
    public void beginTrace(long beginTime) {
        if (!this.mIsTracing.get()) {
            this.mIsTracing.set(true);
            this.mTraceBeginTime = beginTime;
            this.mTraceHandler.removeCallbacks(this.mRunnable);
            this.mTraceHandler.postDelayed(this.mRunnable, this.mTraceDelayMillis);
        }
    }

    @MainThread
    public void endTrace() {
        if (this.mIsTracing.get()) {
            this.mIsTracing.set(false);
            this.mTraceHandler.removeCallbacks(this.mRunnable);
        }
    }

    protected abstract void reset();

    @WorkerThread
    protected abstract boolean doTrace();
}
