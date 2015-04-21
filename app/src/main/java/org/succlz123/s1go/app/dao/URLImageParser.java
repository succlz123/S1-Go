package org.succlz123.s1go.app.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.util.LruCache;
import android.widget.TextView;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.dao.Api.GetBmApi;
import org.succlz123.s1go.app.dao.Helper.S1FidImgHelper;


/**
 * Created by fashi on 2015/4/15.
 */
public class URLImageParser implements Html.ImageGetter {
    private static LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
    private TextView textView;
    private String content;

    public URLImageParser(TextView textView, String content) {
        this.textView = textView;
        this.content = content;
    }

    @Override
    public Drawable getDrawable(String source) {
        Log.v("source", source);
        if (source.startsWith("static/image/smiley/")) {
            String url = source.substring("static/image/smiley/".length());
            String type = url.substring(0, url.indexOf("/"));
            String name = url.substring(url.indexOf("/") + 1, url.indexOf("."));
            Bitmap bitmap = S1FidImgHelper.getBitmap(name);
            BitmapDrawable drawable = new BitmapDrawable(this.textView.getResources(), bitmap);
            drawable.setBounds(0, 0, 60, 60);
            return drawable;
        }

        Bitmap bitmap = lruCache.get(source);
        if (bitmap != null) {
            BitmapDrawable drawable = new BitmapDrawable(this.textView.getResources(), bitmap);
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            return drawable;
        }
        new GetBitmapAsyncTask(source).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//        ImageLoader.getInstance().loadImage(source, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                lruCache.put(source, loadedImage);
//                textView.setText(Html.fromHtml(content, new URLImageParser(textView, content), null));
//            }
//        });
        return null;
    }


    private class GetBitmapAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        private String source;

        public GetBitmapAsyncTask(String source) {
            this.source = source;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            return GetBmApi.getBitMap(source);
        }

        @Override
        protected void onPostExecute(Bitmap aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid != null) {
                lruCache.put(source, aVoid);
            } else if (aVoid == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(textView.getResources(), R.drawable.no);
                bitmap = Bitmap.createScaledBitmap(bitmap, 130, 130, true);
                lruCache.put(source, bitmap);
            }
            textView.setText(Html.fromHtml(content, new URLImageParser(textView, content), null));
        }
    }


}