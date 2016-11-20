package org.succlz123.s1go.app.ui.blackList;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseRecyclerViewFragment;
import org.succlz123.s1go.app.utils.BlackListHelper;
import org.succlz123.s1go.app.utils.common.MyUtils;
import org.succlz123.s1go.app.utils.common.ToastUtils;
import org.succlz123.s1go.app.utils.common.ViewUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by succlz123 on 16/4/13.
 */
public class BlackListFragment extends BaseRecyclerViewFragment {
    public static final String TAG = "BlackListFragment";
    private MyRvAdapter mAdapter;

    public static BlackListFragment newInstance() {
        return new BlackListFragment();
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
//        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
//                int margin = MyUtils.dip2px(5);
//                int top = 0;
//                if (position == 1) {
//                    top = margin;
//                } else if (position == 4) {
//                    top = margin;
//                }
//                outRect.set(0, top, 0, 0);
//            }
//        });
        mAdapter = new MyRvAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void lazyLoad() {

    }

    private static class MyRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<String> mBlackList;

        public MyRvAdapter(Context context) {
            Set<String> set = BlackListHelper.getBlackList(context);
            mBlackList = new ArrayList<>(set.size());
            for (String aSet : set) {
                mBlackList.add(aSet);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return TitleVH.create(parent);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
            if (viewHolder instanceof TitleVH) {
                ((TitleVH) viewHolder).tvTitle.setText(mBlackList.get(position));
                viewHolder.itemView.setTag(mBlackList.get(position));
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        new AlertDialog.Builder(view.getContext()).setTitle("确定删除？")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String name = (String) view.getTag();
                                        if (BlackListHelper.delete(view.getContext(), name)) {
                                            mBlackList.remove(name);
                                            notifyDataSetChanged();
                                            ToastUtils.showToastShort(view.getContext(), "删除成功");
                                        } else {
                                            ToastUtils.showToastShort(view.getContext(), "删除失败");
                                        }
                                    }
                                }).create().show();
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return mBlackList.size();
        }

        private static class TitleVH extends RecyclerView.ViewHolder {
            public TextView tvTitle;
            public ImageView imgIcon;

            public TitleVH(View itemView) {
                super(itemView);
                tvTitle = ViewUtils.f(itemView, R.id.title);
                imgIcon = ViewUtils.f(itemView, R.id.icon);
            }

            public static TitleVH create(ViewGroup parent) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.recycler_view_item_my_title, parent, false);
                return new TitleVH(view);
            }
        }
    }
}