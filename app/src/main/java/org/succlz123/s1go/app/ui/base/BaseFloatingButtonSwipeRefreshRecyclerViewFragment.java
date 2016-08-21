package org.succlz123.s1go.app.ui.base;

import org.succlz123.s1go.app.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by succlz123 on 16/4/22.
 */
public abstract class BaseFloatingButtonSwipeRefreshRecyclerViewFragment extends BaseSwipeRefreshFragment {
    protected RecyclerView recyclerView;
    protected FloatingActionButton floatingActionButton;

    @Override
    protected View onCreateView(LayoutInflater inflater, SwipeRefreshLayout layout, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_float_button_recycler_view, layout, false);
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = f(view, R.id.recycler_view);
        floatingActionButton = f(view, R.id.floating_button);
        if (recyclerView == null) {
            throw new NullPointerException("RecyclerView not found");
        }
        onViewCreated(recyclerView, savedInstanceState);
    }

    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void lazyLoad() {

    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRefreshing() {
        super.setRefreshing();
        floatingActionButton.setVisibility(View.INVISIBLE);
    }

    public void setRefreshComplete() {
        super.setRefreshCompleted();
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    public void setRefreshError() {
        super.setRefreshCompleted();
    }
}
