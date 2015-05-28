package org.succlz123.s1go.app.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.*;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.github.siyamed.shapeimageview.CircularImageView;
import org.succlz123.s1go.app.MyApplication;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.dao.api.ConvertUidToAvatarUrl;
import org.succlz123.s1go.app.dao.api.DeEnCode;
import org.succlz123.s1go.app.dao.interaction.LoginAsyncTask;
import org.succlz123.s1go.app.support.imageloader.ImageDownLoader;
import org.succlz123.s1go.app.support.utils.AppSize;
import org.succlz123.s1go.app.ui.fragment.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawLayoutListView;
	private AppAdapter mBaseAdapter;
	private ArrayList<Fragment> mFragmentList;
	private boolean mIsExit = false;//标识是否点击过一次back
	private int mPosition;
	private TextView mUn;
	private TextView mUid;
	private TextView mLogin;
	private ImageView mCircleImageView;
	private AppSize mAppSize;
	private LoginVariables mUserInfo;

	private MainForumFragment mMainForumFragment;
	private HotAreaFragment mHotAreaFragment;
	private ThemeParkFragment mThemeParkFragment;
	private SubforumFragment mSubForumFragment;
	private XiaoHeiWuFragment mXiaoHeiWuFragment;
	private HotThreadsFragment mHotThreadsFragment;
	private LoginDiaLogFragment mLoginDiaLogFragment;

	//点击返回键时，延时 TIME_TO_EXIT 毫秒发送此handler重置mIsExit，再其被重置前如果再按一次返回键则退出应用
	private Handler mExitHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mIsExit = false;
		}
	};
	final static int TIME_TO_EXIT = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		setToolbar();
		setUserInfo();
		setDrawerToggle();
		FragmentListAdd();
		setDrawLayoutListView();
		TypedValue typedValue = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
		int color = typedValue.data;
//		mDrawerLayout.setStatusBarBackgroundColor(color);
//		mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.shadow));
		mDrawerLayout.setStatusBarBackgroundColor(
				getResources().getColor(R.color.white));
	}

	@Override
	protected void onResume() {
		super.onResume();
//		mToolbar.setTitle(GetMemInfo.getMemInfo().toString());
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		mDrawLayoutListView = (ListView) findViewById(R.id.listView);
		mUn = (TextView) findViewById(R.id.drawerlayout_un);
		mUid = (TextView) findViewById(R.id.drawerlayout_uid);
		mLogin = (TextView) findViewById(R.id.login);
		mCircleImageView = (CircularImageView) findViewById(R.id.avatar_drawerlayout_img);
	}

	private void setToolbar() {
		mToolbar.setTitle("Stage1st Go");
		mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
		mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitle);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		//设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//创建返回键，并实现打开开关监听
	}

	private void setDrawerToggle() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
		};
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setScrimColor(getResources().getColor(R.color.shadow));
	}

	private void FragmentListAdd() {
		mFragmentList = new ArrayList<Fragment>();
		mMainForumFragment = new MainForumFragment();
		mHotAreaFragment = new HotAreaFragment();
		mThemeParkFragment = new ThemeParkFragment();
		mSubForumFragment = new SubforumFragment();
		mXiaoHeiWuFragment = new XiaoHeiWuFragment();
		mHotThreadsFragment = new HotThreadsFragment();
		mLoginDiaLogFragment = new LoginDiaLogFragment();
		mFragmentList.add(mMainForumFragment);
		mFragmentList.add(mHotAreaFragment);
		mFragmentList.add(mThemeParkFragment);
		mFragmentList.add(mSubForumFragment);
		mFragmentList.add(mXiaoHeiWuFragment);
		mFragmentList.add(mHotThreadsFragment);
	}

	private void setDrawLayoutListView() {
		mBaseAdapter = new AppAdapter();
		mDrawLayoutListView.setAdapter(mBaseAdapter);
		mDrawLayoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

				mDrawerLayout.closeDrawer(Gravity.LEFT);//选择完fragment 关闭drawerlayout
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
					@Override
					public void run() {
						MainActivity.this.mPosition = position;
						FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
						for (int i = 0; i < mFragmentList.size(); i++) {
							Fragment fragment = mFragmentList.get(i);
							if (i == position) {
								if (getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) == null) {
									fragmentTransaction.add(R.id.activity_content, fragment, fragment.getClass().getSimpleName());
								}//找到同名fragment显示
								fragmentTransaction.show(fragment);
							} else {
								if (getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) != null) {
									fragmentTransaction.hide(fragment);
								}//其他不是需要的fragment隐藏
							}
						}
						fragmentTransaction.commitAllowingStateLoss();
					}
				}, 300);
			}
		});
		mDrawLayoutListView.performItemClick(null, 0, 0);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		int width = mCircleImageView.getWidth();
		int height = mCircleImageView.getHeight();
		mAppSize = new AppSize(width, height);
		Log.e("mCircleImageView W_H", width + "_" + height);
	}

	private void setUserInfo() {
		mUserInfo = MyApplication.getInstance().getUserInfo();
		if (mUserInfo != null) {
			String name = mUserInfo.getMember_username();
			String password = DeEnCode.code(mUserInfo.getPassword());
			LoginAsyncTask mLoginAsyncTask = new LoginAsyncTask(name, password);
			mLoginAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		onUserInfoChanged(mUserInfo);
		MyApplication.getInstance().addUserListener(new MyApplication.onUserListener() {
			@Override
			public void onChanged(LoginVariables userInfo) {
				onUserInfoChanged(userInfo);
			}
		});
	}

	private void onUserInfoChanged(LoginVariables userInfo) {
		if (userInfo != null) {
			String name = userInfo.getMember_username();
			String password = DeEnCode.code(userInfo.getPassword());
			String uid = userInfo.getMember_uid();
			String readAccess = userInfo.getReadaccess();

			Logout logout = new Logout();
			mLogin.setOnClickListener(logout);
			mLogin.setText("登出");
//			mToolbar.setSubtitle(name);
			mUn.setText(name);
			mUid.setText("UID " + uid);

			if (mAppSize != null) {
				ImageDownLoader.getInstance().loadBitmap(ConvertUidToAvatarUrl.getAvatar(uid), mAppSize, new ImageDownLoader.CallBack() {
					@Override
					public void onLoad(String url, Bitmap bitmap) {
						mCircleImageView.setImageBitmap(bitmap);
					}

					@Override
					public void onError(String url) {

					}
				});
			}

			if (this.mToolbar.getChildCount() > 3) {
				View view = this.mToolbar.getChildAt(3);
				view.setTranslationY(view.getHeight() + 15);
				view.animate().translationY(0);
			}
		} else if (userInfo == null) {
			Login login = new Login();
			mLogin.setOnClickListener(login);
			mLogin.setText("登录");
			mUn.setText(null);
			mUid.setText(null);
			mCircleImageView.setImageResource(R.drawable.noavatar);
		}
	}

	private class Login implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			mLoginDiaLogFragment.show(fragmentTransaction, "");
		}
	}

	private class Logout implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			dialog();
		}
	}

	private void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MyApplication.getInstance().removeUser();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	private class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 6;
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
			convertView = getLayoutInflater().inflate(R.layout.drawerlayout_listview_item, parent, false);
			TextView textView = (TextView) convertView.findViewById(R.id.fourm_drawerlayout_text);
			if (position == 0) {
				textView.setText("主论坛");
			} else if (position == 1) {
				textView.setText("热门新区");
			} else if (position == 2) {
				textView.setText("主题公园");
			} else if (position == 3) {
				textView.setText("子论坛");
			} else if (position == 4) {
				textView.setText("小黑屋");
				ImageView xiaoheiwu = (ImageView) convertView.findViewById(R.id.fourm_drawerlayout_img);
				xiaoheiwu.setBackgroundResource(R.drawable.xiaoheiwu);
			} else if (position == 5) {
				textView.setText("论坛热帖");
				ImageView hotPost = (ImageView) convertView.findViewById(R.id.fourm_drawerlayout_img);
				hotPost.setBackgroundResource(R.drawable.hot);
			}
			return convertView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.menu_main, menu);
		MenuItem item = menu.add(Menu.NONE, Menu.FIRST, 100, "搜索");
		item.setIcon(R.drawable.search);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//			case R.id.action_settings:
//				break;
			case Menu.FIRST:
//				Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//				startActivity(intent);
//				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

				Intent intent1 = new Intent();
				intent1.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivity(intent1);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 点击两次退出
	 */
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);//不销毁activity
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return false;
//        }
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (mIsExit) {
			finish();
			System.exit(0);
		} else {
			mIsExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
			//两秒内不点击back则重置mIsExit
			mExitHandler.sendEmptyMessageDelayed(0, TIME_TO_EXIT);
		}
	}
}
