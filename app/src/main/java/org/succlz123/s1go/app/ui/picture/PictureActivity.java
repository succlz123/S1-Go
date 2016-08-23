package org.succlz123.s1go.app.ui.picture;

import com.facebook.drawee.view.SimpleDraweeView;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.common.ViewUtils;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by succlz123 on 2015/4/17.
 */
public class PictureActivity extends BaseToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        ensureToolbar();
        showBackButton();
        setTitle("看图");
        String url = getIntent().getStringExtra("imageurl");

        SimpleDraweeView simpleDraweeView = ViewUtils.f(this, R.id.img);
        simpleDraweeView.setImageURI(Uri.parse(url));
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

