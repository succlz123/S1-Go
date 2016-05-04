package org.succlz123.s1go.app.ui.area;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseRvViewHolder;
import org.succlz123.s1go.app.ui.thread.ThreadListActivity;
import org.succlz123.s1go.app.utils.s1.S1Fid;
import org.succlz123.s1go.app.utils.common.SysUtils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by succlz123 on 16/4/13.
 */
public class ForumAreaRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mFidList;

    public ForumAreaRvAdapter() {
        String[] mainForum =
                {
                        "4", "135", "6", "136", "48", "24",
                        "51", "50", "31", "77", "75", "133",
                        "82", "115", "119"
                };
        String[] hotForum =
                {
                        "134", "138", "118", "132", "105", "131",
                        "69", "76", "111"
                };
        String[] subForum =
                {
                        "27", "9", "15"
                };
        String[] themePark =
                {
                        "122", "125", "85", "26", "45", "42",
                        "41", "43", "46", "33", "49", "61",
                        "60"
                };
        mFidList = Arrays.asList(mainForum);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ForumAreaVH.create(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ForumAreaVH) {
            ((ForumAreaVH) viewHolder).mName.setText(S1Fid.getS1Fid(mFidList.get(position)));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThreadListActivity.actionStart(viewHolder.itemView.getContext(),
                            String.valueOf(mFidList.get(viewHolder.getAdapterPosition())));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mFidList.size();
    }

    private static class ForumAreaVH extends BaseRvViewHolder {
        private TextView mName;

        public ForumAreaVH(View itemView) {
            super(itemView);
            mName = f(itemView, R.id.name);
        }

        public static ForumAreaVH create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_item_forum_area, parent, false);
            return new ForumAreaVH(view);
        }
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
            int left = 0;
            int top = 0;
            if (position != 0 && position != 1) {
                top = margin;
            }
            if (position % 2 != 0) {
                left = margin;
            }
            outRect.set(left, top, 0, 0);
        }
    }
}