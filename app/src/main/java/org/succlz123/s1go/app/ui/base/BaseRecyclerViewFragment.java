package org.succlz123.s1go.app.ui.base;

import org.succlz123.s1go.app.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by succlz123 on 16/4/22.
 */
public abstract class BaseRecyclerViewFragment extends BaseFragment {
    protected RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recyclerview, container, false);
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = f(view, R.id.recycler_view);
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
}
