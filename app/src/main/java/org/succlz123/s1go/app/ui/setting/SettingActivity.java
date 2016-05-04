package org.succlz123.s1go.app.ui.setting;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.ui.base.BaseToolbarActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


/**
 * Created by fashi on 2015/6/24.
 */
public class SettingActivity extends BaseToolbarActivity {

	public static void newInstance(Context context) {
		Intent intent = new Intent(context, SettingActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		showBackButton();
		setCustomTitle(getString(R.string.action_settings));

		getFragmentManager().beginTransaction().replace(R.id.frame_setting, new SettingFragment()).commit();
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
