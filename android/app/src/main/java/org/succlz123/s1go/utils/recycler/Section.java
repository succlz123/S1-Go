package org.succlz123.s1go.utils.recycler;

import android.support.v7.widget.RecyclerView;

/**
 * Section presents a data set for recycler view.
 * Created by succlz123 on 16/4/13.
 */
public abstract class Section {
    private int start = RecyclerView.NO_POSITION;

    /**
     * gets data of position
     */
    public abstract Object data(int adapterPosition);

    /**
     * @return size of this section in recycler view adapter
     */
    public abstract int size();

    /**
     * @return type of item view
     * @see RecyclerView.Adapter#getItemViewType(int)
     */
    public abstract int getItemViewType(int adapterPosition);

    /**
     * set start position in recycler view adapter
     */
    public final void setStart(int adapterPosition) {
        start = adapterPosition;
    }

    /**
     * get start offset of this section in recycler view adapter
     */
    public final int getStart() {
        return start;
    }

    /**
     * @return item stable unique id
     * @see RecyclerView.Adapter#hasStableIds()
     * @see RecyclerView.Adapter#getItemId(int)
     */
    public abstract long getItemId(int adapterPosition);

    /**
     * get position internal.<br>
     * the value is useless if called before {@link #setStart(int)}
     *
     * @return NO_POSITION if start not set
     */
    public final int convertAdapterPosition(int adapterPosition) {
        if (start == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }
        return adapterPosition - start;
    }
}