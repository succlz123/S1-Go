package org.succlz123.s1go.app.ui.emoticon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.utils.s1.S1Emoticon;

/**
 * Created by fashi on 2015/6/5.
 */
public class EmoticonFragment extends Fragment {
	private View mView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.gridview_emoticon, container, false);
		GridView gridView = (GridView) mView.findViewById(R.id.emoticon_gridview);

		gridView.setNumColumns(8);
		gridView.setAdapter(new GridViewAdapter());
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.e("gridview",parent.toString()+view.toString()+ "" + position+""+id);

			}
		});
		return mView;
	}

	public class GridViewAdapter extends BaseAdapter {

		private class ViewHolder {
			ImageView icon;
		}

		@Override
		public int getCount() {
			return S1Emoticon.getIconBitmapList().size();
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
			ViewHolder holder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.gridview_emoticon_item, parent, false);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.emoticon_gridview_imageview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.icon.setImageBitmap(S1Emoticon.getIcons().get(position));
			return convertView;
		}
	}
}