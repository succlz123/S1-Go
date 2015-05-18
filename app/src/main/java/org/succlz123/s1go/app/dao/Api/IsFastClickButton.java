package org.succlz123.s1go.app.dao.api;

import android.widget.Toast;
import org.succlz123.s1go.app.S1GoApplication;

/**
 * Created by fashi on 2015/4/22.
 */
public class IsFastClickButton {

	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 4000) {
			Toast.makeText(S1GoApplication.getInstance(), "骚年 你点太快了", Toast.LENGTH_SHORT).show();
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
