package org.succlz123.s1go.utils.recycler;

import android.support.v7.widget.RecyclerView;

public abstract class BaseSection extends Section {

    @Override
    public long getItemId(int position) {
        return RecyclerView.NO_ID;
    }
}