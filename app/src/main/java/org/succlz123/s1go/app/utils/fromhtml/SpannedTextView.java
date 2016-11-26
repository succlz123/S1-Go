package org.succlz123.s1go.app.utils.fromhtml;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.MultiDraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.utils.PicHelper;
import org.succlz123.s1go.app.utils.common.MyUtils;
import org.succlz123.s1go.app.utils.network.NetworkManager;
import org.succlz123.s1go.app.utils.s1.S1Emoticon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import static org.succlz123.s1go.app.utils.PicHelper.PIC_NOT_DISPLAY;
import static org.succlz123.s1go.app.utils.PicHelper.PIC_WIFI;
import static org.succlz123.s1go.app.utils.network.NetworkManager.NETWORK_TYPE_WIFI;

/**
 * Created by succlz123 on 2016/11/26.
 */

public class SpannedTextView extends TextView {
    private MultiDraweeHolder<GenericDraweeHierarchy> mMultiDraweeHolder;

    public SpannedTextView(Context context) {
        this(context, null);
    }

    public SpannedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpannedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SpannedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mMultiDraweeHolder = new MultiDraweeHolder<>();
        setMovementMethod(ImageLinkParser.getInstance());
    }

    //
    public void setRichText(String text) {
        Spanned spanned = Html.fromHtml(text, new SpannedImageGetter(getContext(), this, mMultiDraweeHolder), null);
        SpannableStringBuilder spannableStringBuilder;
        if (spanned instanceof SpannableStringBuilder) {
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(spanned);
        }

//        ImageSpan[] imageSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
//        final List<String> imageUrls = new ArrayList<>();

//        for (int i = 0, size = imageSpans.length; i < size; i++) {
//            ImageSpan imageSpan = imageSpans[i];
//            final String imageUrl = imageSpan.getSource();
//            int start = spannableStringBuilder.getSpanStart(imageSpan);
//            int end = spannableStringBuilder.getSpanEnd(imageSpan);
//            imageUrls.add(imageUrl);
//
//            final int position = i;
//            ClickableSpan clickableSpan = new ClickableSpan() {
//                @Override
//                public void onClick(View widget) {
//                    PictureActivity.start(widget.getContext(), imageUrls.get(position));
//                }
//            };
//            ClickableSpan[] clickableSpans = spannableStringBuilder.getSpans(start, end, ClickableSpan.class);
//            if (clickableSpans != null && clickableSpans.length != 0) {
//                for (ClickableSpan cs : clickableSpans) {
//                    spannableStringBuilder.removeSpan(cs);
//                }
//            }
//            spannableStringBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        super.setText(spanned);
    }

    public static class SpannedImageGetter implements Html.ImageGetter {
        private WeakReference<TextView> mTextViewWeakReference;
        private MultiDraweeHolder<GenericDraweeHierarchy> mMultiDraweeHolder;
        private GenericDraweeHierarchy mHierarchy;
        private int mBgColor;

        private int mEmotionSize;

        public SpannedImageGetter(Context context, TextView textView, MultiDraweeHolder<GenericDraweeHierarchy> multiDraweeHolder) {
            mMultiDraweeHolder = multiDraweeHolder;
            mHierarchy = new GenericDraweeHierarchyBuilder(context.getResources()).build();
            mBgColor = ThemeUtils.getColor(context, context.getResources().getColor(R.color.theme_color_window_background));
            mEmotionSize = MyUtils.dip2px(20);
            mTextViewWeakReference = new WeakReference<>(textView);
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

            final UrlDrawable urlDrawable = new UrlDrawable(mBgColor);

            int displayType = PicHelper.getPicType(MainApplication.getInstance().getApplicationContext());
            if ((displayType == PIC_WIFI) && (NetworkManager.getNetworkType(MainApplication.getInstance().getApplicationContext()) != NETWORK_TYPE_WIFI)) {
                return urlDrawable;
            }
            if (displayType == PIC_NOT_DISPLAY) {
                return null;
            }

            final DraweeHolder<GenericDraweeHierarchy> draweeHolder = new DraweeHolder<>(mHierarchy);
            mMultiDraweeHolder.add(draweeHolder);

            ImageRequest req = ImageRequestBuilder.newBuilderWithSource(Uri.parse(source))
                    .setResizeOptions(new ResizeOptions(400, 600))
//                    .setLocalThumbnailPreviewsEnabled(true)
//                    .setProgressiveRenderingEnabled(true)
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(req)
                    .setOldController(draweeHolder.getController())
                    .setControllerListener(new ControllerListener<ImageInfo>() {
                        @Override
                        public void onSubmit(String id, Object callerContext) {
                        }

                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            final Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                            drawable.setBounds(0, 0, imageInfo.getWidth(), imageInfo.getHeight());
                            urlDrawable.setBounds(0, 0, imageInfo.getWidth(), imageInfo.getHeight());
//                            urlDrawable.setDrawable(drawable);
                            MainApplication.getInstance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView textView = mTextViewWeakReference.get();
                                    if (textView == null) {
                                        return;
                                    }
                                    textView.setText(textView.getText());
                                }
                            });
                            Log.i("RichText", "onFinalImageSet width:" + imageInfo.getWidth() + ",height:" + imageInfo.getHeight());
                        }

                        @Override
                        public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                            Log.i("RichText", "onIntermediateImageSet width:" + imageInfo.getWidth() + ",height:" + imageInfo.getHeight());
                        }

                        @Override
                        public void onIntermediateImageFailed(String id, Throwable throwable) {
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
//
                        }

                        @Override
                        public void onRelease(String id) {

                        }
                    })
                    .build();
            draweeHolder.setController(controller);

//            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(source)).build();
//            ImagePipeline imagePipeline = Fresco.getImagePipeline();
//            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
//            dataSource.subscribe(new BaseBitmapDataSubscriber() {
//                @Override
//                public void onNewResultImpl(@Nullable final Bitmap bitmap) {
//                    MainApplication.getInstance().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (bitmap == null) {
//                                return;
//                            }
//                            int width = bitmap.getWidth();
//                            int height = bitmap.getHeight();
//                            if (width > 1080) {
//                                width = 720;
//                            }
//
//                            if (height > 1600) {
//                                height = 1080;
//                            }
//                            urlDrawable.setBounds(0, 0, width, height);
//                            urlDrawable.setActualBitmap(bitmap);
//                            setText(getText());
//                        }
//                    });
//                }
//
//                @Override
//                public void onFailureImpl(DataSource dataSource) {
//                }
//            }, CallerThreadExecutor.getInstance());
            return urlDrawable;
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        if (who instanceof UrlDrawable && mMultiDraweeHolder.verifyDrawable((who).getCurrent())) {
            return true;
        }
        return super.verifyDrawable(who);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mMultiDraweeHolder.onDetach();
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        mMultiDraweeHolder.onDetach();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mMultiDraweeHolder.onAttach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        mMultiDraweeHolder.onAttach();
    }
}
