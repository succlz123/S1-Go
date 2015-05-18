package org.succlz123.s1go.app.ui.fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.hostthreads.HotThreadsDate;
import org.succlz123.s1go.app.bean.hostthreads.HotThreadsObject;
import org.succlz123.s1go.app.dao.interaction.GetHostThreads;
import org.succlz123.s1go.app.dao.helper.S1Fid;

import java.util.HashMap;


/**
 * Created by fashi on 2015/4/12.
 */
public class HotThreadsFragment extends Fragment {
    private ListView mListView;
    private HotThreadsObject hotThreadsObject;
    private View mView;
    private MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.hotpost_fragment, container, false);
        initViews();
        new GetHostPostAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return mView;
    }

    private void initViews() {
        mListView = (ListView) mView.findViewById(R.id.hostpost_listview);
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (hotThreadsObject != null) {
                return hotThreadsObject.getVariables().getHotPostDateList().size();
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

        private class ViewHolder {
            private TextView mTitle;
            private TextView mFid;
            private TextView mName;
            private TextView mTime;
            private TextView mLastPoster;
            private TextView mLastTime;
            private TextView mViews;
            private TextView mReply;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.hotpost_listview_item, parent, false);
                mHolder = new ViewHolder();
                mHolder.mTitle = (TextView) convertView.findViewById(R.id.hotpost_listview_title);
                mHolder.mFid = (TextView) convertView.findViewById(R.id.hotpost_listview_fid);
                mHolder.mName = (TextView) convertView.findViewById(R.id.hotpost_listview_name);
                mHolder.mTime = (TextView) convertView.findViewById(R.id.hotpost_listview_time);
                mHolder.mLastPoster = (TextView) convertView.findViewById(R.id.hotpost_listview_last_poster);
                mHolder.mLastTime = (TextView) convertView.findViewById(R.id.hotpost_listview_last_post_time);
                mHolder.mViews = (TextView) convertView.findViewById(R.id.hotpost_listview_Views);
                mHolder.mReply = (TextView) convertView.findViewById(R.id.hotpost_listview_reply);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            HotThreadsDate mHotThreadsDate = hotThreadsObject.getVariables().getHotPostDateList().get(position);
            mHolder.mTitle.setText(mHotThreadsDate.getSubject());

            mHolder.mFid.setText("[" + S1Fid.GetS1Fid(mHotThreadsDate.getFid()) + "]");
            mHolder.mName.setText(mHotThreadsDate.getAuthor());
            mHolder.mTime.setText(Html.fromHtml(mHotThreadsDate.getDateline()));
            mHolder.mLastPoster.setText(mHotThreadsDate.getLastposter());
            mHolder.mLastTime.setText(Html.fromHtml(mHotThreadsDate.getLastpost()));
            mHolder.mViews.setText(mHotThreadsDate.getViews());
            mHolder.mReply.setText(mHotThreadsDate.getReplies());

            return convertView;
        }
    }

    private class GetHostPostAsyncTask extends AsyncTask<Void, Void, HotThreadsObject> {
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
        protected HotThreadsObject doInBackground(Void... params) {

            HotThreadsObject result = GetHostThreads.getHostPost(hearders);
            return result;
        }

        @Override
        protected void onPostExecute(HotThreadsObject aVoid) {
            super.onPostExecute(aVoid);
            HotThreadsFragment.this.hotThreadsObject = aVoid;
            myAdapter = new MyAdapter();
            mListView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        }


    }
}
