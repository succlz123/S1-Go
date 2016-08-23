package org.succlz123.s1go.app.ui.hot;

import org.succlz123.s1go.app.api.bean.HotPost;
import org.succlz123.s1go.app.ui.base.BaseThreadListRvViewHolder;
import org.succlz123.s1go.app.ui.thread.info.ThreadInfoActivity;
import org.succlz123.s1go.app.utils.s1.S1Fid;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by succlz123 on 16/4/12.
 */
public class HotRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private HotPost mHotPost;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseThreadListRvViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (mHotPost == null || mHotPost.Variables == null || mHotPost.Variables.data == null) {
            return;
        }
        final HotPost.VariablesEntity.DataEntity hostPost = mHotPost.Variables.data.get(position);

        if (viewHolder instanceof BaseThreadListRvViewHolder) {
            ((BaseThreadListRvViewHolder) viewHolder).title.setText(hostPost.subject);
            ((BaseThreadListRvViewHolder) viewHolder).lastTime.setText(Html.fromHtml(hostPost.lastpost));
            ((BaseThreadListRvViewHolder) viewHolder).lastPoster.setText(hostPost.lastposter);
            ((BaseThreadListRvViewHolder) viewHolder).reply.setText(hostPost.replies);
            ((BaseThreadListRvViewHolder) viewHolder).views.setText(hostPost.views);
            ((BaseThreadListRvViewHolder) viewHolder).fid.setText("[" + S1Fid.getS1Fid(hostPost.fid) + "]");
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
        if (mHotPost == null || mHotPost.Variables == null || mHotPost.Variables.data == null) {
            return 0;
        }
        return mHotPost.Variables.data.size();
    }

    public void setData(HotPost hotPost) {
        mHotPost = hotPost;
        notifyDataSetChanged();
    }
}
