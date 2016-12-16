package org.succlz123.s1go.app.utils.image;

import com.facebook.common.internal.Preconditions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ForwardingDrawable;
import com.facebook.drawee.drawable.Rounded;
import com.facebook.drawee.drawable.RoundedBitmapDrawable;
import com.facebook.drawee.drawable.RoundedColorDrawable;
import com.facebook.drawee.drawable.ScaleTypeDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.GenericDraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.succlz123.s1go.app.BuildConfig;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by succlz123 on 16/4/28.
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private ImagePipelineConfig mConfig;

    private static ImageLoader sInstance;

    public static ImageLoader getInstance() {
        return sInstance;
    }

    public static void init() {
        if (sInstance == null) {
            sInstance = new ImageLoader();
        }
    }

    public static String resourceToUri(int resId) {
        return "res://" + BuildConfig.APPLICATION_ID + "/" + resId;
    }

    public String assetToUri(String path) {
        return "asset://android_asset/" + path;
    }

    public void displayImage(@DrawableRes int resId, ImageView imageView) {
        if (!(imageView instanceof SimpleDraweeView)) {
            imageView.setImageResource(resId);
        } else {
            displayImage(resourceToUri(resId), imageView);
        }
    }

    public void displayImage(String imageUri, ImageView imageView) {
        if (TextUtils.isEmpty(imageUri)) {
            return;
        }
        displayImage(Uri.parse(imageUri), imageView);
    }

    public void displayImage(Uri imageUri, ImageView imageView) {
        if (!(imageView instanceof GenericDraweeView)) {
            throw new IllegalAccessError("please use fresco image view");
        }
        if (imageUri == null) {
            return;
        }
        imageView.setImageURI(imageUri);
    }

    public void displayImageWithResizing(Uri imageUri, int width, int height, ImageView imageView) {
        if (!(imageView instanceof GenericDraweeView)) {
            throw new IllegalAccessError("please use fresco image view");
        }
        if (imageUri == null) {
            return;
        }
        ImageRequest req = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(true)
                .build();

        GenericDraweeView v = (GenericDraweeView) imageView;

        DraweeController ctrl = Fresco.newDraweeControllerBuilder()
                .setOldController(v.getController())
                .setImageRequest(req)
                .build();

        v.setController(ctrl);
    }

    public void displayImageWithGif(String url, ImageView view) {
        if (view != null && view instanceof GenericDraweeView && !TextUtils.isEmpty(url)) {
            GenericDraweeView draweeView = (GenericDraweeView) view;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(draweeView.getController())
                    .setAutoPlayAnimations(true)
                    .build();
            draweeView.setController(controller);
        }
    }

    public static BitmapDrawable createBitmapDrawable(Context context, Bitmap bitmap) {
        BitmapDrawable drawable;
        if (context != null) {
            drawable = new BitmapDrawable(context.getResources(), bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    && drawable.canApplyTheme()) {
                drawable.applyTheme(context.getTheme());
            }
        } else {
            drawable = new BitmapDrawable(null, bitmap);
        }
        return drawable;
    }

    public void displayImage(String imageUri, ImageView imageView, ImageLoadingListener listener) {
        if (TextUtils.isEmpty(imageUri)) return;

        if (imageView instanceof GenericDraweeView) {
            if (listener != null) {
                PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
                ImageLoaderListener listener1 = new ImageLoaderListener();
                listener1.setData(listener, imageUri, imageView);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUri)).build();
                AbstractDraweeController controller = builder.setOldController(((GenericDraweeView) imageView).getController()).setControllerListener(listener1).setImageRequest(request).build();
                ((GenericDraweeView) imageView).setController(controller);
                return;
            }
        }
        imageView.setImageURI(Uri.parse(imageUri));
    }

    public void shutDown() {
        sInstance = null;
    }

    public static Drawable applyRoundingBitmapOnly(
            @Nullable RoundingParams roundingParams,
            Resources resources,
            Drawable drawable) {
        if (roundingParams == null ||
                roundingParams.getRoundingMethod() != RoundingParams.RoundingMethod.BITMAP_ONLY) {
            return drawable;
        }

        if (drawable instanceof BitmapDrawable || drawable instanceof ColorDrawable) {
            return applyRounding(roundingParams, resources, drawable);
        } else {
            Drawable parent = drawable;
            Drawable child = parent.getCurrent();
            while (child != null && parent != child) {
                if (parent instanceof ForwardingDrawable &&
                        (child instanceof BitmapDrawable || child instanceof ColorDrawable)) {
                    ((ForwardingDrawable) parent).setCurrent(
                            applyRounding(roundingParams, resources, child));
                }
                parent = child;
                child = parent.getCurrent();
            }
        }
        return drawable;
    }

    public static Drawable applyRounding(
            @Nullable RoundingParams roundingParams,
            Resources resources,
            Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            RoundedBitmapDrawable roundedBitmapDrawable =
                    RoundedBitmapDrawable.fromBitmapDrawable(resources, (BitmapDrawable) drawable);
            applyRoundingParams(roundedBitmapDrawable, roundingParams);
            return roundedBitmapDrawable;
        }
        if (drawable instanceof ColorDrawable &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RoundedColorDrawable roundedColorDrawable =
                    RoundedColorDrawable.fromColorDrawable((ColorDrawable) drawable);
            applyRoundingParams(roundedColorDrawable, roundingParams);
            return roundedColorDrawable;
        }
        return drawable;
    }

    private static void applyRoundingParams(Rounded rounded, RoundingParams roundingParams) {
        rounded.setCircle(roundingParams.getRoundAsCircle());
        rounded.setRadii(roundingParams.getCornersRadii());
        rounded.setBorder(
                roundingParams.getBorderColor(),
                roundingParams.getBorderWidth());
    }

    public static Drawable wrapWithScaleType(
            Drawable drawable,
            @Nullable ScalingUtils.ScaleType scaleType) {
        Preconditions.checkNotNull(drawable);
        if (scaleType == null) {
            return drawable;
        }
        return new ScaleTypeDrawable(drawable, scaleType);
    }

    private class ImageLoaderListener extends BaseControllerListener<ImageInfo> {

        private ImageLoadingListener mLoadingListener;
        private WeakReference<ImageView> mImageView;
        private String mImageUri;

        public void setData(ImageLoadingListener listener, String imageUri, ImageView imageView) {
            mLoadingListener = listener;
            mImageUri = imageUri;
            mImageView = new WeakReference<>(imageView);
        }

        @Override
        public void onSubmit(String id, Object callerContext) {
            super.onSubmit(id, callerContext);
            if (mLoadingListener != null) {
                mLoadingListener.onLoadingStarted(mImageUri, mImageView.get());
            }
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            if (mLoadingListener != null && mImageView != null) {
                ImageView imageView = mImageView.get();
                if (imageInfo != null) {
                    if (imageInfo instanceof CloseableBitmap) {
                        mLoadingListener.onLoadingComplete(mImageUri, imageView, ((CloseableBitmap) imageInfo).getUnderlyingBitmap());
                        return;
                    }
                }
                mLoadingListener.onLoadingComplete(mImageUri, imageView, null);
            }
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            if (mLoadingListener != null) {
                mLoadingListener.onLoadingFailed(mImageUri, mImageView.get(), throwable != null ? throwable.getMessage() : "");
            }
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
            super.onIntermediateImageFailed(id, throwable);
            // TODO mLoadingListener.onLoadingCancelled();
        }

        @Override
        public void onRelease(String id) {
            super.onRelease(id);
        }
    }

    public void clearMemoryCache() {
        try {
            Fresco.getImagePipeline().clearMemoryCaches();
        } catch (Exception ignored) {
        }
        System.gc();
    }
}
