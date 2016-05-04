package org.succlz123.s1go.app.ui.picture;

import com.facebook.drawee.view.SimpleDraweeView;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by fashi on 2015/4/17.
 */
public class PictureActivity extends BaseToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);


        String url = getIntent().getStringExtra("imageurl");

        SimpleDraweeView simpleDraweeView = f(R.id.img);
        simpleDraweeView.setImageURI(Uri.parse(url));
//		DisplayMetrics metric = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metric);
//		int width = metric.widthPixels;     // 屏幕宽度（像素）
//		int height = metric.heightPixels;   // 屏幕高度（像素）
//		AppSize appSize = new AppSize(width, height);

//
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

