package org.succlz123.s1go.app.ui.main.area;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.thread.list.ThreadListActivity;
import org.succlz123.s1go.app.utils.common.ViewUtils;
import org.succlz123.s1go.app.utils.s1.S1Fid;

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
        String[] hahaForum =
                {
                        "140", "132", "138", "135", "111", "82",
                        "4", "144", "6", "136", "48", "24", "51",
                        "50", "31", "77", "75", "115", "27"
                };
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
        mFidList = Arrays.asList(hahaForum);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ForumAreaVH.create(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ForumAreaVH) {
            ((ForumAreaVH) viewHolder).name.setText(S1Fid.getS1FidName(mFidList.get(position)));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThreadListActivity.start(viewHolder.itemView.getContext(), String.valueOf(mFidList.get(viewHolder.getAdapterPosition())));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mFidList.size();
    }

    private static class ForumAreaVH extends RecyclerView.ViewHolder {
        public TextView name;

        public ForumAreaVH(View itemView) {
            super(itemView);
            name = ViewUtils.f(itemView, R.id.name);
        }

        public static ForumAreaVH create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_item_forum_area, parent, false);
            return new ForumAreaVH(view);
        }
    }
}