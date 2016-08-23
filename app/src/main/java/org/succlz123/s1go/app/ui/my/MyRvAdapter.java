package org.succlz123.s1go.app.ui.my;

import com.facebook.drawee.view.SimpleDraweeView;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.api.bean.UserInfo;
import org.succlz123.s1go.app.utils.common.ToastHelper;
import org.succlz123.s1go.app.utils.common.ViewUtils;
import org.succlz123.s1go.app.utils.image.ImageLoader;
import org.succlz123.s1go.app.utils.s1.S1UidToAvatarUrl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by succlz123 on 16/4/13.
 */
public class MyRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VH_USER_INFO = 1;
    public static final int VH_TITLE = 2;

    private String[] mTitle = new String[]{"我的主题", "我的收藏", "我的消息"};
    private int[] mIcon = new int[]{R.drawable.ic_swap_calls_white_48dp, R.drawable.ic_clear_all_white_48dp, R.drawable.ic_remove_white_48dp};

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VH_USER_INFO) {
            return UserInfoVH.create(parent);
        } else {
            return TitleVH.create(parent);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof UserInfoVH) {
            UserInfo.Variables userInfo = MainApplication.getInstance().getUserInfo();
            if (userInfo == null) {
                return;
            }
            ((UserInfoVH) viewHolder).tvName.append(userInfo.member_username);
            ((UserInfoVH) viewHolder).tvUid.append(userInfo.member_uid);
            ImageLoader.getInstance().displayImage(S1UidToAvatarUrl.getAvatar(userInfo.member_uid), ((UserInfoVH) viewHolder).imgAvatar);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (viewHolder instanceof TitleVH) {
            ((TitleVH) viewHolder).tvTitle.setText(mTitle[position - 1]);
            ((TitleVH) viewHolder).imgIcon.setBackgroundResource(mIcon[position - 1]);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastHelper.showShort("额,有空再说.");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VH_USER_INFO;
        } else {
            return VH_TITLE;
        }
    }

    private static class UserInfoVH extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvUid;
        public SimpleDraweeView imgAvatar;

        public UserInfoVH(View itemView) {
            super(itemView);
            tvName = ViewUtils.f(itemView, R.id.name);
            tvUid = ViewUtils.f(itemView, R.id.uid);
            imgAvatar = ViewUtils.f(itemView, R.id.avatar);
        }

        public static UserInfoVH create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_item_my_user_info, parent, false);
            return new UserInfoVH(view);
        }
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