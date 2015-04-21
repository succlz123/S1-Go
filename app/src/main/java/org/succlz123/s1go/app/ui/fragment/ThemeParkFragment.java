package org.succlz123.s1go.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.dao.Helper.S1FidHelper;
import org.succlz123.s1go.app.dao.Helper.S1FidImgHelper;
import org.succlz123.s1go.app.ui.activity.ThreadsActivity;

import java.util.*;

/**
 * Created by fashi on 2015/4/11.
 */
public class ThemeParkFragment extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forum_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.forum_base_fragment_listview);
        BaseAdapter mApdater = new AppAdapet();
        listView.setAdapter(mApdater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] fid = {"122", "125", "85", "26", "45", "42", "41", "43", "46", "33", "49", "61", "60"};
                List<String> fideList = Arrays.asList(fid);
                Intent intent = new Intent(getActivity(), ThreadsActivity.class);
                intent.putExtra("fid", fideList.get(position));
                startActivity(intent);
            }
        });
        return view;
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
            imageView.setImageBitmap(S1FidImgHelper.getBitmap(new Random().nextInt(193)));
            if (position == 0) {
                textView.setText(S1FidHelper.S122);
            } else if (position == 1) {
                textView.setText(S1FidHelper.S125);
            } else if (position == 2) {
                textView.setText(S1FidHelper.S85);
            } else if (position == 3) {
                textView.setText(S1FidHelper.S26);
            } else if (position == 4) {
                textView.setText(S1FidHelper.S45);
            } else if (position == 5) {
                textView.setText(S1FidHelper.S42);
            } else if (position == 6) {
                textView.setText(S1FidHelper.S41);
            } else if (position == 7) {
                textView.setText(S1FidHelper.S43);
            } else if (position == 8) {
                textView.setText(S1FidHelper.S46);
            } else if (position == 9) {
                textView.setText(S1FidHelper.S33);
            } else if (position == 10) {
                textView.setText(S1FidHelper.S49);
            } else if (position == 11) {
                textView.setText(S1FidHelper.S61);
            } else if (position == 12) {
                textView.setText(S1FidHelper.S60);
            }

            return convertView;
        }
    }
}
