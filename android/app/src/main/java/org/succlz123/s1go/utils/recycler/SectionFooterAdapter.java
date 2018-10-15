package org.succlz123.s1go.utils.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * 带有section的列表(只有footer,没有header)
 * 注意:RecyclerView的setLayoutManager方法必须在setAdapter方法之前调用.
 * 确保onAttachedToRecyclerView中关于LayoutManager部分及时调用
 * <pre>
 *   recycler.setLayoutManager(layoutManager);
 *   recycler.setAdapter(adapter);
 * </pre>
 */
public abstract class SectionFooterAdapter extends SectionAdapter {
    @Override
    protected boolean enableSectionHeader(int section) {
        return false;
    }

    @Override
    protected BaseAdapter.ViewHolder createSectionHeaderHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void bindSectionHeaderHolder(RecyclerView.ViewHolder holder, int section) {

    }
}
