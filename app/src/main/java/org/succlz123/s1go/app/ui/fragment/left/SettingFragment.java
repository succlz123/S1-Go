package org.succlz123.s1go.app.ui.fragment.left;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.succlz123.s1go.app.R;

/**
 * Created by fashi on 2015/6/24.
 */
public class SettingFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main_preference);
	}
}