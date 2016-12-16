package org.succlz123.blockanalyzer;

/**
 * Created by succlz123 on 2016/12/16.
 */

public abstract class BlockRecord {

    public BlockRecord() {
    }

    protected abstract String getLabel();

    public abstract String dump();
}
