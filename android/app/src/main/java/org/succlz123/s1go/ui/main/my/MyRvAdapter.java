package org.succlz123.s1go.ui.main.my;

import com.facebook.drawee.view.SimpleDraweeView;

import org.succlz123.s1go.MainApplication;
import org.succlz123.s1go.R;
import org.succlz123.s1go.bean.UserInfo;
import org.succlz123.s1go.ui.blackList.BlackListActivity;
import org.succlz123.s1go.ui.login.LoginActivity;
import org.succlz123.s1go.utils.PicHelper;
import org.succlz123.s1go.utils.SettingHelper;
import org.succlz123.s1go.utils.ThemeHelper;
import org.succlz123.s1go.utils.common.MyUtils;
import org.succlz123.s1go.utils.common.ToastUtils;
import org.succlz123.s1go.utils.common.ViewUtils;
import org.succlz123.s1go.utils.image.ImageLoader;
import org.succlz123.s1go.utils.s1.S1UidToAvatarUrl;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import static org.succlz123.s1go.R.id.uid;
import static org.succlz123.s1go.ui.main.my.MyFragment.REQ_CODE_LOGIN;

/**
 * Created by succlz123 on 16/4/13.
 */
public class MyRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VH_USER_INFO = 1;
    public static final int VH_TITLE = 2;
    public static final int VH_SETTING_NIGHT = 3;
    public static final int VH_SETTING_AVATAR = 4;
    public static final int VH_SETTING_PIC = 5;
    public static final int VH_SETTING_PHONE_TAIL = 6;
    public static final int VH_SETTING_URL_IP = 7;

    private UserInfoVH mUserInfoVH;
    private String[] mTitle = new String[]{"我的主题", "我的收藏", "我的消息", "个人黑名单"};
    private int[] mIcon = new int[]{R.drawable.ic_swap_calls_white_48dp, R.drawable.ic_clear_all_white_48dp, R.drawable.ic_remove_white_48dp};

    private int mThemeId;
    private boolean mPhoneTail;
    private boolean mUrlIp;

    public MyRvAdapter(Context context) {
        mThemeId = ThemeHelper.getTheme(context);
        mPhoneTail = SettingHelper.isShowPhoneTail(context);
        mUrlIp = SettingHelper.isUrlIp(context);
    }

    public void refresh() {
        if (mUserInfoVH == null || mUserInfoVH.tvName == null) {
            return;
        }
        UserInfo.Variables userInfo = MainApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            mUserInfoVH.tvName.setText("姓名: " + userInfo.member_username);
            mUserInfoVH.tvUid.setText("UID " + userInfo.member_uid);
            ImageLoader.getInstance().displayImage(S1UidToAvatarUrl.getAvatar(userInfo.member_uid), mUserInfoVH.imgAvatar);
        } else {
            mUserInfoVH.tvName.setText("");
            mUserInfoVH.tvUid.setText("");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VH_USER_INFO) {
            return mUserInfoVH = UserInfoVH.create(parent);
        } else if (viewType == VH_SETTING_NIGHT) {
            return SettingNightVH.create(parent);
        } else if (viewType == VH_SETTING_AVATAR) {
            return SettingAvatarVH.create(parent);
        } else if (viewType == VH_TITLE) {
            return TitleVH.create(parent);
        } else if (viewType == VH_SETTING_PIC) {
            return SettingPicVH.create(parent);
        } else if (viewType == VH_SETTING_PHONE_TAIL) {
            return PhoneTailVH.create(parent);
        } else if (viewType == VH_SETTING_URL_IP) {
            return UrlIpVH.create(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof UserInfoVH) {
            refresh();
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainApplication.getInstance().getUserInfo() != null) {
                        new AlertDialog.Builder(v.getContext()).setTitle("确定退出？")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        MainApplication.getInstance().logout();
                                        refresh();
                                    }
                                }).create().show();
                    } else {
                        LoginActivity.startForResult(MyUtils.getWrapperActivity(v.getContext()), REQ_CODE_LOGIN);
                    }
                }
            });
        } else if (viewHolder instanceof TitleVH) {
            ((TitleVH) viewHolder).tvTitle.setText(mTitle[position - 1]);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 4) {
                        BlackListActivity.start(view.getContext());
                    } else {
                        ToastUtils.showToastShort(view.getContext(), "额,有空再说.");
                    }
                }
            });
        } else if (viewHolder instanceof SettingNightVH) {
            if (mThemeId == ThemeHelper.CARD_NIGHT) {
                ((SettingNightVH) viewHolder).switchNight.setChecked(true);
            } else {
                ((SettingNightVH) viewHolder).switchNight.setChecked(false);
            }
            ((SettingNightVH) viewHolder).switchNight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshTheme(((SettingNightVH) viewHolder).switchNight);
                }
            });
        } else if (viewHolder instanceof SettingAvatarVH) {
            ((SettingAvatarVH) viewHolder).display.setText(PicHelper.getAvatarText(viewHolder.itemView.getContext()));
            ((SettingAvatarVH) viewHolder).display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("请选择")
                            .setSingleChoiceItems(PicHelper.avatarItems, PicHelper.getAvatarType(v.getContext()) - 1,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            PicHelper.setAvatarType(v.getContext(), which + 1);
                                            ((SettingAvatarVH) viewHolder).display.setText(PicHelper.getAvatarText(viewHolder.itemView.getContext()));
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .setNegativeButton("取消", null)
                            .show();
                }
            });
        } else if (viewHolder instanceof SettingPicVH) {
            ((SettingPicVH) viewHolder).display.setText(PicHelper.getPicText(viewHolder.itemView.getContext()));
            ((SettingPicVH) viewHolder).display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("请选择")
                            .setSingleChoiceItems(PicHelper.picItems, PicHelper.getPicType(v.getContext()) - 1,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            PicHelper.setPicType(v.getContext(), which + 1);
                                            ((SettingPicVH) viewHolder).display.setText(PicHelper.getPicText(viewHolder.itemView.getContext()));
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .setNegativeButton("取消", null)
                            .show();
                }
            });
        } else if (viewHolder instanceof PhoneTailVH) {
            ((PhoneTailVH) viewHolder).switchPhoneTail.setChecked(mPhoneTail);
            ((PhoneTailVH) viewHolder).switchPhoneTail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PhoneTailVH) viewHolder).switchPhoneTail.setChecked(SettingHelper.togglePhoneTail(view.getContext()));
                }
            });
        } else if (viewHolder instanceof UrlIpVH) {
            ((UrlIpVH) viewHolder).switchIp.setChecked(mUrlIp);
            ((UrlIpVH) viewHolder).switchIp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((UrlIpVH) viewHolder).switchIp.setChecked(SettingHelper.toggleUrl(view.getContext()));
                }
            });
        }
    }

    private void refreshTheme(CheckBox s) {
        final Context context = s.getContext();
        if (mThemeId == ThemeHelper.CARD_NIGHT) {
            mThemeId = ThemeHelper.CARD_SAKURA;
            ThemeHelper.setTheme(context, ThemeHelper.CARD_SAKURA);
            s.setChecked(false);
        } else {
            mThemeId = ThemeHelper.CARD_NIGHT;
            ThemeHelper.setTheme(context, ThemeHelper.CARD_NIGHT);
            s.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VH_USER_INFO;
        } else if (position == 1 || position == 2 || position == 3 || position == 4) {
            return VH_TITLE;
        } else if (position == 5) {
            return VH_SETTING_NIGHT;
        } else if (position == 6) {
            return VH_SETTING_AVATAR;
        } else if (position == 7) {
            return VH_SETTING_PIC;
        } else if (position == 8) {
            return VH_SETTING_PHONE_TAIL;
        } else if (position == 9) {
            return VH_SETTING_URL_IP;
        } else {
            return 0;
        }
    }

    private static class UserInfoVH extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvUid;
        public SimpleDraweeView imgAvatar;

        public UserInfoVH(View itemView) {
            super(itemView);
            tvName = ViewUtils.f(itemView, R.id.name);
            tvUid = ViewUtils.f(itemView, uid);
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

    private static class SettingNightVH extends RecyclerView.ViewHolder {
        public CheckBox switchNight;

        public SettingNightVH(View itemView) {
            super(itemView);
            switchNight = ViewUtils.f(itemView, R.id.switch_night);
        }

        public static SettingNightVH create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_item_my_setting_night, parent, false);
            return new SettingNightVH(view);
        }
    }

    private static class SettingAvatarVH extends RecyclerView.ViewHolder {
        public TextView display;
        public TextView title;

        public SettingAvatarVH(View itemView) {
            super(itemView);
            display = ViewUtils.f(itemView, R.id.display);
            title = ViewUtils.f(itemView, R.id.title);
            title.setText("头像显示策略");
        }

        public static SettingAvatarVH create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_item_my_setting_avater, parent, false);
            return new SettingAvatarVH(view);
        }
    }

    private static class SettingPicVH extends RecyclerView.ViewHolder {
        public TextView display;
        public TextView title;

        public SettingPicVH(View itemView) {
            super(itemView);
            display = ViewUtils.f(itemView, R.id.display);
            title = ViewUtils.f(itemView, R.id.title);
            title.setText("图片显示策略");
        }

        public static SettingPicVH create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_item_my_setting_avater, parent, false);
            return new SettingPicVH(view);
        }
    }

    private static class PhoneTailVH extends RecyclerView.ViewHolder {
        public CheckBox switchPhoneTail;

        public PhoneTailVH(View itemView) {
            super(itemView);
            switchPhoneTail = ViewUtils.f(itemView, R.id.switch_tail);
        }

        public static PhoneTailVH create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_item_my_setting_phone_tail, parent, false);
            return new PhoneTailVH(view);
        }
    }

    private static class UrlIpVH extends RecyclerView.ViewHolder {
        public CheckBox switchIp;

        public UrlIpVH(View itemView) {
            super(itemView);
            switchIp = ViewUtils.f(itemView, R.id.switch_ip);
        }

        public static UrlIpVH create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_item_my_setting_url_ip, parent, false);
            return new UrlIpVH(view);
        }
    }
}