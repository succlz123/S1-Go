package org.succlz123.s1go.app.utils.fromhtml;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.widget.AppSize;
import org.succlz123.s1go.app.deprecated.ImageLoader;
import org.succlz123.s1go.app.utils.s1.S1Emoticon;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.widget.TextView;

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
            BitmapDrawable drawable = new BitmapDrawable(MainApplication.getInstance().getResources(), bitmap);
            drawable.setBounds(0, 0, 60, 60);
            return drawable;
        }

        final UrlDrawable urlDrawable = new UrlDrawable();
        Drawable drawable = ContextCompat.getDrawable(MainApplication.getInstance().getApplicationContext(), R.drawable.ic_downloading);
        urlDrawable.setActualDrawable(drawable);
        urlDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        ImageLoader.getInstance().loadBitmap(source, mAppSize, new ImageLoader.CallBack() {
            @Override
            public void onLoad(String url, Bitmap bitmap) {
                BitmapDrawable drawable = new BitmapDrawable(MainApplication.getInstance().getResources(), bitmap);
                urlDrawable.setActualDrawable(drawable);
                urlDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mTextView.setText(mTextView.getText());
                mTextView.invalidate();
            }

            @Override
            public void onError(String url) {
//                Bitmap bitmap = BitmapFactory.decodeResource(mTextView.getResources(), R.drawable.no);
//                bitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, true);
            }
        });
        return urlDrawable;

    }
}