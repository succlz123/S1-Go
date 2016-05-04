package org.succlz123.s1go.app.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by succlz123 on 16/4/24.
 */
public class BaseRvViewHolder extends RecyclerView.ViewHolder {

    public BaseRvViewHolder(View itemView) {
        super(itemView);
    }

    protected <T extends View> T f(View view, int resId) {
        return (T) view.findViewById(resId);
    }
}
