package org.succlz123.s1go.app.ui.area;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseFragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fashi on 2015/4/14.
 */
public class ForumAreaFragment extends BaseFragment {

    public static ForumAreaFragment newInstance() {
        ForumAreaFragment fragment = new ForumAreaFragment();
        return fragment;
    }

    public static final String TAG = ForumAreaFragment.class.getName();

    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        mRecyclerView = f(view, R.id.recycler_view);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new ForumAreaRvAdapter.ItemDecoration());
        ForumAreaRvAdapter mAdapter = new ForumAreaRvAdapter();
//        mAdapter.setOnClickListener(new AcNavigationRvAdapter.OnClickListener() {
//            @Override
//            public void onClick(View view, String partitionType) {
//                AcPartitionActivity.startActivity(getActivity(), partitionType);
        //                ThreadListActivity.newInstance(getActivity(), fidList.get(position));

//            }
//        });
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    protected void lazyLoad() {

    }
}
