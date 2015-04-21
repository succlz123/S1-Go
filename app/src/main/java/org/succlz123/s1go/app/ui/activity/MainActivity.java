package org.succlz123.s1go.app.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.dao.Api.GetAvatarApi;
import org.succlz123.s1go.app.ui.fragment.*;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView drawLayoutListView;
    private AppAdapter mbaseAdapter;
    private ArrayList<Fragment> fragmentArraryList;
    private boolean mIsExit = false;//标识是否点击过一次back
    private int position;
    private TextView uid;
    private TextView readaccess;
    private TextView mLogin;
    private ImageView circleImageView;
    private LoginVariables loginVariables;

    private MainForumFragment mainForum = new MainForumFragment();
    private HotAreaFragment hotArea = new HotAreaFragment();
    private ThemeParkFragment themePark = new ThemeParkFragment();
    private SubforumFragment subForum = new SubforumFragment();
    private XiaoHeiWuFragment xiaoHeiWuFragment = new XiaoHeiWuFragment();
    private HotPostFragment hotPostFragment = new HotPostFragment();
    private LoginDiaLogFragment loginDiaLogFragment = new LoginDiaLogFragment();

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
        findViews();
        S1GoApplication.getInstance().addUserInfoListener(new S1GoApplication.UserInfoListener() {
            @Override
            public void onInfoChanged(LoginVariables loginVariables) {
                MainActivity.this.loginVariables = loginVariables;
                setInfo(loginVariables);
            }
        });
        final LoginVariables loginVariables = S1GoApplication.getInstance().getUserInfo();
        setInfo(loginVariables);
        mToolbar.setTitle("Stage1st Go");
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开开关监听
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
        fragmentArraryList = new ArrayList<Fragment>();
        fragmentArraryList.add(mainForum);
        fragmentArraryList.add(hotArea);
        fragmentArraryList.add(themePark);
        fragmentArraryList.add(subForum);
        fragmentArraryList.add(xiaoHeiWuFragment);
        fragmentArraryList.add(hotPostFragment);
        mbaseAdapter = new AppAdapter();
        drawLayoutListView.setAdapter(mbaseAdapter);
        drawLayoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                mDrawerLayout.closeDrawer(Gravity.LEFT);//选择完fragment 关闭drawerlayout
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.position = position;
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        for (int i = 0; i < fragmentArraryList.size(); i++) {
                            Fragment fragment = fragmentArraryList.get(i);
                            if (i == position) {
                                if (getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) == null) {
                                    fragmentTransaction.add(R.id.darwer_frameLayout, fragment, fragment.getClass().getSimpleName());
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
        drawLayoutListView.performItemClick(null, 0, 0);
    }

    private void findViews() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawLayoutListView = (ListView) findViewById(R.id.listView);
        uid = (TextView) findViewById(R.id.drawerlayout_uid);
        readaccess = (TextView) findViewById(R.id.drawerlayout_readaccess);
        mLogin = (TextView) findViewById(R.id.login);
        circleImageView = (ImageView) findViewById(R.id.avatar_drawerlayout_img);
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
            uid.setText("UID " + toolbaruid);
            readaccess.setText("阅读权限 " + toolbarreadaccess);
            new GetAvatarApi.GetAvatar(toolbaruid, circleImageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            uid.setText("UID ");
            readaccess.setText("阅读权限 ");
            circleImageView.setImageResource(R.drawable.noavatar);
        }
    }

    private class Login implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            loginDiaLogFragment.show(fragmentTransaction, "");
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
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
