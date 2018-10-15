package org.succlz123.s1go.ui.thread.list;

import org.succlz123.s1go.MainApplication;
import org.succlz123.s1go.R;
import org.succlz123.s1go.bean.ThreadList;
import org.succlz123.s1go.bean.UserInfo;
import org.succlz123.s1go.ui.base.BaseThreadRvFragment;
import org.succlz123.s1go.ui.login.LoginActivity;
import org.succlz123.s1go.ui.thread.send.SendThreadsActivity;
import org.succlz123.s1go.utils.common.MyUtils;
import org.succlz123.s1go.utils.common.ToastUtils;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

/**
 * Created by succlz123 on 2015/6/25.
 */
public class ThreadListFragment extends BaseThreadRvFragment implements ThreadListContract.View {
    public static final String TAG = "ThreadListFragment";
    public static final String KEY_THREAD_FRAGMENT_FID = "key_fragment_thread_fid";

    private ThreadListContract.Presenter mPresenter;
    private ThreadListRvAdapter mThreadListRvAdapter;
    private LinearLayoutManager mLayoutManager;

    public static ThreadListFragment newInstance(String tid) {
        ThreadListFragment threadListFragment = new ThreadListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_THREAD_FRAGMENT_FID, tid);
        threadListFragment.setArguments(bundle);
        return threadListFragment;
    }

    @Override
    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(recyclerView, savedInstanceState);

        final String fid;
        if (getArguments() == null || TextUtils.equals(fid = getArguments().getString(KEY_THREAD_FRAGMENT_FID), "0")) {
            getActivity().finish();
            return;
        }

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
                int margin = MyUtils.dip2px(5);
                if (position == 0) {
                    outRect.set(0, margin, 0, margin);
                } else {
                    outRect.set(0, 0, 0, margin);
                }
            }
        });
        mThreadListRvAdapter = new ThreadListRvAdapter();
        recyclerView.setAdapter(mThreadListRvAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mThreadListRvAdapter == null) {
                    return;
                }
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if (mLayoutManager.getChildCount() > 0
                        && lastVisibleItemPosition + 1 >= mLayoutManager.getItemCount() - 1
                        && mLayoutManager.getItemCount() > mLayoutManager.getChildCount()) {
                    mPresenter.loadThreadList();
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.Variables loginInfo = MainApplication.getInstance().loginInfo;
                if (loginInfo == null) {
                    LoginActivity.start(getActivity());
                } else {
                    SendThreadsActivity.start(getActivity(), fid, mPresenter.getFormHash());
                }
            }
        });
        mPresenter.loadThreadList();
        setRefreshing();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThreadListRvAdapter = null;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.onRefresh();
    }

    public void goToTop() {
        if (recyclerView == null || mLayoutManager == null) {
            return;
        }
        recyclerView.stopScroll();
        mLayoutManager.setSmoothScrollbarEnabled(true);
        int firstVisibilityPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstVisibilityPosition > 10) {
            mLayoutManager.scrollToPositionWithOffset(10, 0);
        }
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void setPresenter(ThreadListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setData(List<ThreadList.VariablesEntity.ForumThreadlistEntity> data) {
        if (mThreadListRvAdapter == null) {
            return;
        }
        mThreadListRvAdapter.setData(data);
        setRefreshCompleted();
    }

    @Override
    public void setChange(List<ThreadList.VariablesEntity.ForumThreadlistEntity> data) {
        if (mThreadListRvAdapter == null) {
            return;
        }
        mThreadListRvAdapter.change(data);
        setRefreshCompleted();
    }

    @Override
    public void onFailed() {
        ToastUtils.showToastShort(getActivity(), R.string.sorry);
        setRefreshError();
    }
}
