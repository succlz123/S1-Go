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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by fashi on 2015/4/11.
 */
public class SubforumFragment extends Fragment {
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
                String[] fid = {"27", "9", "15"};
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
            return 3;
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
            switch (position) {
                case 0:
                    textView.setText(S1FidHelper.S27);
                    break;
                case 1:
                    textView.setText(S1FidHelper.S9);
                    break;
                case 2:
                    textView.setText(S1FidHelper.S15);
                    break;
            }
            return convertView;
        }
    }
}
