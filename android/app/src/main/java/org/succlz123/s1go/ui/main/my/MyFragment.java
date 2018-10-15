package org.succlz123.s1go.ui.main.my;

import org.succlz123.s1go.ui.base.BaseRecyclerViewFragment;
import org.succlz123.s1go.utils.common.MyUtils;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by succlz123 on 16/4/13.
 */
public class MyFragment extends BaseRecyclerViewFragment {
    public static final int REQ_CODE_LOGIN = 200;
    private MyRvAdapter mAdapter;

    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public void onViewCreated(final RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(recyclerView, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        int dp5 = MyUtils.dip2px(5);
        recyclerView.setPadding(dp5, dp5, dp5, dp5);
        recyclerView.setClipToPadding(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
                int margin = MyUtils.dip2px(5);
                int top = 0;
                if (position == 1) {
                    top = margin;
                } else if (position == 5) {
                    top = margin;
                }
                outRect.set(0, top, 0, 0);
            }
        });
        mAdapter = new MyRvAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void lazyLoad() {

    }

    public void refresh(int requestCode) {
        if (requestCode == REQ_CODE_LOGIN) {
            if (mAdapter != null) {
                mAdapter.refresh();
            }
        }
    }
}
