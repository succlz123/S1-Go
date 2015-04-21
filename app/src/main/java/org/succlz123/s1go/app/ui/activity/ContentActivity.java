package org.succlz123.s1go.app.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.*;
import com.melnykov.fab.FloatingActionButton;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.thread.ThreadObject;
import org.succlz123.s1go.app.bean.thread.ThreadPostlist;
import org.succlz123.s1go.app.dao.Api.GetAvatarApi;
import org.succlz123.s1go.app.dao.Api.ThreadContentApi;
import org.succlz123.s1go.app.dao.ImageLinkParser;
import org.succlz123.s1go.app.dao.URLImageParser;
import org.succlz123.s1go.app.support.swingindicator.SwingIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fashi on 2015/4/15.
 */
public class ContentActivity extends ActionBarActivity {
    private ListView mListView;
    private String tid;
    private ThreadObject threadObject;
    private AppAdapet mApdater;
    private List<ThreadPostlist> list;
    private Toolbar mToolbar;
    private SwingIndicator swingIndicator;
    private String ToolbarTitle;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);
        tid = getIntent().getStringExtra("tid");
        ToolbarTitle = getIntent().getStringExtra("title");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(ToolbarTitle);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.setreviews_activity, null);
        final PopupWindow pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);

//                Button post = (Button) findViewById(R.id.reviews_post);

        pop.setFocusable(true); //设置PopupWindow可获得焦点
//                pop.setWidth();
//        pop.setHeight(395);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(false);
//                pop.setTouchable(false); //设置PopupWindow可触摸
//                pop.setOutsideTouchable(true); //设置非PopupWindow区域可触摸

        floatingActionButton = (FloatingActionButton) findViewById(R.id.content_fab);
        floatingActionButton.setShadow(true);
        floatingActionButton.setType(FloatingActionButton.TYPE_NORMAL);
        floatingActionButton.setColorNormal(getResources().getColor(R.color.base));
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.reviews_layout);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ContentActivity.this, SetReviewsActivity.class);
//                intent.putExtra("tid", tid);
//                intent.putExtra("formhash", threadObject.getVariables().getFormhash());
//                startActivityForResult(intent, 2);
//                overridePendingTransition(0,0);
//                relativeLayout.setVisibility(View.VISIBLE);


//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (pop.isShowing()) {
//                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
//                    pop.dismiss();
//                } else {
//                    // 显示窗口
//                    pop.showAsDropDown(v);
//                }
//            }
//        });
//                pop.sho
                pop.showAsDropDown(view);
            }
        });


        swingIndicator = (SwingIndicator) findViewById(R.id.content_progress);
        if (tid != null) {
            mListView = (ListView) findViewById(R.id.content_base_activity_listview);
            new GetThreadsContentAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            floatingActionButton.attachToListView(mListView);//把listview和浮动imagebutton组合
        }
    }


    private class AppAdapet extends BaseAdapter {

        @Override
        public int getCount() {
            if (threadObject != null) {
                return threadObject.getVariables().getPostlist().size();
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
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.content_listview_item, parent, false);
            }

            ImageView img = (ImageView) convertView.findViewById(R.id.content_listview_item_img);
            TextView name = (TextView) convertView.findViewById(R.id.content_listview_item_name);
            TextView time = (TextView) convertView.findViewById(R.id.content_listview_item_time);
            TextView num = (TextView) convertView.findViewById(R.id.content_listview_item_num);
            TextView content = (TextView) convertView.findViewById(R.id.content_listview_item_content);

            list = new ArrayList<ThreadPostlist>();
            list = threadObject.getVariables().getPostlist();

//            String url = S1UrlHelper.USER_AVATAR.replace("uid=", "uid=" + list.get(position).getAuthorid());;
//            ImageLoader.getInstance().displayImage(url,img);

            img.setImageBitmap(null);

            new GetAvatarApi.GetAvatar(list.get(position).getAuthorid(), img).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            name.setText(list.get(position).getAuthor());
            time.setText(Html.fromHtml(list.get(position).getDateline()));
            if (position == 0) {
                num.setText("楼主");
            } else if (position > 0) {
                num.setText("" + position + "楼");
            }

            content.setText(Html.fromHtml(list.get(position).getMessage(), new URLImageParser(content, list.get(position).getMessage()), null));

            content.setMovementMethod(ImageLinkParser.getInstance());

            return convertView;
        }
    }

    private class GetThreadsContentAsyncTask extends AsyncTask<Void, Void, ThreadObject> {
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
        protected ThreadObject doInBackground(Void... params) {

            return ThreadContentApi.getThreadContent(tid, hearders);
        }

        @Override
        protected void onPostExecute(ThreadObject aVoid) {
            super.onPostExecute(aVoid);
            threadObject = aVoid;
            mApdater = new AppAdapet();
            mListView.setAdapter(mApdater);
            mApdater.notifyDataSetChanged();
            swingIndicator.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
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
