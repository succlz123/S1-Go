package org.succlz123.s1go.app.utils.recycler;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;

/**
 * 带有section的列表
 * 注意:RecyclerView的setLayoutManager方法必须在setAdapter方法之前调用.
 * 确保onAttachedToRecyclerView中关于LayoutManager部分及时调用
 * <pre>
 *   recycler.setLayoutManager(layoutManager);
 *   recycler.setAdapter(adapter);
 * </pre>
 */
public abstract class SectionAdapter extends BaseAdapter {
    public static final int TYPE_SECTION_HEADER_DEFAULT = -3;
    public static final int TYPE_SECTION_FOOTER_DEFAULT = -4;

    private SparseArray<SectionItem> mSectionItems = new SparseArray<>();
    private HashSet<Integer> mHeaderTypes = new HashSet<>();
    private HashSet<Integer> mFooterTypes = new HashSet<>();

    public SectionAdapter() {
        super();
        registerAdapterDataObserver(new SectionDataObserver());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setSectionItems();
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isSectionHeader(position) || isSectionFooter(position)) {
                        return gridLayoutManager.getSpanCount();
                    }

                    return SectionAdapter.this.getSpanSize(position, gridLayoutManager.getSpanCount());
                }
            });
        }
    }

    @Override
    public final int getItemCount() {
        return mSectionItems.size();
    }

    private void setSectionItems() {
        mSectionItems.clear();
        int sectionCount = getSectionCount();
        int position = 0;

        for (int i = 0; i < sectionCount; i++) {
            if (enableSectionHeader(i)) {
                setSectionItem(position, true, false, i, 0);
                position++;
            }

            for (int j = 0; j < getItemCountForSection(i); j++) {
                setSectionItem(position, false, false, i, j);
                position++;
            }

            if (enableSectionFooter(i)) {
                setSectionItem(position, false, true, i, 0);
                position++;
            }
        }
    }

    private void setSectionItem(int position, boolean isSectionHeader, boolean isSectionFooter,
                                int sectionForPosition, int indexInSection) {
        SectionItem item = mSectionItems.get(position);
        if (item == null) {
            item = new SectionItem();
            mSectionItems.put(position, item);
        }

        item.isHeader = isSectionHeader;
        item.isFooter = isSectionFooter;
        item.section = sectionForPosition;
        item.indexInSection = indexInSection;
    }

    @Override
    public final ViewHolder createHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;

        if (isSectionHeaderViewType(viewType)) { /**创建section头部View*/
            viewHolder = createSectionHeaderHolder(parent, viewType);
            if (viewHolder == null) {
                throw new IllegalStateException("the SectionHeaderHolder's viewType equals " + viewType + " can not be created as null !");
            }
        } else if (isSectionFooterViewType(viewType)) { /**创建section底部View*/
            viewHolder = createSectionFooterHolder(parent, viewType);
            if (viewHolder == null) {
                throw new IllegalStateException("the SectionFooterHolder's viewType equals " + viewType + " can not be created as null !");
            }
        } else {  /**创建Item View*/
            viewHolder = createSectionContentHolder(parent, viewType);
            if (viewHolder == null) {
                throw new IllegalStateException("the SectionContentHolder's viewType equals " + viewType + " can not be created as null !");
            }
        }

        return viewHolder;
    }

    @Override
    public final void bindHolder(ViewHolder holder, int position, View itemView) {
        int section = mSectionItems.get(position).section;
        int index = mSectionItems.get(position).indexInSection;

        if (isSectionHeader(position)) {
            bindSectionHeaderHolder(holder, section);
        } else if (isSectionFooter(position)) {
            bindSectionFooterHolder(holder, section);
        } else {
            bindSectionContentHolder(holder, section, index);
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (isSectionHeader(position)) {
            int sectionHeaderViewType = getSectionHeaderViewType(position);
            mHeaderTypes.add(sectionHeaderViewType);
            return sectionHeaderViewType;
        } else if (isSectionFooter(position)) {
            int sectionFooterViewType = getSectionFooterViewType(position);
            mFooterTypes.add(sectionFooterViewType);
            return sectionFooterViewType;
        } else {
            return getSectionContentViewType(position);
        }
    }

    /** 覆写该方法,增加Header Type */
    public int getSectionHeaderViewType(int position) {
        return TYPE_SECTION_HEADER_DEFAULT;
    }

    /** 覆写该方法,增加Footer Type */
    public int getSectionFooterViewType(int position) {
        return TYPE_SECTION_FOOTER_DEFAULT;
    }

    /** 覆写该方法,增加Content Type */
    public int getSectionContentViewType(int position) {
        return super.getItemViewType(position);
    }

    /** 是否为section头部 */
    public boolean isSectionHeader(int position) {
        SectionItem sectionItem = mSectionItems.get(position);
        return sectionItem.isHeader && enableSectionHeader(sectionItem.section);
    }

    /** 是否为section底部 */
    public boolean isSectionFooter(int position) {
        SectionItem sectionItem = mSectionItems.get(position);
        return sectionItem.isFooter && enableSectionFooter(sectionItem.section);
    }

    protected boolean isSectionHeaderViewType(int viewType) {
        return mHeaderTypes.contains(viewType);
    }

    protected boolean isSectionFooterViewType(int viewType) {
        return mFooterTypes.contains(viewType);
    }

    /** 获取position对应的sectionItem对象 */
    public SectionItem getSectionItem(int position) {
        return mSectionItems.get(position);
    }

    /** 获取position所在的section值 */
    public int getSection(int position) {
        return mSectionItems.get(position).section;
    }

    /** 获取position所在的section中的索引值 */
    public int getIndexInSection(int position) {
        return mSectionItems.get(position).indexInSection;
    }

    /** 覆写该方法,设置跨度 */
    public int getSpanSize(int position, int defaultSpanCount) {
        return 1;
    }

    /** 覆写该方法,设置section是否允许Header出现 */
    protected boolean enableSectionHeader(int section) {
        return true;
    }

    /** 覆写该方法,设置section是否允许footer出现 */
    protected boolean enableSectionFooter(int section) {
        return true;
    }

    /** 获取section总数 */
    protected abstract int getSectionCount();

    /** 获取section中item总数,不包含header以及footer */
    protected abstract int getItemCountForSection(int section);

    /** 创建section头部的ViewHolder */
    protected abstract ViewHolder createSectionHeaderHolder(ViewGroup parent, int viewType);

    /** 绑定数据到section头部的View */
    protected abstract void bindSectionHeaderHolder(RecyclerView.ViewHolder holder, int section);

    /** 创建section底部的ViewHolder */
    protected abstract ViewHolder createSectionFooterHolder(ViewGroup parent, int viewType);

    /** 绑定数据到section底部的View */
    protected abstract void bindSectionFooterHolder(RecyclerView.ViewHolder holder, int section);

    /** 创建item的ViewHolder */
    protected abstract ViewHolder createSectionContentHolder(ViewGroup parent, int viewType);

    /** 绑定数据到item的View */
    protected abstract void bindSectionContentHolder(RecyclerView.ViewHolder holder, int section, int indexInSection);

    class SectionDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            setSectionItems();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            setSectionItems();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            setSectionItems();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            setSectionItems();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            setSectionItems();
        }
    }

    public static class SectionItem {
        /** position所在的section值 */
        public int section;
        /** position所在的section索引值 */
        public int indexInSection;
        /** 是否为section头部 */
        public boolean isHeader;
        /** 是否为section底部 */
        public boolean isFooter;
    }
}
