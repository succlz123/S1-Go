package org.succlz123.s1go.app.ui.main;

import com.facebook.drawee.view.SimpleDraweeView;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.api.bean.LoginInfo;
import org.succlz123.s1go.app.ui.area.ForumAreaFragment;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.ui.hot.HotFragment;
import org.succlz123.s1go.app.ui.login.LoginActivity;
import org.succlz123.s1go.app.ui.setting.SettingActivity;
import org.succlz123.s1go.app.utils.common.SysUtils;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends BaseToolbarActivity {
    private SimpleDraweeView mUserImg;
    private TextView mUserName;
    private TextView mUid;

    private HotFragment mHotFragment;
    private ForumAreaFragment mForumAreaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showBackButton();

        DrawerLayout drawerLayout = f(R.id.drawer_layout);
        final NavigationView navigationView = f(R.id.nav_view);

        mUserImg = (SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.img);
        mUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        mUid = (TextView) navigationView.getHeaderView(0).findViewById(R.id.uid);

        setUpDrawerContent(navigationView, drawerLayout);
        setDrawerToggle(drawerLayout);
        setUserInfo();
    }

    private void setDrawerToggle(DrawerLayout drawerLayout) {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.setScrimColor(ContextCompat.getColor(MainActivity.this, R.color.shadow));
    }

    private void setUpDrawerContent(NavigationView navigationView, final DrawerLayout drawerLayout) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home_new:
                        setFragment();
                        break;
                    case R.id.my_setting:
                        SettingActivity.newInstance(MainActivity.this);
                        break;
                    default:
                        mForumAreaFragment = (ForumAreaFragment) getSupportFragmentManager().findFragmentByTag(ForumAreaFragment.TAG);
                        if (mForumAreaFragment == null) {
                            mForumAreaFragment = ForumAreaFragment.newInstance();
                            getSupportFragmentManager().beginTransaction().hide(mHotFragment).add(R.id.content, mForumAreaFragment, ForumAreaFragment.TAG).commit();
                        } else {
                            getSupportFragmentManager().beginTransaction().hide(mHotFragment).show(mForumAreaFragment).commit();
                        }

                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.home_new);
        navigationView.getMenu().performIdentifierAction(R.id.home_new, 0);
    }

    private void setFragment() {
        mHotFragment = (HotFragment) getSupportFragmentManager().findFragmentByTag(HotFragment.TAG);
        if (mHotFragment == null) {
            mHotFragment = HotFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.content, mHotFragment, HotFragment.TAG).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(mForumAreaFragment).show(mHotFragment).commit();
        }
    }

    private void setUserInfo() {
        LoginInfo.VariablesEntity userInfo = MainApplication.getInstance().getLoginInfo();
        if (userInfo != null) {

        }
        onUserInfoChanged(userInfo);
    }

    private void onUserInfoChanged(LoginInfo.VariablesEntity userInfo) {
        if (userInfo != null) {
            String username = userInfo.member_username;
            String password = SysUtils.code(userInfo.password);
            String uid = userInfo.member_uid;
            String readAccess = userInfo.readaccess;

            toolbar.setSubtitle(username);
            mUserName.setText(username);
            mUid.setText("UID " + uid);

            if (this.toolbar.getChildCount() > 3) {
                View view = this.toolbar.getChildAt(3);
                view.setTranslationY(view.getHeight() + 15);
                view.animate().translationY(0);
            }
        } else {
            mUserName.setText("点击头像登陆");
            mUserImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginActivity.newInstance(MainActivity.this);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, Menu.FIRST, 100, "搜索");
        item.setIcon(R.drawable.ic_swap_calls_white_48dp);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return super.onOptionsItemSelected(item);
    }
}
