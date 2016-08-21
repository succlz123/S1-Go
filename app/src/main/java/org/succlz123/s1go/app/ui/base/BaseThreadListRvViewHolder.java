package org.succlz123.s1go.app.ui.base;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.utils.common.ViewUtils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by succlz123 on 16/4/24.
 */
public class BaseThreadListRvViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView fid;
    public TextView lastPoster;
    public TextView lastTime;
    public TextView views;
    public TextView reply;

    public BaseThreadListRvViewHolder(View itemView) {
        super(itemView);
        title = ViewUtils.f(itemView, R.id.title);
        fid = ViewUtils.f(itemView, R.id.fid);

        lastPoster = ViewUtils.f(itemView, R.id.last_poster);
        lastTime = ViewUtils.f(itemView, R.id.last_post_time);
        views = ViewUtils.f(itemView, R.id.views);
        reply = ViewUtils.f(itemView, R.id.reply);

        itemView.setFocusable(true);
    }

    public static BaseThreadListRvViewHolder create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_view_item_thread_list, parent, false);
        return new BaseThreadListRvViewHolder(view);
    }
}
