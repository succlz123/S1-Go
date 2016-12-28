package org.succlz123.s1go.app.ui.main.hot;

import org.succlz123.s1go.app.bean.HotPost;
import org.succlz123.s1go.app.ui.base.BaseThreadListRvViewHolder;
import org.succlz123.s1go.app.ui.thread.info.ThreadInfoActivity;
import org.succlz123.s1go.app.utils.s1.S1Fid;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by succlz123 on 16/4/12.
 */
public class HotRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HotPost.VariablesEntity.DataEntity> mHotPost;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseThreadListRvViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (mHotPost == null) {
            return;
        }
        final HotPost.VariablesEntity.DataEntity hostPost = mHotPost.get(position);

        if (viewHolder instanceof BaseThreadListRvViewHolder) {
            ((BaseThreadListRvViewHolder) viewHolder).title.setText(hostPost.subject);
            ((BaseThreadListRvViewHolder) viewHolder).lastTime.setText(Html.fromHtml(hostPost.lastpost));
            ((BaseThreadListRvViewHolder) viewHolder).lastPoster.setText(hostPost.lastposter);
            ((BaseThreadListRvViewHolder) viewHolder).reply.setText(hostPost.replies);
            ((BaseThreadListRvViewHolder) viewHolder).views.setText(hostPost.views);
            ((BaseThreadListRvViewHolder) viewHolder).fid.setText("[" + S1Fid.getS1FidName(hostPost.fid) + "]");
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadInfoActivity.newInstance(viewHolder.itemView.getContext(), hostPost.tid,
                        hostPost.subject, hostPost.replies);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mHotPost == null) {
            return 0;
        }
        return mHotPost.size();
    }

    public void setData(HotPost hotPost) {
        if (hotPost == null || hotPost.Variables == null || hotPost.Variables.data == null) {
            return;
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new Callback(mHotPost, hotPost.Variables.data), true);
        mHotPost = hotPost.Variables.data;
        result.dispatchUpdatesTo(this);
    }

    private class Callback extends DiffUtil.Callback {
        private List<HotPost.VariablesEntity.DataEntity> mOldData=new ArrayList<>(), mNewData=new ArrayList<>();

        private Callback(List<HotPost.VariablesEntity.DataEntity> oldData, List<HotPost.VariablesEntity.DataEntity> newData) {
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
    }
}
