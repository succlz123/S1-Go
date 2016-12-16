package org.succlz123.blockanalyzer;

import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.util.LinkedList;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class MainStackTracker extends StackTracker {
    private Thread mThread = Looper.getMainLooper().getThread();

    MainStackTracker() {
        super(0L, 0);
    }

    @WorkerThread
    protected boolean doTrace() {
        LinkedList var1 = this.mStackTraceList;
        synchronized (this.mStackTraceList) {
            while (this.mStackTraceList.size() >= this.mMaxStackCount) {
                this.mStackTraceList.removeFirst();
            }

            StackTraceElement[] elements = this.mThread.getStackTrace();
            if (elements != null && elements.length > 0) {
                this.mStackTraceList.addLast(new BlockStackTraceElement(String.valueOf(SystemClock.uptimeMillis() - this.mTraceBeginTime), elements));
            }

            return true;
        }
    }

    @MainThread
    @NonNull
    public BlockRecord getRecord() {
        LinkedList var2 = this.mStackTraceList;
        synchronized (this.mStackTraceList) {
            MainStackTracker.MainStackBlockRecord record = new MainStackTracker.MainStackBlockRecord((BlockStackTraceElement[]) this.mStackTraceList.toArray(new BlockStackTraceElement[this.mStackTraceList.size()]));
            this.reset();
            return record;
        }
    }

    private class MainStackBlockRecord extends StackBlockRecord {
        public MainStackBlockRecord(BlockStackTraceElement[] elements) {
            super(elements);
        }

        protected String getLabel() {
            return "Main Thread Stack Trace";
        }
    }
}
