package org.succlz123.s1go.app.ui.thread.info;

import com.facebook.drawee.view.SimpleDraweeView;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.api.bean.ThreadInfo;
import org.succlz123.s1go.app.utils.common.ViewUtils;
import org.succlz123.s1go.app.utils.fromhtml.ImageLinkParser;
import org.succlz123.s1go.app.utils.fromhtml.SpannedImageGetter;
import org.succlz123.s1go.app.utils.s1.S1UidToAvatarUrl;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by succlz123 on 16/4/24.
 */
public class ThreadInfoRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ThreadInfo.VariablesEntity.PostlistEntity> mThreadInfoList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ThreadInfoRvViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (mThreadInfoList == null) {
            return;
        }
        if (viewHolder instanceof ThreadInfoRvViewHolder) {

            String url = S1UidToAvatarUrl.getAvatar(mThreadInfoList.get(position).authorid);
            ((ThreadInfoRvViewHolder) viewHolder).imgAvatar.setImageURI(Uri.parse(url));

            ((ThreadInfoRvViewHolder) viewHolder).tvName.setText(mThreadInfoList.get(position).author);
            ((ThreadInfoRvViewHolder) viewHolder).tvTime.setText(Html.fromHtml(mThreadInfoList.get(position).dateline));

            int mCurrentpagerNum = 1;
            if (mCurrentpagerNum == 1) {
                if (position == 0) {
                    ((ThreadInfoRvViewHolder) viewHolder).tvNum.setText(MainApplication.getInstance().getString(R.string.louzhu));
                } else if (position > 0) {
                    ((ThreadInfoRvViewHolder) viewHolder).tvNum.setText("" + position + MainApplication.getInstance().getString(R.string.lou));
                }
            } else {
                ((ThreadInfoRvViewHolder) viewHolder).tvNum.setText("" + ((30 * (mCurrentpagerNum - 1)) + position) + MainApplication.getInstance().getString(R.string.lou));
            }

            ((ThreadInfoRvViewHolder) viewHolder).tvReviews.setMovementMethod(ImageLinkParser.getInstance());
            String reply = mThreadInfoList.get(position).message;
            if (TextUtils.isEmpty(reply)) {
                ((ThreadInfoRvViewHolder) viewHolder).tvReviews.setText("null-null");
            } else {
                Spanned spanned = Html.fromHtml(reply, new SpannedImageGetter(((ThreadInfoRvViewHolder) viewHolder).tvReviews), null);
                ((ThreadInfoRvViewHolder) viewHolder).tvReviews.setText(spanned);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mThreadInfoList != null ? mThreadInfoList.size() : 0;
    }

    public void setData(List<ThreadInfo.VariablesEntity.PostlistEntity> threadInfoList) {
        mThreadInfoList.addAll(threadInfoList);
        notifyDataSetChanged();
    }

    private static class ThreadInfoRvViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView imgAvatar;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvNum;
        public TextView tvReviews;

        public ThreadInfoRvViewHolder(View itemView) {
            super(itemView);
            imgAvatar = ViewUtils.f(itemView, R.id.author_img);
            tvName = ViewUtils.f(itemView, R.id.author_name);
            tvTime = ViewUtils.f(itemView, R.id.author_time);
            tvNum = ViewUtils.f(itemView, R.id.author_num);
            tvReviews = ViewUtils.f(itemView, R.id.author_content);
        }

        public static ThreadInfoRvViewHolder create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_item_thread_info, parent, false);
            return new ThreadInfoRvViewHolder(view);
        }
    }
}