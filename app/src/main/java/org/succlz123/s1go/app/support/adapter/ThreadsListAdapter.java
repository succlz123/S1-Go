package org.succlz123.s1go.app.support.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.bean.threads.ThreadsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fashi on 2015/6/26.
 */
public class ThreadsListAdapter extends BaseAdapter {
	private List<ThreadsList> mThreadsList = new ArrayList<>();

	public List<ThreadsList> getmThreadsList() {
		return mThreadsList;
	}

	public void initmThreadsList(List<ThreadsList> threadsList) {
		this.mThreadsList = threadsList;
	}

	public void setmThreadsList(List<ThreadsList> mThreadsList) {
		this.mThreadsList.addAll(mThreadsList);
	}

	private class ViewHolder {
		private TextView title;
		private TextView name;
		private TextView time;
		private TextView lastTime;
		private TextView lastPoster;
		private TextView reply;
		private TextView click;
		private TextView fid;
	}

	@Override
	public int getCount() {
		if (mThreadsList != null) {
			return mThreadsList.size();
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
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(R.layout.fragment_threads_listview_item4, parent, false);

			holder.title = (TextView) convertView.findViewById(R.id.threads_listview_title);
			holder.name = (TextView) convertView.findViewById(R.id.threads_listview_name);
			holder.time = (TextView) convertView.findViewById(R.id.threads_listview_time);
			holder.lastTime = (TextView) convertView.findViewById(R.id.threads_listview_last_post_time);
			holder.lastPoster = (TextView) convertView.findViewById(R.id.threads_listview_last_poster);
			holder.reply = (TextView) convertView.findViewById(R.id.threads_listview_reply);
			holder.click = (TextView) convertView.findViewById(R.id.threads_listview_click);
			holder.fid = (TextView) convertView.findViewById(R.id.threads_listview_fid);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ThreadsList threadsList = mThreadsList.get(position);
		if (threadsList != null) {
			holder.title.setText(threadsList.getSubject());
			holder.name.setText(threadsList.getAuthor());
			holder.time.setText(Html.fromHtml(threadsList.getDateline()));
			holder.lastTime.setText(Html.fromHtml(threadsList.getLastpost()));
			holder.lastPoster.setText(threadsList.getLastposter());
			holder.reply.setText(threadsList.getReplies());
			holder.click.setText(threadsList.getViews());
			holder.fid.setText(null);
		}

		return convertView;
	}
}