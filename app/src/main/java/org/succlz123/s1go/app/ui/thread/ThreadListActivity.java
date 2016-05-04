package org.succlz123.s1go.app.ui.thread;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;
import org.succlz123.s1go.app.utils.s1.S1Fid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by fashi on 2015/4/14.
 */
public class ThreadListActivity extends BaseToolbarActivity {
    public static final String KEY_THREAD_ACTIVITY_FID = "key_activity_thread_fid";

    private String mFid;

    public static void actionStart(Context context, String fid) {
        Intent intent = new Intent(context, ThreadListActivity.class);
        intent.putExtra(KEY_THREAD_ACTIVITY_FID, fid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_list);

        mFid = getIntent().getStringExtra(KEY_THREAD_ACTIVITY_FID);
        showBackButton();
        setCustomTitle(S1Fid.getS1Fid(Integer.valueOf(mFid)));

        setFloatingActionButton();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment,
                ThreadListFragment.newInstance(mFid)).commit();
    }

    private void setFloatingActionButton() {
//		mFloatingActionButton.setShadow(true);
//		mFloatingActionButton.setType(FloatingActionButton.TYPE_NORMAL);
//		mFloatingActionButton.setColorNormal(getResources().getColor(R.color.base));
//		mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				SendThreadsActivity.newInstance(ThreadListActivity.this, fid,
//						MainApplication.getInstance().getLoginInfo().getFormhash());
//
//			}
//		});
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