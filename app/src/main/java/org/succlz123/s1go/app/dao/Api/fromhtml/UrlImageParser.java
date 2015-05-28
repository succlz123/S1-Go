package org.succlz123.s1go.app.dao.api.fromhtml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.dao.helper.S1FidIcon;
import org.succlz123.s1go.app.support.imageloader.ImageDownLoader;
import org.succlz123.s1go.app.support.utils.AppSize;


/**
 * Created by fashi on 2015/4/15.
 */
public class UrlImageParser implements Html.ImageGetter {
	//    private static LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(40);
	private TextView textView;
	//    private String content;
	private AppSize appSize;
	private Context context;
	public Spanned spanned;

	public UrlImageParser(TextView textView,Context context, AppSize appSize) {
		this.textView = textView;
//        this.content = content;
		this.context=context;
		this.appSize = appSize;
	}

	@Override
	public Drawable getDrawable(final String source) {
		Log.v("source", source);
		if (source.startsWith("static/image/smiley/")) {
			String url = source.substring("static/image/smiley/".length());
			String type = url.substring(0, url.indexOf("/"));
			String name = url.substring(url.indexOf("/") + 1, url.indexOf("."));
			Bitmap bitmap = S1FidIcon.getBitmap(name);
			BitmapDrawable drawable = new BitmapDrawable(this.textView.getResources(), bitmap);
			drawable.setBounds(0, 0, 60, 60);
			return drawable;
		}

//        Bitmap bitmap = lruCache.get(source);
//        if (bitmap != null) {
//            BitmapDrawable drawable = new BitmapDrawable(this.textView.getResources(), bitmap);
//            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
//            return drawable;
//        }

		Bitmap bitmap=null;
		ImageDownLoader.getInstance().loadBitmap(source, appSize, new ImageDownLoader.CallBack() {
			@Override
			public void onLoad(String url, Bitmap bitmap) {
//                lruCache.put(source, bitmap);
//				textView.setText("");
//				textView.setText(spanned);
				bitmap=bitmap;
			}

			@Override
			public void onError(String url) {
				Bitmap bitmap = BitmapFactory.decodeResource(textView.getResources(), R.drawable.no);
				bitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, true);
//                lruCache.put(source, bitmap);
//				textView.setText(Html.fromHtml( new UrlImageParser(textView, appSize), null));
			}
		});

		Drawable drawable = this.textView.getResources().getDrawable(R.drawable.downloading, null);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

		return drawable;
	}
}