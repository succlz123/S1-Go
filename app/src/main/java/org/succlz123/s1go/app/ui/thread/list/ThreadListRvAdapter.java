package org.succlz123.s1go.app.ui.thread.list;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.bean.ThreadList;
import org.succlz123.s1go.app.ui.base.BaseThreadListRvViewHolder;
import org.succlz123.s1go.app.ui.thread.info.ThreadInfoActivity;
import org.succlz123.s1go.app.utils.BlackListHelper;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by succlz123 on 2015/6/26.
 */
public class ThreadListRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ThreadList.VariablesEntity.ForumThreadlistEntity> mThreadListList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseThreadListRvViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (mThreadListList == null) {
            return;
        }
        if (viewHolder instanceof BaseThreadListRvViewHolder) {
            final ThreadList.VariablesEntity.ForumThreadlistEntity thread = mThreadListList.get(position);

            ((BaseThreadListRvViewHolder) viewHolder).title.setText(thread.subject);
            ((BaseThreadListRvViewHolder) viewHolder).lastTime.setText(Html.fromHtml(thread.lastpost));
            ((BaseThreadListRvViewHolder) viewHolder).lastPoster.setText(thread.lastposter);
            ((BaseThreadListRvViewHolder) viewHolder).reply.setText(thread.replies);
            ((BaseThreadListRvViewHolder) viewHolder).views.setText(thread.views);

            ((BaseThreadListRvViewHolder) viewHolder).fid.setText(null);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThreadInfoActivity.newInstance(
                            viewHolder.itemView.getContext(), thread.tid,
                            thread.subject, thread.replies);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mThreadListList != null) {
            return mThreadListList.size();
        }
        return 0;
    }

    public void setData(List<ThreadList.VariablesEntity.ForumThreadlistEntity> threadList) {
        Iterator<ThreadList.VariablesEntity.ForumThreadlistEntity> iterator = threadList.iterator();
        while (iterator.hasNext()) {
            ThreadList.VariablesEntity.ForumThreadlistEntity entry = iterator.next();
            if (BlackListHelper.isBlack(MainApplication.getInstance().getApplicationContext(), entry.author)) {
                iterator.remove();
            }
        }
        if (mThreadListList == null) {
            mThreadListList = threadList;
            notifyDataSetChanged();
            return;
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new Callback(mThreadListList, threadList), true);
        mThreadListList = threadList;
        result.dispatchUpdatesTo(this);
    }

    public void change(List<ThreadList.VariablesEntity.ForumThreadlistEntity> threadList) {
        Iterator<ThreadList.VariablesEntity.ForumThreadlistEntity> iterator = threadList.iterator();
        while (iterator.hasNext()) {
            ThreadList.VariablesEntity.ForumThreadlistEntity entry = iterator.next();
            if (BlackListHelper.isBlack(MainApplication.getInstance().getApplicationContext(), entry.author)) {
                iterator.remove();
            }
        }
        mThreadListList.addAll(threadList);
        notifyDataSetChanged();
    }

    private class Callback extends DiffUtil.Callback {
        private List<ThreadList.VariablesEntity.ForumThreadlistEntity> mOldData = new ArrayList<>(0), mNewData = new ArrayList<>(0);

        private Callback(List<ThreadList.VariablesEntity.ForumThreadlistEntity> oldData, List<ThreadList.VariablesEntity.ForumThreadlistEntity> newData) {
            if (oldData != null) {
                this.mOldData = oldData;
            }
            if (newData != null) {
                this.mNewData = newData;
            }
        }

        @Override
        public int getOldListSize() {
            return mOldData.size();
        }

        @Override
        public int getNewListSize() {
            return mNewData.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldData.get(oldItemPosition).tid.equals(mNewData.get(newItemPosition).tid);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldData.get(oldItemPosition).tid.equals(mNewData.get(newItemPosition).tid);
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }
}