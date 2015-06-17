package org.succlz123.s1go.app.support.biz.fromhtml;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;
import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.utils.S1Emoticon;
import org.succlz123.s1go.app.support.utils.ImageLoader;
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
			Bitmap bitmap = S1Emoticon.getEmoticon(name);
			BitmapDrawable drawable = new BitmapDrawable(MyApplication.getInstance().getResources(), bitmap);
			drawable.setBounds(0, 0, 60, 60);
			return drawable;
		}

		final UrlDrawable urlDrawable = new UrlDrawable();
		Drawable drawable = MyApplication.getInstance().getResources().getDrawable(R.drawable.downloading, null);
		urlDrawable.setActualDrawable(drawable);
		urlDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

		ImageLoader.getInstance().loadBitmap(source, mAppSize, new ImageLoader.CallBack() {
			@Override
			public void onLoad(String url, Bitmap bitmap) {
				BitmapDrawable drawable = new BitmapDrawable(MyApplication.getInstance().getResources(), bitmap);
				urlDrawable.setActualDrawable(drawable);
				urlDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
				mTextView.setText(mTextView.getText());
				mTextView.invalidate();
			}

			@Override
			public void onError(String url) {
				Bitmap bitmap = BitmapFactory.decodeResource(mTextView.getResources(), R.drawable.no);
				bitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, true);
			}
		});
		return urlDrawable;

	}
}