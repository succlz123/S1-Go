package org.succlz123.s1go.utils.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * * Created by succlz123 on 16/4/13.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {
    public abstract void bindHolder(ViewHolder holder, int position, View itemView);

    public abstract ViewHolder createHolder(ViewGroup parent, int viewType);

    /** 创建新View,提供给LayoutManger调用 */
    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = createHolder(parent, viewType);
        holder.handleClick();
        return holder;
    }

    /** 将数据与界面进行绑定操作 */
    @Override
    public final void onBindViewHolder(final ViewHolder holder, int position) {
        bindHolder(holder, position, holder.itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 覆写这个方法处理点击事件
     * <pre>
     * public void handleClick(ViewHolder holder) {
     *      TextView item = holder.getItemView().findViewById(R.id.item);
     *      item.setOnClickListener(new View.OnClickListener() {
     *          public void onClick(View v) {
     *          }
     *      });
     * }
     * </pre>
     */
    public void handleClick(ViewHolder holder) {
    }

    public static class ViewHolder<T extends BaseAdapter> extends RecyclerView.ViewHolder {
        private T mAdapter;

        public ViewHolder(View itemView, T adapter) {
            super(itemView);
            mAdapter = adapter;
        }

        private void handleClick() {
            mAdapter.handleClick(this);
            if (mAdapter.mHandleClickListener != null) {
                mAdapter.mHandleClickListener.handleClick(this);
            }
        }

        public T getAdapter() {
            return mAdapter;
        }
    }


    protected HandleClickListener mHandleClickListener;

    public interface HandleClickListener {
        void handleClick(ViewHolder holder);
    }

    public void setHandleClickListener(HandleClickListener handleClickListener) {
        mHandleClickListener = handleClickListener;
    }
}
