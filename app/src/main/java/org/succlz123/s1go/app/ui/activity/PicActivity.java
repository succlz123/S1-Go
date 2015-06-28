package org.succlz123.s1go.app.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.utils.AppSize;
import org.succlz123.s1go.app.support.utils.ImageLoader;

/**
 * Created by fashi on 2015/4/17.
 */
public class PicActivity extends AppCompatActivity {
	private String mUrl;
	private Toolbar mToolbar;
	private SubsamplingScaleImageView mSubsamplingScaleImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic);
		mSubsamplingScaleImageView = (SubsamplingScaleImageView) findViewById(R.id.touch_imageview);
		mSubsamplingScaleImageView.setMinimumScaleType(3);
//        subsamplingScaleImageView.setMinScale(1f);
		mSubsamplingScaleImageView.setMaxScale(7f);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		//设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

 		mUrl = getIntent().getStringExtra("imageurl");

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;     // 屏幕宽度（像素）
		int height = metric.heightPixels;   // 屏幕高度（像素）
		AppSize appSize = new AppSize(width, height);
		ImageLoader.getInstance().loadBitmap(mUrl, appSize, new ImageLoader.CallBack() {
			@Override
			public void onLoad(String url, Bitmap bitmap) {
				mSubsamplingScaleImageView.setImage(ImageSource.bitmap(bitmap));
			}

			@Override
			public void onError(String url) {
				mSubsamplingScaleImageView.setImage(ImageSource.resource(R.drawable.tt));
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

