package org.succlz123.s1go.app.ui.main;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.area.ForumAreaFragment;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.ui.hot.HotFragment;
import org.succlz123.s1go.app.ui.my.MyFragment;
import org.succlz123.s1go.app.utils.common.MyUtils;
import org.succlz123.s1go.app.utils.common.ViewUtils;
import org.succlz123.s1go.app.widget.BottomNavigationBar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

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

        BottomNavigationBar bottomNavigationBar = ViewUtils.f(this, R.id.bottom_bar);
        bottomNavigationBar.addItem("节点", R.drawable.ic_panorama_fish_eye_white_48dp);
        bottomNavigationBar.addItem("热帖", R.drawable.ic_hdr_weak_white_48dp);
        bottomNavigationBar.addItem("我的", R.drawable.ic_all_inclusive_white_48dp);
        bottomNavigationBar.bindViewPager(viewPager);
        bottomNavigationBar.getChildAt(0).performClick();
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
}
