package org.succlz123.s1go.app.ui.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.dao.Api.GetBmApi;
import org.succlz123.s1go.app.support.com.davemorrissey.labs.subscaleview.ImageSource;
import org.succlz123.s1go.app.support.com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by fashi on 2015/4/17.
 */
public class ImageViewActivity extends ActionBarActivity {
    private ImageView imageView;
    private String url;
    private Toolbar mToolbar;
    private SubsamplingScaleImageView subsamplingScaleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview_activity);
        subsamplingScaleImageView = (SubsamplingScaleImageView) findViewById(R.id.touch_imageview);
        subsamplingScaleImageView.setMinimumScaleType(3);
//        subsamplingScaleImageView.setMinScale(1f);
        subsamplingScaleImageView.setMaxScale(7f);
         mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        url = getIntent().getStringExtra("imageurl");
        new GetImageView(url, subsamplingScaleImageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class GetImageView extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private SubsamplingScaleImageView subsamplingScaleImageView;

        public GetImageView(String url, SubsamplingScaleImageView subsamplingScaleImageView) {
            this.url = url;
            this.subsamplingScaleImageView = subsamplingScaleImageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            return GetBmApi.getBitMap(url);
        }

        @Override
        protected void onPostExecute(Bitmap aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid != null) {
                subsamplingScaleImageView.setImage(ImageSource.bitmap(aVoid));
            } else if (aVoid == null) {
                subsamplingScaleImageView.setImage(ImageSource.resource(R.drawable.tt));
                ;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

