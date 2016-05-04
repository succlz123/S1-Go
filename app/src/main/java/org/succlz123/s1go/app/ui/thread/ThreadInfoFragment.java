package org.succlz123.s1go.app.ui.thread;


import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.api.bean.ThreadInfo;
import org.succlz123.s1go.app.config.RetrofitManager;
import org.succlz123.s1go.app.ui.base.BaseRecyclerViewFragment;
import org.succlz123.s1go.app.utils.common.SysUtils;
import org.succlz123.s1go.app.widget.AppSize;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fashi on 2015/6/13.
 */
public class ThreadInfoFragment extends BaseRecyclerViewFragment {

    public static final String TID = "tid";
    public static final String TITLE = "title";
    public static final String TOTAL_PAGER_NUM = "totalPagerNum";
    public static final String CURRENTPAGERNUM = "currentpagerNum";


    private String mTid;
    private int mCurrentpagerNum;
    private int mTotalPagerNum;

    private ThreadInfoRvAdapter mThreadInfoRvAdapter;

    private AppSize mAppSize;

    public static ThreadInfoFragment newInstance(String tid, int currentPagerNum, int totalPagerNum) {
        ThreadInfoFragment threadInfoFragment = new ThreadInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TID, tid);
        bundle.putInt(CURRENTPAGERNUM, currentPagerNum);
        bundle.putInt(TOTAL_PAGER_NUM, totalPagerNum);
        threadInfoFragment.setArguments(bundle);
        return threadInfoFragment;
    }

    @Override
    public void onViewCreated(RecyclerView recyclerView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(recyclerView, savedInstanceState);

        if (getArguments() == null) {
            return;
        }

        mTid = getArguments().getString(TID);
        mCurrentpagerNum = getArguments().getInt(CURRENTPAGERNUM);
        mTotalPagerNum = getArguments().getInt(TOTAL_PAGER_NUM);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ThreadInfoRvAdapter.ItemDecoration());

        mThreadInfoRvAdapter = new ThreadInfoRvAdapter();
        recyclerView.setAdapter(mThreadInfoRvAdapter);

        if (mTid != null) {
            Observable<ThreadInfo> observable = RetrofitManager.apiService().getThreadInfo(1, mTid);

            Subscription subscription = observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Func1<ThreadInfo, Boolean>() {
                        @Override
                        public Boolean call(ThreadInfo threadInfo) {
                            return SysUtils.isActivityLive(ThreadInfoFragment.this);
                        }
                    })
                    .subscribe(new Action1<ThreadInfo>() {
                        @Override
                        public void call(ThreadInfo threadInfo) {

                            mThreadInfoRvAdapter.setData(threadInfo.Variables.postlist);

//                        swipeRefreshLayout.setRefreshing(false);
//                        swipeRefreshLayout.setEnabled(true);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
//                        swipeRefreshLayout.setRefreshing(false);
//                        swipeRefreshLayout.setEnabled(true);
                            Toast.makeText(MainApplication.getInstance().getApplicationContext(),
                                    MainApplication.getInstance().getApplicationContext().getString(R.string.sorry),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            mCompositeSubscription.add(subscription);
        }
//        LoginVariables loginInfo = MainApplication.getInstance().loginInfo;
//        if (loginInfo != null) {
//            String cookie = loginInfo.getCookiepre();
//            String auth = S1GoConfig.AUTH + "=" + Uri.encode(loginInfo.getAuth());
//            String saltKey = S1GoConfig.SALT_KEY + "=" + loginInfo.getSaltkey();
//            this.mHearders.put(S1GoConfig.COOKIE, cookie + auth + ";" + cookie + saltKey + ";");
//        }
        //每次刷新时获得的回帖数
//        int replies = aVoid.getVariables().getThreadList().getReplies();
//		SendReplyActivity.actionStart(getActivity(), mTid, reviewsObject.getVariables().getFormhash());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y / 2;
        mAppSize = new AppSize(screenWidth, screenHeight);
    }
}
