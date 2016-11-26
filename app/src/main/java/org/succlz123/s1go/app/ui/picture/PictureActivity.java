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

import org.qiibeta.bitmapview.BitmapSource;
import org.qiibeta.bitmapview.GestureBitmapView;
import org.succlz123.s1go.app.MainApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.common.FileUtils;
import org.succlz123.s1go.app.utils.common.ToastUtils;
import org.succlz123.s1go.app.utils.common.ViewUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by succlz123 on 2015/4/17.
 */
public class PictureActivity extends BaseToolbarActivity {
    public static final String KEY_IMAGE_URL = "image_url";

    private String mUrl;

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, PictureActivity.class);
        starter.putExtra(KEY_IMAGE_URL, url);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        ensureToolbar();
        showBackButton();
        setTitle("看图");
        mUrl = getIntent().getStringExtra(KEY_IMAGE_URL);

        final GestureBitmapView imageView = ViewUtils.f(this, R.id.img);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mUrl)).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, imageRequest);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (PictureActivity.this.isFinishing() || PictureActivity.this.isDestroyed() || bitmap == null) {
                            return;
                        }
                        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(Uri.parse(mUrl)), this);
                        File file = getCachedImageOnDisk(cacheKey);
                        if (file != null) {
                            imageView.setBitmapSource(BitmapSource.newInstance(file.toString(), bitmap));
                        } else {
//                            imageView.setBitmapSource(BitmapSource.newInstance(mBitmap));
                        }
                        dataSource.close();
                    }
                });
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
            }
        }, CallerThreadExecutor.getInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, 100, 102, "保存图片到/SDCard/Pictures");
        item.setIcon(R.drawable.ic_near_me_white_48dp);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case 100:
                savePic();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUrl = null;
    }

    private void savePic() {
        if (TextUtils.isEmpty(mUrl)) {
            return;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(Uri.parse(mUrl)), MainApplication.getInstance());
        File file = getCachedImageOnDisk(cacheKey);

        File appDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String desc = String.valueOf(mUrl.hashCode()) + mUrl.length();
        String fileName = desc + ".jpg";
        final File newFile = new File(appDir, fileName);

        if (file == null || !file.exists()) {

            final GestureBitmapView imageView = ViewUtils.f(this, R.id.img);
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mUrl)).build();
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (PictureActivity.this.isFinishing() || PictureActivity.this.isDestroyed()) {
                                return;
                            }

                            MainApplication.getInstance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (bitmap != null) {
                                        try {
                                            FileOutputStream fos = new FileOutputStream(newFile);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                            fos.flush();
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            ToastUtils.showToastShort(PictureActivity.this, "保存图片失败");
                                        }
                                    } else {
                                        ToastUtils.showToastShort(PictureActivity.this, "保存图片失败");
                                    }
                                }
                            });
                        }
                    });
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {
                }
            }, CallerThreadExecutor.getInstance());
        } else {
            if (!FileUtils.copyTo(file, newFile)) {
                ToastUtils.showToastShort(this, "保存图片失败");
                return;
            }
        }
        ToastUtils.showToastShort(this, "保存图片成功");
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

