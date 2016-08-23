package org.succlz123.s1go.app.ui.area;

import org.succlz123.s1go.app.ui.base.BaseRecyclerViewFragment;
import org.succlz123.s1go.app.utils.common.MyUtils;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by succlz123 on 16/4/13.
 */
public class ForumAreaFragment extends BaseRecyclerViewFragment {

    public static ForumAreaFragment newInstance() {
        return new ForumAreaFragment();
    }

    @Override
    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(recyclerView, savedInstanceState);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recyclerView.setHasFixedSize(true);
        int dp5 = MyUtils.dip2px(5);
        recyclerView.setPadding(dp5, dp5, dp5, dp5);
        recyclerView.setClipToPadding(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
                int margin = MyUtils.dip2px(5);
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
        });
        ForumAreaRvAdapter mAdapter = new ForumAreaRvAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void lazyLoad() {

    }
}
