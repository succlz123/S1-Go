package org.succlz123.s1go.app.utils.fromhtml;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.utils.PicHelper;
import org.succlz123.s1go.app.utils.common.MyUtils;
import org.succlz123.s1go.app.utils.network.NetworkManager;
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

import static org.succlz123.s1go.app.utils.PicHelper.PIC_NOT_DISPLAY;
import static org.succlz123.s1go.app.utils.PicHelper.PIC_WIFI;
import static org.succlz123.s1go.app.utils.network.NetworkManager.NETWORK_TYPE_WIFI;

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

        int displayType = PicHelper.getPicType(MainApplication.getInstance().getApplicationContext());
        if ((displayType == PIC_WIFI) && (NetworkManager.getNetworkType(MainApplication.getInstance().getApplicationContext()) != NETWORK_TYPE_WIFI)) {
            return urlDrawable;
        }

        if (displayType == PIC_NOT_DISPLAY) {
            return null;
        }

        ImageRequest req = ImageRequestBuilder.newBuilderWithSource(Uri.parse(source))
                .setResizeOptions(new ResizeOptions(400, 600))
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(true)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(req, MainApplication.getInstance().getResources());
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
        if (bitmap == null) {
            return;
        }
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
            }
        });
    }
}