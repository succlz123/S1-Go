package org.succlz123.s1go.app.utils.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * 带有section的列表(只有header,没有footer)
 * 注意:RecyclerView的setLayoutManager方法必须在setAdapter方法之前调用.
 * 确保onAttachedToRecyclerView中关于LayoutManager部分及时调用
 * <pre>
 *   recycler.setLayoutManager(layoutManager);
 *   recycler.setAdapter(adapter);
 * </pre>
 */
public abstract class SectionHeaderAdapter extends SectionAdapter {
    @Override
    protected final boolean enableSectionFooter(int section) {
        return false;
    }

    @Override
    protected final ViewHolder createSectionFooterHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected final void bindSectionFooterHolder(RecyclerView.ViewHolder holder, int section) {

    }
}
