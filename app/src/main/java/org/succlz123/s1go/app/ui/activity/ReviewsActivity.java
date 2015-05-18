package org.succlz123.s1go.app.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.melnykov.fab.FloatingActionButton;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.reviews.ReviewsList;
import org.succlz123.s1go.app.bean.reviews.ReviewsObject;
import org.succlz123.s1go.app.dao.api.ConvertUidToAvatarUrl;
import org.succlz123.s1go.app.dao.interaction.GetReviews;
import org.succlz123.s1go.app.dao.api.ImageLinkParser;
import org.succlz123.s1go.app.dao.api.UrlImageParser;
import org.succlz123.s1go.app.support.AppSize;
import org.succlz123.s1go.app.support.com.kaiguan.swingindicator.SwingIndicator;
import org.succlz123.s1go.app.support.imageloader.ImageDownLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fashi on 2015/4/15.
 */
public class ReviewsActivity extends ActionBarActivity {
    private String mTid;
    private String mToolbarTitle;
    private ListView mListView;
    private ReviewsObject reviewsObject;
    private AppAdapet mApdater;
    private List<ReviewsList> mReviewsList;
    private Toolbar mToolbar;
    private SwingIndicator mSwingIndicator;
    private LruCache<String, Bitmap> mImageCache;
    private AppSize mAppSize;

    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews_activity);
        mTid = getIntent().getStringExtra("tid");
        mToolbarTitle = getIntent().getStringExtra("title");
        initViews();
        setToolbar();
        setFloatingActionButton();
        mImageCache = new LruCache<String, Bitmap>(70);
        if (mTid != null) {
            new GetReviewsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.reviews_fab);
        mSwingIndicator = (SwingIndicator) findViewById(R.id.reviews_progress);
        mListView = (ListView) findViewById(R.id.reviews_activity_listview);
    }

    private void setToolbar() {
        mToolbar.setTitle(mToolbarTitle);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFloatingActionButton() {
        mFloatingActionButton.setShadow(true);
        mFloatingActionButton.setType(FloatingActionButton.TYPE_NORMAL);
        mFloatingActionButton.setColorNormal(getResources().getColor(R.color.base));
        mFloatingActionButton.attachToListView(mListView);//把listview和浮动imagebutton组合
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewsActivity.this, SetReviewsActivity.class);
                intent.putExtra("tid", mTid);
                intent.putExtra("formhash", reviewsObject.getVariables().getFormhash());
                startActivityForResult(intent, 2);
                overridePendingTransition(0, 0);
            }
        });
    }

    private class AppAdapet extends BaseAdapter {

        private class ViewHolder {
            private ImageView mAvatarImg;
            private TextView mName;
            private TextView mTime;
            private TextView mNum;
            private TextView mReviews;
        }

        @Override
        public int getCount() {
            if (reviewsObject != null) {
                return reviewsObject.getVariables().getPostlist().size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.reviews_listview_item, parent, false);
                holder = new ViewHolder();
                holder.mAvatarImg = (ImageView) convertView.findViewById(R.id.reviews_listview_item_img);
                holder.mName = (TextView) convertView.findViewById(R.id.reviews_listview_item_name);
                holder.mTime = (TextView) convertView.findViewById(R.id.reviews_listview_item_time);
                holder.mNum = (TextView) convertView.findViewById(R.id.reviews_listview_item_num);
                holder.mReviews = (TextView) convertView.findViewById(R.id.reviews_listview_item_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            mReviewsList = new ArrayList<ReviewsList>();
            mReviewsList = reviewsObject.getVariables().getPostlist();
            final String mAvatarUrl = ConvertUidToAvatarUrl.getAvatar(mReviewsList.get(position).getAuthorid());
            holder.mAvatarImg.setImageResource(R.drawable.noavatar);

//            holder.mAvatarImg.setTag(mAvatarUrl);

            if (mAppSize != null) {
                final ViewHolder finalHolder = holder;
                ImageDownLoader.getInstance().loadBitmap(mAvatarUrl, mAppSize, new ImageDownLoader.CallBack() {
                    @Override
                    public void onLoad(String url, Bitmap bitmap) {
//                        if (mAvatarUrl.equals(finalHolder.mAvatarImg.getTag())) {
                        finalHolder.mAvatarImg.setImageBitmap(bitmap);
//                        }
                    }

                    @Override
                    public void onError(String url) {
//                        if (mAvatarUrl.equals(finalHolder.mAvatarImg.getTag())) {
                        finalHolder.mAvatarImg.setImageResource(R.drawable.noavatar);
//                        }
                    }
                });
            }
            holder.mName.setText(mReviewsList.get(position).getAuthor());
            holder.mTime.setText(Html.fromHtml(mReviewsList.get(position).getDateline()));
            if (position == 0) {
                holder.mNum.setText("楼主");
            } else if (position > 0) {
                holder.mNum.setText("" + position + "楼");
            }
            holder.mReviews.setText(Html.fromHtml(mReviewsList.get(position).getMessage(), new UrlImageParser(holder.mReviews, mReviewsList.get(position).getMessage(), mAppSize), null));

            holder.mReviews.setMovementMethod(ImageLinkParser.getInstance());

            return convertView;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        mAppSize = new AppSize(screenWidth, screenHeight);
        Log.e("ReviewsActivity W_H", mAppSize.getWidth() + "_" + mAppSize.getHeight());
    }

    private class GetReviewsAsyncTask extends AsyncTask<Void, Void, ReviewsObject> {
        private HashMap<String, String> hearders = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (S1GoApplication.getInstance().getUserInfo() == null) {
            } else {
                String cookie = S1GoApplication.getInstance().getUserInfo().getCookiepre();
                String auth = "auth=" + Uri.encode(S1GoApplication.getInstance().getUserInfo().getAuth());
                String saltkey = "saltkey=" + S1GoApplication.getInstance().getUserInfo().getSaltkey();
                this.hearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
            }
        }

        @Override
        protected ReviewsObject doInBackground(Void... params) {

            return GetReviews.getReviews(mTid, hearders);
        }

        @Override
        protected void onPostExecute(ReviewsObject aVoid) {
            super.onPostExecute(aVoid);
            reviewsObject = aVoid;
            mApdater = new AppAdapet();
            mListView.setAdapter(mApdater);
            mApdater.notifyDataSetChanged();
            mSwingIndicator.setVisibility(View.GONE);
            mFloatingActionButton.setVisibility(View.VISIBLE);
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
