package org.succlz123.s1go.app.utils.fromhtml;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.utils.common.MyUtils;
import org.succlz123.s1go.app.utils.s1.S1Emoticon;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by succlz123 on 2015/4/15.
 */
public class SpannedImageGetter implements Html.ImageGetter {
    private WeakReference<TextView> mTextViewWeakReference;
    private int mEmotionSize;

    public SpannedImageGetter(TextView textView) {
        mTextViewWeakReference = new WeakReference<>(textView);
        mEmotionSize = MyUtils.dip2px(20);
    }

    @Override
    public Drawable getDrawable(String source) {
        if (source.startsWith("static/image/smiley/")) {
            String url = source.substring("static/image/smiley/".length());
            String type = url.substring(0, url.indexOf("/"));
            String name = url.substring(url.indexOf("/") + 1, url.indexOf("."));
            Bitmap bitmap = S1Emoticon.getEmoticon(name);
            BitmapDrawable drawable = new BitmapDrawable(MainApplication.getInstance().getResources(), bitmap);
            drawable.setBounds(0, 0, mEmotionSize, mEmotionSize);
            return drawable;
        }

        final UrlDrawable urlDrawable = new UrlDrawable(mEmotionSize * 5);
        Drawable drawable = ContextCompat.getDrawable(MainApplication.getInstance().getApplicationContext(), R.drawable.ic_downloading);
        urlDrawable.setActualDrawable(drawable);
        urlDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(source)).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, MainApplication.getInstance().getResources());
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher);
                }
                refresh(urlDrawable, bitmap);
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher);
                refresh(urlDrawable, bitmap);
            }
        }, CallerThreadExecutor.getInstance());
        return urlDrawable;
    }

    private void refresh(UrlDrawable urlDrawable, Bitmap bitmap) {
        final TextView tvShow = mTextViewWeakReference.get();
        if (tvShow == null) {
            return;
        }
        BitmapDrawable drawable = new BitmapDrawable(MainApplication.getInstance().getResources(), bitmap);
        urlDrawable.setActualDrawable(drawable);
        urlDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        MainApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvShow.setText(tvShow.getText());
                tvShow.invalidate();
            }
        });
    }
}