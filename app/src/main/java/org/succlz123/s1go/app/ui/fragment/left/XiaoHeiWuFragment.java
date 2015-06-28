package org.succlz123.s1go.app.ui.fragment.left;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.succlz123.s1go.app.R;

/**
 * Created by fashi on 2015/4/12.
 */
public class XiaoHeiWuFragment extends Fragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_forum, container, false);


//        AppImageLoader.getInstance().loadBitmap(url, xx, new AppImageLoader.Callback() {
//            @Override
//            public void onResult(String url, Bitmap bitmap) {
//
//            }
//
//            @Override
//            public void onError(String url, Exception exception) {
//
//            }
//        });

        return mView;
    }


//    LOGIN c = new LOGIN(h,q);
//
//    class h {
//
//    }


public interface xx {
        public void rucn(String x, int hah);
    }
}
