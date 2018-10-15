package org.succlz123.blockanalyzer;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.LinkedList;

/**
 * Created by succlz123 on 2016/12/16.
 */

public abstract class StackTracker extends BlockTracker {
    private static final int DEFAULT_MAX_STACK_COUNT = 20;
    private static final int MAX_STACK_SIZE = 20;
    protected int mMaxStackCount = 20;
    protected final LinkedList<BlockStackTraceElement> mStackTraceList = new LinkedList<>();

    StackTracker(long traceDelayMillis, int maxStackCount) {
        super(traceDelayMillis);
        if (maxStackCount > 0) {
            this.mMaxStackCount = maxStackCount;
        }

    }

    public void beginTrace(long beginTime) {
        super.beginTrace(beginTime);
        this.reset();
    }

    protected void reset() {
        synchronized (this.mStackTraceList) {
            this.mStackTraceList.clear();
        }
    }

    abstract class StackBlockRecord extends BlockRecord {
        private StackTracker.BlockStackTraceElement[] mElements;

        public StackBlockRecord(StackTracker.BlockStackTraceElement[] elements) {
            this.mElements = elements;
        }

        public String dump() {
            StringBuilder sb = (new StringBuilder("***************************************\r\n")).append(this.getLabel()).append("\r\n").append("***************************************\r\n");
            if (this.mElements != null && this.mElements.length > 0) {
                StackTracker.BlockStackTraceElement[] arr$ = this.mElements;
                int len$ = arr$.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    StackTracker.BlockStackTraceElement element = arr$[i$];
                    if (element != null) {
                        sb.append((String) element.first).append("\r\n");
                        int size = ((StackTraceElement[]) element.second).length;

                        for (int i = 0; i < size; ++i) {
                            if (i > 20) {
                                sb.append("[Stack over limit size :20 , has been cutted !]").append("\r\n");
                                break;
                            }

                            StackTraceElement stackTraceElement = ((StackTraceElement[]) element.second)[i];
                            sb.append(stackTraceElement.toString()).append("\r\n");
                        }

                        sb.append("\r\n");
                    }
                }
            }

            return sb.toString();
        }
    }

    protected static class BlockStackTraceElement extends Pair<String, StackTraceElement[]> {

        public BlockStackTraceElement(@NonNull String first, @NonNull StackTraceElement[] second) {
            super(first, second);
        }
    }
}
