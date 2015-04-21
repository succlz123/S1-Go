package org.succlz123.s1go.app.ui.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.setreviews.SetReviewsObject;
import org.succlz123.s1go.app.dao.Api.SetReviewsApi;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by fashi on 2015/4/19.
 */
public class SetReviewsActivity extends Activity {
    private EditText contentEdit;
    private String tid;
    private Button post;
    private String formhash;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setreviews_activity);
        tid = getIntent().getStringExtra("tid");
        formhash = getIntent().getStringExtra("formhash");

        contentEdit = (EditText) findViewById(R.id.setthreads_content);
        post = (Button) findViewById(R.id.reviews_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEdit.getText().toString();
                if (!content.isEmpty()) {
                    if (content.length() < 2) {
                        Toast.makeText(SetReviewsActivity.this, "正文字数过少", Toast.LENGTH_SHORT).show();
                    } else {
                        new SetReviewstAsyncTask(tid, content).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                } else if (content.isEmpty()) {
                    Toast.makeText(SetReviewsActivity.this, "请输入正文", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class SetReviewstAsyncTask extends AsyncTask<Void, Void, SetReviewsObject> {
        private HashMap<String, String> hearders = new HashMap<String, String>();
        private LinkedHashMap<String, String> body = new LinkedHashMap<String, String>();
        private String tid;

        private String title;
        private String contentEdit;

        public SetReviewstAsyncTask(String tid, String contentEdit) {
            super();
            this.contentEdit = contentEdit;
            this.tid = tid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (S1GoApplication.getInstance().getUserInfo() == null) {
            } else {
                String cookie = S1GoApplication.getInstance().getUserInfo().getCookiepre();
                String auth = "auth=" + Uri.encode(S1GoApplication.getInstance().getUserInfo().getAuth());
                String saltkey = "saltkey=" + S1GoApplication.getInstance().getUserInfo().getSaltkey();

                String noticetrimstr = "";
                String message = contentEdit;
                String mobiletype = "0";

                this.hearders.put("Cookie", cookie + auth + ";" + cookie + saltkey + ";");
                this.body.put("formhash", formhash);
                this.body.put("noticetrimstr", noticetrimstr);
                this.body.put("message", message);
                this.body.put("mobiletype", mobiletype);
            }
        }

        @Override
        protected SetReviewsObject doInBackground(Void... params) {

            return SetReviewsApi.SetReviews(tid, hearders, body);
        }

        @Override
        protected void onPostExecute(SetReviewsObject aVoid) {
            super.onPostExecute(aVoid);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }


}
