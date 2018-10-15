package org.succlz123.s1go.ui.search;//package org.succlz123.s1go.app.ui.activity;
//
//import android.app.SearchManager;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import org.succlz123.s1go.R;
//
//import java.lang.reflect.Field;
//
///**
// * Created by succlz123 on 2015/5/21.
// */
//public class SearchActivity extends ActionBarActivity {
//	private SearchManager mSearMan;
//	private SearchView mSarchView;
//	private ListView mSearchList;
//	private Toolbar layout_toolbar;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.search_view);
//		initViews();
//		setToolbar();
//	}
//
//	private void initViews() {
//		layout_toolbar = (Toolbar) findViewById(R.id.layout_toolbar);
//		mSearchList = (ListView) findViewById(R.id.search_list_view);
//	}
//
//	private void setToolbar() {
////		layout_toolbar.setBackgroundColor(Color.WHITE);
//		setSupportActionBar(layout_toolbar);
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.menu, menu);
//		MenuItem searchItem = menu.findItem(R.id.action_search);
//		searchItem.collapseActionView();
//		searchItem.expandActionView();
//		SearchView searchView = (SearchView) searchItem.getActionView();
//		searchView.setQueryHint("请输入搜索内容");
//		searchView.setFocusable(false);
//		searchView.setFocusableInTouchMode(false);
//		TextView textView = (TextView) searchView.findViewById(R.id.search_src_text);
//		textView.setTextColor(Color.WHITE);
//		textView.setHintTextColor(Color.WHITE);
//
//		try {
//			Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
//			mCursorDrawableRes.setAccessible(true);
//			mCursorDrawableRes.setInt(textView, R.drawable.search_cursor);
//			textView.invalidate();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//			@Override
//			public boolean onQueryTextSubmit(String s) {
//				Toast.makeText(SearchActivity.this, s, Toast.LENGTH_LONG).show();
//				return true;
//			}
//
//			@Override
//			public boolean onQueryTextChange(String s) {
//				return false;
//			}
//		});
//		return true;
//	}
//
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		MenuItem searchItem = menu.findItem(R.id.action_search);
//		SearchView searchView = (SearchView) searchItem.getActionView();
//
//
////		TextView textView = (TextView) searchView.findViewById(R.id.search_src_text);
////		textView.setTextColor(Color.WHITE);
//		return super.onPrepareOptionsMenu(menu);
//	}
//
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case android.R.id.home:
//				onBackPressed();
// 				break;
//		}
//		return true;
//	}
//
//	@Override
//	public void onBackPressed() {
//
//		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            finish();
//            return false;
//        }
//		return super.onKeyDown(keyCode, event);
//	}
//}
