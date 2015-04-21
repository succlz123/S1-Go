//package org.succlz123.s1go.app.ui.fragment;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import org.succlz123.s1go.app.R;
//
///**
// * Created by fashi on 2015/4/11.
// */
//public class aMainForumFragment extends Fragment {
//    private RecyclerView mRecyclerView;
//    private RecyclerViewAdapter mAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.mainforumfragment, container, false);
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.mainfourm_recyclerview);
//        mRecyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), 1, false);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mAdapter = new RecyclerViewAdapter();
//        mRecyclerView.setAdapter(mAdapter);
//
//        return view;
//    }
//
//    private static class AppViewHolder extends RecyclerView.ViewHolder {
//        ImageView icon;
//        public AppViewHolder(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//
//            icon=(ImageView)itemView.findViewById(R.id.toolbar);
//            icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//        }
//    }
//
//    private class RecyclerViewAdapter extends RecyclerView.Adapter<AppViewHolder> {
//
//        @Override
//        public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
//            View view = View.inflate(parent.getContext(), android.R.layout.simple_list_item_1, null);
//            // 创建一个ViewHolder
//            final AppViewHolder holder = new AppViewHolder(view);
//
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(AppViewHolder viewHolder, int i) {
//
//            // 绑定数据到ViewHolder上
//    }
//
//        @Override
//        public int getItemCount() {
//            return 40;
//        }
//
//
//    }
//}
//
//
