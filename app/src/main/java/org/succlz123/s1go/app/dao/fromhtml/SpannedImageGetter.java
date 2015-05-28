package org.succlz123.s1go.app.dao.fromhtml;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;
import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.dao.helper.S1FidIcon;
import org.succlz123.s1go.app.support.utils.AppSize;


/**
 * Created by fashi on 2015/4/15.
 */
public class SpannedImageGetter implements Html.ImageGetter {
	private TextView mTextView;
	private AppSize mAppSize;

	public SpannedImageGetter(TextView textView, AppSize appSize) {
		this.mTextView = textView;
		this.mAppSize = appSize;
	}

	@Override
	public Drawable getDrawable(String source) {
		if (source.startsWith("static/image/smiley/")) {
			String url = source.substring("static/image/smiley/".length());
			String type = url.substring(0, url.indexOf("/"));
			String name = url.substring(url.indexOf("/") + 1, url.indexOf("."));
			Bitmap bitmap = S1FidIcon.getBitmap(name);
			BitmapDrawable drawable = new BitmapDrawable(MyApplication.getInstance().getResources(), bitmap);
			drawable.setBounds(0, 0, 60, 60);
			return drawable;
		} else if (source.startsWith("http")) {
			final UrlDrawable urlDrawable = new UrlDrawable();
			Drawable drawable = MyApplication.getInstance().getResources().getDrawable(R.drawable.downloading, null);
 //			urlDrawable.setActualDrawable(drawable);
//			drawable.setBounds(0, 0, 200, 200);

//
//			ImageDownLoader.getInstance().loadBitmap(source, mAppSize, new ImageDownLoader.CallBack() {
//				@Override
//				public void onLoad(String url, Bitmap bitmap) {
//					BitmapDrawable drawable = new BitmapDrawable(MyApplication.getInstance().getResources(), bitmap);
//					drawable.setBounds(0,0, 90, 90);
//					urlDrawable.setActualDrawable(drawable);
//					mTextView.invalidate();
//				}
//
//				@Override
//				public void onError(String url) {
//					Bitmap bitmap = BitmapFactory.decodeResource(mTextView.getResources(), R.drawable.no);
//					bitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, true);
//				}
//			});
			return urlDrawable;
		}
		return null;
	}
}