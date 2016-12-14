package org.succlz123.s1go.app.widget;

import org.succlz123.s1go.app.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by succlz123 on 16/8/21.
 */
public class BottomNavigationBar extends LinearLayout {
    private ViewPager mViewPager;

    public BottomNavigationBar(Context context) {
        this(context, null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.color.theme_color_view_background);
        setOrientation(HORIZONTAL);
    }

    public void addItem(String title, int drawableResId) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.layout_bottom_navigation_item, this, false);

        ImageView imgIcon = (ImageView) itemView.findViewById(R.id.icon);
        TextView tvTitle = (TextView) itemView.findViewById(R.id.title);

        imgIcon.setBackgroundResource(drawableResId);
        tvTitle.setText(title);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager == null) {
                    return;
                }
                int currentPosition = mViewPager.getCurrentItem();

                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    TextView tvTitle = (TextView) v.findViewById(R.id.title);
                    if (v == view) {
                        tvTitle.setVisibility(View.VISIBLE);
                        v.setSelected(true);
                        if (i == currentPosition) {
                            continue;
                        }
                        mViewPager.setCurrentItem(i);
                    } else {
                        tvTitle.setVisibility(View.GONE);
                        v.setSelected(false);
                    }
                }
            }
        });
        addView(itemView);
    }

    public void bindViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int currentPosition = mViewPager.getCurrentItem();
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    TextView tvTitle = (TextView) v.findViewById(R.id.title);
                    if (i == currentPosition) {
                        tvTitle.setVisibility(View.VISIBLE);
                        v.setSelected(true);
                    } else {
                        tvTitle.setVisibility(View.GONE);
                        v.setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
