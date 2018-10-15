package org.succlz123.blockanalyzer;

import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class AllStackTracker extends StackTracker {
    private static final int MAX_STACK_COUNT = 100;
    private long mTraceTime;

    AllStackTracker() {
        super(2250L, 100);
    }

    @WorkerThread
    protected boolean doTrace() {
        Map map = Thread.getAllStackTraces();
        if (map != null && !map.isEmpty()) {
            this.mTraceTime = SystemClock.uptimeMillis() - this.mTraceBeginTime;
            Iterator iterator = map.entrySet().iterator();

            while (iterator.hasNext() && this.mStackTraceList.size() < this.mMaxStackCount) {
                Map.Entry threadEntry = (Map.Entry) iterator.next();
                if (threadEntry != null) {
                    StackTraceElement[] elements = (StackTraceElement[]) threadEntry.getValue();
                    if (elements != null && elements.length != 0) {
                        Thread thread = (Thread) threadEntry.getKey();
                        if (thread != null && thread != Thread.currentThread()) {
                            this.mStackTraceList.add(new BlockStackTraceElement(thread.getName() + "/" + thread.getId(), elements));
                        }
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }

    @MainThread
    @NonNull
    public BlockRecord getRecord() {
        LinkedList var2 = this.mStackTraceList;
        synchronized (this.mStackTraceList) {
            AllStackTracker.AllStackBlockRecord record = new AllStackTracker.AllStackBlockRecord((BlockStackTraceElement[]) this.mStackTraceList.toArray(new BlockStackTraceElement[this.mStackTraceList.size()]));
            this.reset();
            return record;
        }
    }

    private class AllStackBlockRecord extends StackBlockRecord {

        public AllStackBlockRecord(BlockStackTraceElement[] elements) {
            super(elements);
        }

        protected String getLabel() {
            return "All Thread Stack Trace @" + AllStackTracker.this.mTraceTime + "ms";
        }
    }
}