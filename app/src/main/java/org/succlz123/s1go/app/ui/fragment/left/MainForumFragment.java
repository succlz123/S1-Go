package org.succlz123.s1go.app.ui.fragment.left;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.utils.S1Fid;
import org.succlz123.s1go.app.support.utils.S1Emoticon;
import org.succlz123.s1go.app.ui.activity.ThreadsActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by fashi on 2015/4/14.
 */
public class MainForumFragment extends Fragment {
	private ListView mListView;
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_forum, container, false);
		initViews();
		setListView();
		return mView;
	}

	private void initViews() {
		mListView = (ListView) mView.findViewById(R.id.forum_base_fragment_listview);
	}

	private void setListView() {
		FidAdapter fidAdapter = new FidAdapter();
		mListView.setAdapter(fidAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String[] fid = {"4", "135", "6", "136", "48", "24", "51", "50", "31", "77", "75", "133", "82", "115", "119"};
				List<String> fidList = Arrays.asList(fid);
				ThreadsActivity.actionStart(getActivity(), fidList.get(position));
			}
		});
	}

	private class FidAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 15;
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
			convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_forum_listview_item, parent, false);
			TextView textView = (TextView) convertView.findViewById(R.id.forum_base_fragment_listview_item_title);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.forum_base_fragment_listview_item_forum_img);

			imageView.setImageBitmap(S1Emoticon.getEmoticon(new Random().nextInt(193)));

			switch (position) {
				case 0:
					textView.setText(S1Fid.S4);
					break;
				case 1:
					textView.setText(S1Fid.S135);
					break;
				case 2:
					textView.setText(S1Fid.S6);
					break;
				case 3:
					textView.setText(S1Fid.S136);
					break;
				case 4:
					textView.setText(S1Fid.S48);
					break;
				case 5:
					textView.setText(S1Fid.S24);
					break;
				case 6:
					textView.setText(S1Fid.S51);
					break;
				case 7:
					textView.setText(S1Fid.S50);
					break;
				case 8:
					textView.setText(S1Fid.S31);
					break;
				case 9:
					textView.setText(S1Fid.S77);
					break;
				case 10:
					textView.setText(S1Fid.S75);
					break;
				case 11:
					textView.setText(S1Fid.S133);
					break;
				case 12:
					textView.setText(S1Fid.S82);
					break;
				case 13:
					textView.setText(S1Fid.S115);
					break;
				case 14:
					textView.setText(S1Fid.S119);
					break;
			}
			return convertView;
		}
	}
}
