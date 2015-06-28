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
 * Created by fashi on 2015/4/11.
 */
public class HotAreaFragment extends Fragment {
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
				String[] fid = {"134", "138", "118", "132", "105", "131", "69", "76", "111"};
				List<String> fidList = Arrays.asList(fid);
				ThreadsActivity.actionStart(getActivity(), fidList.get(position));
			}
		});
	}

	private class FidAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 9;
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
					textView.setText(S1Fid.S134);
					break;
				case 1:
					textView.setText(S1Fid.S138);
					break;
				case 2:
					textView.setText(S1Fid.S118);
					break;
				case 3:
					textView.setText(S1Fid.S132);
					break;
				case 4:
					textView.setText(S1Fid.S105);
					break;
				case 5:
					textView.setText(S1Fid.S131);
					break;
				case 6:
					textView.setText(S1Fid.S69);
					break;
				case 7:
					textView.setText(S1Fid.S76);
					break;
				case 8:
					textView.setText(S1Fid.S111);
					break;
			}
			return convertView;
		}
	}
}
