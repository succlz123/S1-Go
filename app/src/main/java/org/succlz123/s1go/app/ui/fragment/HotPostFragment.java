package org.succlz123.s1go.app.ui.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.bean.hostpost.HotPostDate;
import org.succlz123.s1go.app.bean.hostpost.HotPostObject;
import org.succlz123.s1go.app.dao.Api.HostPostApi;
import org.succlz123.s1go.app.dao.Helper.S1FidHelper;

import java.util.List;


/**
 * Created by fashi on 2015/4/12.
 */
public class HotPostFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private HotPostObject hotPostObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hotpostfragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.hostpost_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetHostPostAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static class AppViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView lastTime;
        private TextView lastPoster;
        private TextView name;
        private TextView time;
        private TextView reply;
        private TextView click;
        private TextView fid;

        public AppViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            title = (TextView) itemView.findViewById(R.id.hotpost_listview_title);
            name = (TextView) itemView.findViewById(R.id.hotpost_listview_name);
            time = (TextView) itemView.findViewById(R.id.hotpost_listview_time);
            lastTime = (TextView) itemView.findViewById(R.id.hotpost_listview_last_post_time);
            lastPoster = (TextView) itemView.findViewById(R.id.hotpost_listview_last_poster);
            reply = (TextView) itemView.findViewById(R.id.hotpost_listview_reply);
            click = (TextView) itemView.findViewById(R.id.hotpost_listview_click);
            fid = (TextView) itemView.findViewById(R.id.hotpost_listview_fid);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<AppViewHolder> {

        @Override
        public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
            View view = View.inflate(parent.getContext(), R.layout.hotpost_listview_item, null);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //不设置layoutprarams 默认 高宽都是 wrap 无法占满整个屏幕
            layoutParams.setMargins(7, 4, 7, 4);
            //srecyclerview 左上右下间距
            view.setLayoutParams(layoutParams);
            // 创建一个ViewHolder
            final AppViewHolder holder = new AppViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(AppViewHolder viewHolder, int i) {
            // 绑定数据到ViewHolder上

            List<HotPostDate> hotPostDateArrayList = hotPostObject.getVariables().getHotPostDateList();
            HotPostDate hotPostDate = hotPostDateArrayList.get(i);
            viewHolder.title.setText(hotPostDate.getSubject());
            viewHolder.name.setText(hotPostDate.getAuthor());
            viewHolder.time.setText(Html.fromHtml(hotPostDate.getDateline().toString()));
            viewHolder.lastTime.setText(Html.fromHtml(hotPostDate.getLastpost().toString()));
            viewHolder.lastPoster.setText(hotPostDate.getLastposter());
            viewHolder.reply.setText(hotPostDate.getReplies());
            viewHolder.click.setText(hotPostDate.getViews());
            viewHolder.fid.setText("[" + S1FidHelper.GetS1Fid(hotPostDate.getFid()) + "]");

        }

        @Override
        public int getItemCount() {
            if (hotPostObject != null) {
                return hotPostObject.getVariables().getHotPostDateList().size();
            } else {
                return 0;
            }
        }
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        private Drawable mDivider;

        public DividerItemDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent) {
            drawVertical(c, parent);
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + 9;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }

    private class GetHostPostAsyncTask extends AsyncTask<Void, Void, HotPostObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HotPostObject doInBackground(Void... params) {

            HotPostObject result = HostPostApi.getHostPost();
            return result;
        }

        @Override
        protected void onPostExecute(HotPostObject aVoid) {
            super.onPostExecute(aVoid);
            HotPostFragment.this.hotPostObject = aVoid;
            mAdapter.notifyDataSetChanged();
        }


    }
}
