package org.succlz123.s1go.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.forum.ForumForumThreadlist;
import org.succlz123.s1go.app.bean.forum.ForumObject;
import org.succlz123.s1go.app.dao.Api.ForumTitleApi;
import org.succlz123.s1go.app.dao.Helper.S1FidHelper;
import com.melnykov.fab.FloatingActionButton;
import org.succlz123.s1go.app.support.swingindicator.SwingIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fashi on 2015/4/14.
 */
public class ThreadsActivity extends ActionBarActivity {
    private ListView mListView;
    private String fid;
    private ForumObject forumObject;
    private AppAdapet mApdater;
    private Toolbar mToolbar;
    private Boolean isLogin;
    private String ToolbarTitle;
    private List<ForumForumThreadlist> list;
    private SwingIndicator swingIndicator;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threads_base_activity);
        fid = getIntent().getStringExtra("fid");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ToolbarTitle = S1FidHelper.GetS1Fid(Integer.valueOf(fid));
        mToolbar.setTitle(ToolbarTitle);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        swingIndicator = (SwingIndicator) findViewById(R.id.threads_progress);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.thread_fab);
        floatingActionButton.setShadow(true);
        floatingActionButton.setType(FloatingActionButton.TYPE_NORMAL);
        floatingActionButton.setColorNormal(getResources().getColor(R.color.base));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThreadsActivity.this, SetReviewsActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        mListView = (ListView) findViewById(R.id.threads_base_activity_listview);
        floatingActionButton.attachToListView(mListView);//把listview和浮动imagebutton组合
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ThreadsActivity.this, ContentActivity.class);
                intent.putExtra("tid", list.get(position).getTid());
                intent.putExtra("title", list.get(position).getSubject());
                startActivity(intent);
            }
        });
        new GetThreadsTitleAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    private class AppAdapet extends BaseAdapter {

        @Override
        public int getCount() {
            if (forumObject != null) {
                return forumObject.getVariables().getForum_threadlist().size();
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
                convertView = getLayoutInflater().inflate(R.layout.threads_listview_item, parent, false);
            } else {
            }
            TextView title = (TextView) convertView.findViewById(R.id.threads_listview_title);
            TextView name = (TextView) convertView.findViewById(R.id.threads_listview_name);
            TextView time = (TextView) convertView.findViewById(R.id.threads_listview_time);
            TextView lastTime = (TextView) convertView.findViewById(R.id.threads_listview_last_post_time);
            TextView lastPoster = (TextView) convertView.findViewById(R.id.threads_listview_last_poster);
            TextView reply = (TextView) convertView.findViewById(R.id.threads_listview_reply);
            TextView click = (TextView) convertView.findViewById(R.id.threads_listview_click);
            TextView fid = (TextView) convertView.findViewById(R.id.threads_listview_fid);
            list = new ArrayList<ForumForumThreadlist>();
            list = forumObject.getVariables().getForum_threadlist();
            title.setText(list.get(position).getSubject());
            name.setText(list.get(position).getAuthor());
            time.setText(Html.fromHtml(list.get(position).getDateline()));
            lastTime.setText(Html.fromHtml(list.get(position).getLastpost()));
            lastPoster.setText(list.get(position).getLastposter());
            reply.setText(list.get(position).getReplies());
            click.setText(list.get(position).getViews());

            fid.setText(null);

            return convertView;
        }
    }

    private class GetThreadsTitleAsyncTask extends AsyncTask<Void, Void, ForumObject> {

        private HashMap<String, String> paramss = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (S1GoApplication.getInstance().getUserInfo() == null) {
            } else {
                String cookie = S1GoApplication.getInstance().getUserInfo().getCookiepre();
                String auth = "auth=" + Uri.encode(S1GoApplication.getInstance().getUserInfo().getAuth());
                String saltkey = "saltkey=" + S1GoApplication.getInstance().getUserInfo().getSaltkey();
                this.paramss.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
            }
        }

        @Override
        protected ForumObject doInBackground(Void... params) {

            return ForumTitleApi.getForumTitle(fid, paramss);
        }

        @Override
        protected void onPostExecute(ForumObject aVoid) {
            super.onPostExecute(aVoid);
            forumObject = aVoid;
            isLogin = (forumObject != null && forumObject.getMessage() == null);
            if (!isLogin) {
                Toast.makeText(ThreadsActivity.this, "抱歉，您尚未登录，没有权限访问该版块", Toast.LENGTH_LONG).show();
            } else if (isLogin) {
                mApdater = new AppAdapet();
                mListView.setAdapter(mApdater);
                swingIndicator.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                mApdater.notifyDataSetChanged();
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