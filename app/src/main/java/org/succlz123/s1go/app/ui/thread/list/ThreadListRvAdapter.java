package org.succlz123.s1go.app.ui.thread.list;

import org.succlz123.s1go.app.api.bean.ThreadList;
import org.succlz123.s1go.app.ui.base.BaseThreadListRvViewHolder;
import org.succlz123.s1go.app.ui.thread.info.ThreadInfoActivity;
import org.succlz123.s1go.app.utils.common.SysUtils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fashi on 2015/6/26.
 */
public class ThreadListRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ThreadList.VariablesEntity.ForumThreadlistEntity> mThreadListList = new ArrayList<>();

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
        mThreadListList.addAll(threadList);
        notifyDataSetChanged();
    }

    public static class ItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            int margin = SysUtils.dp2px(parent.getContext(), 5);
            if (position == 0) {
                outRect.set(0, margin, 0, margin);
            } else {
                outRect.set(0, 0, 0, margin);
            }
        }
    }
}