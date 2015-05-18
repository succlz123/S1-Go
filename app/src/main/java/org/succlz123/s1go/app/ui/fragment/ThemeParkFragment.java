package org.succlz123.s1go.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.dao.helper.S1Fid;
import org.succlz123.s1go.app.dao.helper.S1FidIcon;
import org.succlz123.s1go.app.ui.activity.ThreadsActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by fashi on 2015/4/11.
 */
public class ThemeParkFragment extends Fragment {
	private ListView mListView;
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.forum_fragment, container, false);
		mListView = (ListView) mView.findViewById(R.id.forum_base_fragment_listview);

		BaseAdapter mApdater = new AppAdapet();
		mListView.setAdapter(mApdater);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String[] fid = {"122", "125", "85", "26", "45", "42", "41", "43", "46", "33", "49", "61", "60"};
				List<String> fideList = Arrays.asList(fid);
				Intent intent = new Intent(getActivity(), ThreadsActivity.class);
				intent.putExtra("fid", fideList.get(position));
				startActivity(intent);
			}
		});
		return mView;
	}

	private class AppAdapet extends BaseAdapter {

		@Override
		public int getCount() {
			return 13;
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
			convertView = getActivity().getLayoutInflater().inflate(R.layout.forum_fragment_listview_item, parent, false);
			TextView textView = (TextView) convertView.findViewById(R.id.forum_base_fragment_listview_item_title);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.forum_base_fragment_listview_item_forum_img);
			imageView.setImageBitmap(S1FidIcon.getBitmap(new Random().nextInt(193)));
			if (position == 0) {
				textView.setText(S1Fid.S122);
			} else if (position == 1) {
				textView.setText(S1Fid.S125);
			} else if (position == 2) {
				textView.setText(S1Fid.S85);
			} else if (position == 3) {
				textView.setText(S1Fid.S26);
			} else if (position == 4) {
				textView.setText(S1Fid.S45);
			} else if (position == 5) {
				textView.setText(S1Fid.S42);
			} else if (position == 6) {
				textView.setText(S1Fid.S41);
			} else if (position == 7) {
				textView.setText(S1Fid.S43);
			} else if (position == 8) {
				textView.setText(S1Fid.S46);
			} else if (position == 9) {
				textView.setText(S1Fid.S33);
			} else if (position == 10) {
				textView.setText(S1Fid.S49);
			} else if (position == 11) {
				textView.setText(S1Fid.S61);
			} else if (position == 12) {
				textView.setText(S1Fid.S60);
			}
			return convertView;
		}
	}
}
