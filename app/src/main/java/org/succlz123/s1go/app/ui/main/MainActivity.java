package org.succlz123.s1go.app.ui.main;

import com.bilibili.magicasakura.utils.ThemeUtils;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.ui.main.area.ForumAreaFragment;
import org.succlz123.s1go.app.ui.main.hot.HotFragment;
import org.succlz123.s1go.app.ui.main.my.MyFragment;
import org.succlz123.s1go.app.utils.IMMLeaks;
import org.succlz123.s1go.app.utils.ThemeHelper;
import org.succlz123.s1go.app.utils.common.MyUtils;
import org.succlz123.s1go.app.utils.common.ViewUtils;
import org.succlz123.s1go.app.widget.BottomNavigationBar;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by succlz123 on 16/4/13.
 */
public class MainActivity extends BaseToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ensureToolbar();
        setTitle(R.string.app_name);
        getToolbar().setTitleMarginStart(MyUtils.dip2px(20));
        ViewPager viewPager = ViewUtils.f(this, R.id.viewpager);
        MainVpAdapter mainVpAdapter = new MainVpAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainVpAdapter);

        final BottomNavigationBar bottomNavigationBar = ViewUtils.f(this, R.id.bottom_bar);
        bottomNavigationBar.addItem("节点", R.drawable.ic_panorama_fish_eye_white_48dp);
        bottomNavigationBar.addItem("热帖", R.drawable.ic_hdr_weak_white_48dp);
        bottomNavigationBar.addItem("我的", R.drawable.ic_all_inclusive_white_48dp);
        bottomNavigationBar.bindViewPager(viewPager);
        bottomNavigationBar.getChildAt(0).performClick();
        bottomNavigationBar.addOnLayoutChangeListener(
                new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        bottomNavigationBar.removeOnLayoutChangeListener(this);
                        boolean isNightTheme = ThemeHelper.isNightTheme(MainActivity.this);
                        if (isNightTheme) {
                            ThemeUtils.updateNightMode(getResources(), true);
                        }
                        ThemeUtils.refreshUI(MainActivity.this, new ThemeUtils.ExtraRefreshable() {
                            @Override
                            public void refreshGlobal(Activity activity) {
                                if (SDK_INT >= 21) {
                                    Window window = activity.getWindow();
                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                    window.setStatusBarColor(ThemeUtils.getColorById(activity, R.color.theme_color_primary_dark));
                                    ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(null, null, ThemeUtils.getThemeAttrColor(activity, android.R.attr.colorPrimary));
                                    activity.setTaskDescription(description);
                                }
                            }

                            @Override
                            public void refreshSpecificView(View view) {

                            }
                        });
                    }
                }
        );
    }

//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 21) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(ThemeUtils.getColorById(this, R.color.theme_color_primary_dark));
//            ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(null, null, ThemeUtils.getThemeAttrColor(this, android.R.attr.colorPrimary));
//            setTaskDescription(description);
//        }
//    }


    @Override
    protected void onDestroy() {
        IMMLeaks.fixFocusedViewLeak(MainApplication.getInstance());
        super.onDestroy();
    }

    private static class MainVpAdapter extends FragmentPagerAdapter {

        public MainVpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ForumAreaFragment.newInstance();
                case 1:
                    return HotFragment.newInstance();
                case 2:
                    return MyFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof MyFragment) {
                ((MyFragment) fragment).refresh(requestCode);
            }
        }
    }
}
