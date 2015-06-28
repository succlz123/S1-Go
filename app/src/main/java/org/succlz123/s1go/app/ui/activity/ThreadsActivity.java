package org.succlz123.s1go.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.utils.S1Fid;
import org.succlz123.s1go.app.support.utils.S1String;
import org.succlz123.s1go.app.ui.fragment.listview.ThreadsListListener;
import org.succlz123.s1go.app.ui.fragment.listview.ThreadsListFragment;

/**
 * Created by fashi on 2015/4/14.
 */
public class ThreadsActivity extends AppCompatActivity implements ThreadsListListener {
 	private String mFid;
  	private Toolbar mToolbar;
 	private String ToolbarTitle;

 	private FloatingActionButton mFloatingActionButton;

	public static void actionStart(Context context, String fid) {
		Intent intent = new Intent(context, ThreadsActivity.class);
		intent.putExtra(S1String.FID, fid);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_threads);
		initData();
		initViews();
		setToolbar();
		setFloatingActionButton();

 		getSupportFragmentManager().beginTransaction().replace(
				R.id.frame_fragment_threads,
				ThreadsListFragment.newInstance(mFid)).commit();
 	}

	private void initData() {
		mFid = getIntent().getStringExtra(S1String.FID);
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mFloatingActionButton = (FloatingActionButton) findViewById(R.id.thread_fab);
	}

	private void setToolbar() {
		//从S1Fid里去帖子所在分区标题
		ToolbarTitle = S1Fid.GetS1Fid(Integer.valueOf(mFid));
		mToolbar.setTitle(ToolbarTitle);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void setFloatingActionButton() {
		mFloatingActionButton.setShadow(true);
		mFloatingActionButton.setType(FloatingActionButton.TYPE_NORMAL);
		mFloatingActionButton.setColorNormal(getResources().getColor(R.color.base));
		mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetThreadsActivity.actionStart(ThreadsActivity.this, mFid,
						MyApplication.getInstance().getUserInfo().getFormhash());

			}
		});
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

	@Override
	public void onListViewListener(ListView listView) {
		mFloatingActionButton.attachToListView(listView);
	}

	@Override
	public void onFloatingActionButton(boolean isShow) {
		if(isShow){
			mFloatingActionButton.setVisibility(View.VISIBLE);
		}
	}
}