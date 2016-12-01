package org.succlz123.s1go.app.ui.main.hot;

import org.succlz123.s1go.app.bean.HotPost;
import org.succlz123.s1go.app.ui.base.BaseSwipeRefreshRvFragment;
import org.succlz123.s1go.app.utils.common.MyUtils;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by succlz123 on 2015/4/12.
 */
public class HotFragment extends BaseSwipeRefreshRvFragment implements HotContract.View {
    private HotRvAdapter mHotRvAdapter;
    private HotContract.Presenter mPresenter;

    public static HotFragment newInstance() {
        return new HotFragment();
    }

    @Override
    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(recyclerView, savedInstanceState);
        new HotPresenter(new HotDataSource(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        int dp5 = MyUtils.dip2px(5);
        recyclerView.setPadding(dp5, dp5, dp5, 0);
        recyclerView.setClipToPadding(false);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
                int margin = MyUtils.dip2px(5);
                outRect.set(0, 0, 0, margin);
            }
        });
        mHotRvAdapter = new HotRvAdapter();
        recyclerView.setAdapter(mHotRvAdapter);

        mPresenter.getHot();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getHot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
        mHotRvAdapter = null;
    }

    @Override
    public void setPresenter(HotContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onResponse(HotPost hotPost) {
        mHotRvAdapter.setData(hotPost);
        setRefreshCompleted();
    }

    @Override
    public void onError() {
        setRefreshCompleted();
    }
}
