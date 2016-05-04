package org.succlz123.s1go.app.ui.base;

import org.succlz123.s1go.app.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by succlz123 on 16/4/24.
 */
public class BaseThreadListRvViewHolder extends BaseRvViewHolder {
    public TextView title;
    public TextView fid;
    public TextView lastPoster;
    public TextView lastTime;
    public TextView views;
    public TextView reply;

    public BaseThreadListRvViewHolder(View itemView) {
        super(itemView);
        title = f(itemView, R.id.title);
        fid = f(itemView, R.id.fid);

        lastPoster = f(itemView, R.id.last_poster);
        lastTime = f(itemView, R.id.last_post_time);
        views = f(itemView, R.id.views);
        reply = f(itemView, R.id.reply);

        itemView.setFocusable(true);
    }

    public static BaseThreadListRvViewHolder create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_view_item_thread_list, parent, false);
        return new BaseThreadListRvViewHolder(view);
    }
}
