package org.succlz123.s1go.app.utils.html;

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

import org.succlz123.htmlview.image.UrlDrawable;
import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.utils.PicHelper;
import org.succlz123.s1go.app.utils.common.MyUtils;
import org.succlz123.s1go.app.utils.network.NetworkManager;

import java.lang.ref.WeakReference;

import static org.succlz123.s1go.app.utils.PicHelper.PIC_NOT_DISPLAY;
import static org.succlz123.s1go.app.utils.PicHelper.PIC_WIFI;
import static org.succlz123.s1go.app.utils.network.NetworkManager.NETWORK_TYPE_WIFI;

/**
 * Created by succlz123 on 2015/4/15.
 */
public class S1ImageGetter implements Html.ImageGetter {
    private WeakReference<TextView> mTvWeakReference;
    private int mScreenWidth;
    private int mScreenHeight;

    private int mEmotionSize;

    public S1ImageGetter(TextView textView, int screenWidth, int screenHeight) {
        mTvWeakReference = new WeakReference<>(textView);
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mEmotionSize = MyUtils.dip2px(28);
    }

    @Override
    public Drawable getDrawable(String source) {
        int displayType = PicHelper.getPicType(MainApplication.getInstance().getApplicationContext());
        if ((displayType == PIC_WIFI) && (NetworkManager.getNetworkType(MainApplication.getInstance().getApplicationContext()) != NETWORK_TYPE_WIFI)) {
            return null;
        }
        if (displayType == PIC_NOT_DISPLAY) {
            return null;
        }
        final UrlDrawable urlDrawable = new UrlDrawable();
        if (source.startsWith("http://static.saraba1st.com/image/smiley/")) {
            urlDrawable.setBounds(0, 0, mEmotionSize, mEmotionSize);
            handlerHttpImage(urlDrawable, source, mEmotionSize, mEmotionSize);
        } else {
            Drawable drawable = ContextCompat.getDrawable(MainApplication.getInstance(), R.drawable.ic_downloading);
            urlDrawable.setActualDrawable(drawable);
            urlDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            handlerHttpImage(urlDrawable, source, mScreenWidth / 5 * 3, mScreenHeight / 5 * 3);
        }
        return urlDrawable;
    }

    private void handlerHttpImage(final UrlDrawable urlDrawable, String source, final int width, final int height) {
        ImageRequest req = ImageRequestBuilder.newBuilderWithSource(Uri.parse(source))
                .setResizeOptions(new ResizeOptions(width, height))
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
                refresh(urlDrawable, bitmap, width, height);
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher);
                refresh(urlDrawable, bitmap, width, height);
            }
        }, CallerThreadExecutor.getInstance());
    }

    private void refresh(UrlDrawable urlDrawable, Bitmap bitmap, int width, int height) {
        if (bitmap == null || mTvWeakReference == null) {
            return;
        }
        final TextView tvShow = mTvWeakReference.get();
        if (tvShow == null) {
            return;
        }
        BitmapDrawable drawable = new BitmapDrawable(MainApplication.getInstance().getResources(), bitmap);
        urlDrawable.setActualDrawable(drawable);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float scale = getScale(bitmapWidth, bitmapHeight, width, height);
        urlDrawable.setBounds(0, 0, (int) (bitmapWidth / scale), (int) (bitmapHeight / scale));
        MainApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvShow.setText(tvShow.getText());
            }
        });
    }

    private float getScale(int sourceWidth, int sourceHeight, int limitWidth, int limitHeight) {
        float w = (float) sourceWidth / limitWidth;
        float h = (float) sourceHeight / limitHeight;
        if (w > h) {
            return w;
        } else {
            return h;
        }
    }
}