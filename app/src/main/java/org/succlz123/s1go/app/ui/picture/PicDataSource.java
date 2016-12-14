package org.succlz123.s1go.app.ui.picture;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.succlz123.s1go.app.utils.common.FileUtils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by succlz123 on 2016/11/28.
 */

public class PicDataSource implements PicContract.DataSource {

    @Override
    public Observable<Object> getBitmap(String url) {
        final Uri uri = Uri.parse(url);
        final ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();
        final ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        Observable<Bitmap> observable1 = Observable.fromEmitter(new Action1<Emitter<Bitmap>>() {
            @Override
            public void call(final Emitter<Bitmap> bitmapEmitter) {
                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                        bitmapEmitter.onNext(bitmap);
//                        dataSource.close();
                        bitmapEmitter.onCompleted();
                    }

                    @Override
                    public void onFailureImpl(DataSource dataSource) {
                        bitmapEmitter.onError(null);
                    }
                }, CallerThreadExecutor.getInstance());
            }
        }, Emitter.BackpressureMode.BUFFER);

        Observable<String> observable2 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
                File file = getCachedImageOnDisk(cacheKey);
                if (file != null) {
                    subscriber.onNext(file.toString());
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(null);
                }
            }
        });

        return Observable.merge(observable1, observable2);
    }

    @Override
    public Observable<Boolean> savePic(String url) {
        final Uri uri = Uri.parse(url);
        final ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();
        final ImagePipeline imagePipeline = Fresco.getImagePipeline();

        File appDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String desc = String.valueOf(url.hashCode()) + url.length();
        String fileName = desc + ".jpg";
        final File newFile = new File(appDir, fileName);

        return Observable.fromEmitter(new Action1<Emitter<Boolean>>() {
            @Override
            public void call(final Emitter<Boolean> bitmapEmitter) {
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
                File file = getCachedImageOnDisk(cacheKey);

                if (file != null && file.exists()) {
                    bitmapEmitter.onNext(FileUtils.copyTo(file, newFile));
                    bitmapEmitter.onCompleted();
                    return;
                }

                DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                        if (bitmap != null) {
                            try {
                                FileOutputStream fos = new FileOutputStream(newFile);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.flush();
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                bitmapEmitter.onNext(false);
                            }
                            bitmapEmitter.onNext(true);
                        } else {
                            bitmapEmitter.onNext(false);
                        }
                        bitmapEmitter.onCompleted();
                    }

                    @Override
                    public void onFailureImpl(DataSource dataSource) {
                        bitmapEmitter.onError(null);
                    }
                }, CallerThreadExecutor.getInstance());
            }
        }, Emitter.BackpressureMode.BUFFER);
    }

    public File getCachedImageOnDisk(CacheKey cacheKey) {
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }
}
