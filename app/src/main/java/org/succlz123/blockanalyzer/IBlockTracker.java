package org.succlz123.blockanalyzer;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/**
 * Created by succlz123 on 2016/12/16.
 */

public interface IBlockTracker {
    @MainThread
    void beginTrace(long var1);

    @MainThread
    void endTrace();

    @MainThread
    @NonNull
    BlockRecord getRecord();
}
