package org.succlz123.s1go.app.utils.recycler;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Sections adapter for RecyclerView
 * * Created by succlz123 on 16/4/13.
 */
public abstract class BaseSectionAdapter<VH extends BaseSectionAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<Section> sections = new ArrayList<>();
    private SparseArrayCompat<Section> caches = new SparseArrayCompat<>();

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Section section = getSection(position);
        if (section != null) {
            holder.bind(section.data(position));
        }
    }

    @Override
    public long getItemId(int position) {
        if (hasStableIds()) {
            Section section = getSection(position);
            if (section != null) {
                return section.getItemId(position);
            }
        }
        return super.getItemId(position);
    }

    /**
     * @return size count of all sections
     */
    @Override
    public final int getItemCount() {
        return caches.size();
    }

    @Override
    public int getItemViewType(int position) {
        Section section = getSection(position);
        if (section == null) return 0;
        return section.getItemViewType(position);
    }

    /**
     * gets cached section by position
     */
    public final Section getSection(int position) {
        return caches.get(position);
    }

    protected final int getSectionsSize() {
        return sections.size();
    }

    protected final Section getSectionInternal(int position) {
        if (position >= sections.size() || position < 0) return null;
        return sections.get(position);
    }

    protected final int getSectionInternalPosition(Section section) {
        if (section == null)
            return -1;
        return sections.indexOf(section);
    }

    /** add to internal sections list */
    protected final void addSectionInternal(int position, Section section) {
        sections.add(position, section);
    }

    protected final void addSectionInternal(Section section) {
        sections.add(section);
    }


    public final void removeSection(int position) {
        sections.remove(position);
        onSectionsChanged();
    }

    protected final void removeSectionInternal(Section section) {
        sections.remove(section);
    }

    /**
     * attaches data set
     */
    public final void setSections(List<? extends Section> list) {
        sections.clear();
        caches.clear();
        sections.addAll(list);
        onSectionsChanged();
    }

    protected final void onSectionsChanged() {
        onSectionsChanged(true);
    }

    /** rebuilds section caches */
    protected final void onSectionsChanged(boolean invalidate) {
        caches.clear();
        int count = 0;
        for (Section sec : sections) {
            sec.setStart(count);
            final int size = sec.size();
            for (int i = 0; i < size; i++)
                caches.put(count + i, sec);
            count += size;
        }
        if (invalidate) {
            notifyDataSetChanged();
        }
    }

    /** adapter's host is destroying */
    public void onDestroy() {
        clear();
    }

    /**
     * clears the caches and sections data set
     */
    public final void clear() {
        caches.clear();
        sections.clear();
    }

    @Override
    protected void finalize() throws Throwable {
        if (caches.size() > 0 || sections.size() > 0) {
//            DebugLog.i("SectionAdapter", this + " finalized not called onDestroy()!");
            onDestroy();
        }
        super.finalize();
    }

    /**
     * for fragment {@link android.support.v4.app.Fragment#setUserVisibleHint(boolean)}<br>
     */
    public void notifyUserVisible(boolean isVisibleToUser) {
        // no op
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(Object data);
    }

}