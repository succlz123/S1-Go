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
 * Created by fashi on 2015/4/14.
 */
public class MainForumFragment extends Fragment {
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
                String[] fid = {"4", "135", "6", "136", "48", "24", "51", "50", "31","77","75","133","82","115","119"};
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.forum_fragment_listview_item, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.forum_base_fragment_listview_item_title);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.forum_base_fragment_listview_item_forum_img);
            imageView.setImageBitmap(S1FidImgHelper.getBitmap(new Random().nextInt(193)));
            switch (position) {
                case 0:
                    textView.setText(S1FidHelper.S4);
                    break;
                case 1:
                    textView.setText(S1FidHelper.S135);
                    break;
                case 2:
                    textView.setText(S1FidHelper.S6);
                    break;
                case 3:
                    textView.setText(S1FidHelper.S136);
                    break;
                case 4:
                    textView.setText(S1FidHelper.S48);
                    break;
                case 5:
                    textView.setText(S1FidHelper.S24);
                    break;
                case 6:
                    textView.setText(S1FidHelper.S51);
                    break;
                case 7:
                    textView.setText(S1FidHelper.S50);
                    break;
                case 8:
                    textView.setText(S1FidHelper.S31);
                    break;
                case 9:
                    textView.setText(S1FidHelper.S77);
                    break;
                case 10:
                    textView.setText(S1FidHelper.S75);
                    break;
                case 11:
                    textView.setText(S1FidHelper.S133);
                    break;
                case 12:
                    textView.setText(S1FidHelper.S82);
                    break;
                case 13:
                    textView.setText(S1FidHelper.S115);
                    break;
                case 14:
                    textView.setText(S1FidHelper.S119);
                    break;
            }
            return convertView;
        }
    }
}
