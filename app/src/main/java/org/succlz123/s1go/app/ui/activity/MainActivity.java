package org.succlz123.s1go.app.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.github.siyamed.shapeimageview.CircularImageView;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.dao.api.ConvertUidToAvatarUrl;
import org.succlz123.s1go.app.dao.api.GetMemInfo;
import org.succlz123.s1go.app.support.AppSize;
import org.succlz123.s1go.app.support.imageloader.ImageDownLoader;
import org.succlz123.s1go.app.ui.fragment.*;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawLayoutListView;
    private AppAdapter mBaseAdapter;
    private ArrayList<Fragment> mFragmentArraryList;
    private boolean mIsExit = false;//标识是否点击过一次back
    private int mPosition;
    private TextView mUid;
    private TextView mReadaccess;
    private TextView mLogin;
    private CircularImageView mCircleImageView;
    private LoginVariables loginVariables;
    private AppSize mAppSize;

    private MainForumFragment mMainForumFragment;
    private HotAreaFragment mHotAreaFragment;
    private ThemeParkFragment mThemeParkFragment;
    private SubforumFragment mSubForumFragment;
    private XiaoHeiWuFragment mXiaoHeiWuFragment;
    private HotThreadsFragment mHotThreadsFragment;
    private LoginDiaLogFragment mLoginDiaLogFragment;

    //点击返回键时，延时 TIME_TO_EXIT 毫秒发送此handler重置mIsExit，再其被重置前如果再按一次返回键则退出应用
    private Handler mExitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mIsExit = false;
        }
    };
    final static int TIME_TO_EXIT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        S1GoApplication.getInstance().addUserInfoListener(new S1GoApplication.UserInfoListener() {
            @Override
            public void onInfoChanged(LoginVariables loginVariables) {
                MainActivity.this.loginVariables = loginVariables;
                setInfo(loginVariables);
            }
        });
        final LoginVariables loginVariables = S1GoApplication.getInstance().getUserInfo();
        setInfo(loginVariables);
        setToolbar();
        setmDrawerToggle();
        mFragmentArraryListAdd();
        setmDrawLayoutListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle(GetMemInfo.getMemInfo().toString());
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawLayoutListView = (ListView) findViewById(R.id.listView);
        mUid = (TextView) findViewById(R.id.drawerlayout_uid);
        mReadaccess = (TextView) findViewById(R.id.drawerlayout_readaccess);
        mLogin = (TextView) findViewById(R.id.login);
        mCircleImageView = (CircularImageView) findViewById(R.id.avatar_drawerlayout_img);
    }

    private void setToolbar() {

//        mToolbar.setTitle("Stage1st Go");
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开开关监听
    }

    private void setmDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.shadow));
    }

    private void mFragmentArraryListAdd() {
        mFragmentArraryList = new ArrayList<Fragment>();
        mMainForumFragment = new MainForumFragment();
        mHotAreaFragment = new HotAreaFragment();
        mThemeParkFragment = new ThemeParkFragment();
        mSubForumFragment = new SubforumFragment();
        mXiaoHeiWuFragment = new XiaoHeiWuFragment();
        mHotThreadsFragment = new HotThreadsFragment();
        mLoginDiaLogFragment = new LoginDiaLogFragment();
        mFragmentArraryList.add(mMainForumFragment);
        mFragmentArraryList.add(mHotAreaFragment);
        mFragmentArraryList.add(mThemeParkFragment);
        mFragmentArraryList.add(mSubForumFragment);
        mFragmentArraryList.add(mXiaoHeiWuFragment);
        mFragmentArraryList.add(mHotThreadsFragment);
    }

    private void setmDrawLayoutListView() {
        mBaseAdapter = new AppAdapter();
        mDrawLayoutListView.setAdapter(mBaseAdapter);
        mDrawLayoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                mDrawerLayout.closeDrawer(Gravity.LEFT);//选择完fragment 关闭drawerlayout
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.mPosition = position;
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        for (int i = 0; i < mFragmentArraryList.size(); i++) {
                            Fragment fragment = mFragmentArraryList.get(i);
                            if (i == position) {
                                if (getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) == null) {
                                    fragmentTransaction.add(R.id.activity_content, fragment, fragment.getClass().getSimpleName());
                                }//找到同名fragment显示
                                fragmentTransaction.show(fragment);
                            } else {
                                if (getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) != null) {
                                    fragmentTransaction.hide(fragment);
                                }//其他不是需要的fragment隐藏
                            }
                        }
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                }, 300);
            }
        });
        mDrawLayoutListView.performItemClick(null, 0, 0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int width = mCircleImageView.getWidth();
        int height = mCircleImageView.getHeight();
        mAppSize = new AppSize(width, height);
        Log.e("mCircleImageView W_H", width + "_" + height);
    }

    private void setInfo(LoginVariables loginVariables) {

        if (loginVariables != null) {
            Logout logout = new Logout();
            mLogin.setOnClickListener(logout);
            mLogin.setText("登出");
            String toolbarname = loginVariables.getMember_username();
            String toolbaruid = loginVariables.getMember_uid();
            String toolbarreadaccess = loginVariables.getReadaccess();
            mToolbar.setSubtitle(toolbarname);
            mUid.setText("UID " + toolbaruid);
            mReadaccess.setText("阅读权限 " + toolbarreadaccess);
            if (mAppSize != null) {
                ImageDownLoader.getInstance().loadBitmap(ConvertUidToAvatarUrl.getAvatar(toolbaruid), mAppSize, new ImageDownLoader.CallBack() {
                    @Override
                    public void onLoad(String url, Bitmap bitmap) {
                        mCircleImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(String url) {

                    }
                });
            }
            if (this.mToolbar.getChildCount() > 3) {
                View view = this.mToolbar.getChildAt(3);
                view.setTranslationY(view.getHeight() + 15);
                view.animate().translationY(0);
            }
        } else if (loginVariables == null) {
            Login login = new Login();
            mLogin.setOnClickListener(login);
            mLogin.setText("登录");
            mToolbar.setSubtitle(null);
            mUid.setText("UID ");
            mReadaccess.setText("阅读权限 ");
            mCircleImageView.setImageResource(R.drawable.noavatar);
        }
    }

    private class Login implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            mLoginDiaLogFragment.show(fragmentTransaction, "");
        }
    }

    private class Logout implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dialog();
        }
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                S1GoApplication.getInstance().removeUserInfo();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RecyclerView.ViewHolder holder = null;
            convertView = getLayoutInflater().inflate(R.layout.drawerlayout_listview_item, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.fourm_drawerlayout_text);
            if (position == 0) {
                textView.setText("主论坛");
            } else if (position == 1) {
                textView.setText("热门新区");
            } else if (position == 2) {
                textView.setText("主题公园");
            } else if (position == 3) {
                textView.setText("子论坛");
            } else if (position == 4) {
                textView.setText("小黑屋");
                ImageView xiaoheiwu = (ImageView) convertView.findViewById(R.id.fourm_drawerlayout_img);
                xiaoheiwu.setBackgroundResource(R.drawable.xiaoheiwu);
            } else if (position == 5) {
                textView.setText("论坛热帖");
                ImageView hotPost = (ImageView) convertView.findViewById(R.id.fourm_drawerlayout_img);
                hotPost.setBackgroundResource(R.drawable.hot);
            }
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击两次退出
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);//不销毁activity
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return false;
//        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (mIsExit) {
            finish();
            System.exit(0);
        } else {
            mIsExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
            //两秒内不点击back则重置mIsExit
            mExitHandler.sendEmptyMessageDelayed(0, TIME_TO_EXIT);
        }
    }
}
