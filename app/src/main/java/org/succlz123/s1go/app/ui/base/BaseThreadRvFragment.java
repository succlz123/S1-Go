package org.succlz123.s1go.app.ui.base;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.utils.common.ViewUtils;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by succlz123 on 16/4/22.
 */
public abstract class BaseThreadRvFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected long mLastRefreshStartTime;
    protected RecyclerView recyclerView;
    protected FloatingActionButton floatingActionButton;
//    private TextView hintTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_float_button_recycler_view, container, false);
        swipeRefreshLayout = ViewUtils.f(view, R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.theme_color_secondary));
        recyclerView = ViewUtils.f(view, R.id.recycler_view);
        if (recyclerView == null) {
            throw new NullPointerException("recyclerView not found");
        }
        floatingActionButton = ViewUtils.f(view, R.id.floating_button);
        floatingActionButton.setVisibility(View.INVISIBLE);

//        hintTextView.setVisibility(View.VISIBLE);
//        hintTextView.setText(TarsyliaStr.getText());

        onViewCreated(recyclerView, savedInstanceState);
        return view;
    }

    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onRefresh() {
        mLastRefreshStartTime = SystemClock.elapsedRealtime();
    }

    private Runnable mRefreshAction = new Runnable() {
        @Override
        public void run() {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(true);
            }
            mLastRefreshStartTime = SystemClock.elapsedRealtime();
        }
    };
    private Runnable mRefreshCompletedAction = new Runnable() {
        @Override
        public void run() {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.destroyDrawingCache();
            swipeRefreshLayout.clearAnimation();
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRefreshing() {
        swipeRefreshLayout.post(mRefreshAction);
    }

    public void setRefreshCompleted() {
        floatingActionButton.setVisibility(View.VISIBLE);
        swipeRefreshLayout.removeCallbacks(mRefreshAction);
        int delta = (int) (SystemClock.elapsedRealtime() - mLastRefreshStartTime);
        if (0 <= delta && delta < 500) {
            swipeRefreshLayout.postDelayed(mRefreshCompletedAction, 500 - delta);
        } else {
            swipeRefreshLayout.post(mRefreshCompletedAction);
        }
    }

    public void setRefreshError() {
        setRefreshCompleted();
    }

    public void hideSwipeRefreshLayout() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(false);
        }
    }
}
