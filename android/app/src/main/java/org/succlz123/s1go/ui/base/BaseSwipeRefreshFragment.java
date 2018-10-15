package org.succlz123.s1go.ui.base;

import org.succlz123.s1go.R;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by succlz123 on 16/4/29.
 */
public abstract class BaseSwipeRefreshFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    protected SwipeRefreshLayout swipeRefreshLayout;
    private long mLastRefreshStartTime;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.layout_swipe_refresh_layout, container, false);
        swipeRefreshLayout.setOnRefreshListener(this);
        View view = onCreateView(inflater, swipeRefreshLayout, savedInstanceState);
        if (view.getParent() == null) {
            swipeRefreshLayout.addView(view, 0);
        }
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.theme_color_primary));
        return swipeRefreshLayout;
    }

    protected abstract View onCreateView(LayoutInflater inflater, SwipeRefreshLayout layout, Bundle savedInstanceState);

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

    public void setRefreshing() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(mRefreshAction);
        }
    }

    public void setRefreshCompleted() {
        swipeRefreshLayout.removeCallbacks(mRefreshAction);
        int delta = (int) (SystemClock.elapsedRealtime() - mLastRefreshStartTime);
        if (0 <= delta && delta < 500) {
            swipeRefreshLayout.postDelayed(mRefreshCompletedAction, 500 - delta);
        } else {
            swipeRefreshLayout.post(mRefreshCompletedAction);
        }
    }

    public void hideSwipeRefreshLayout() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(false);
        }
    }
}