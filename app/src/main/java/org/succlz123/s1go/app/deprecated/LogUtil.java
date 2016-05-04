package org.succlz123.s1go.app.deprecated;

import android.util.Log;

/**
 * Created by fashi on 2015/5/21.
 */
public class LogUtil {
	public static final int VERBOSE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5;
	public static final int NOTHING = 6;
	public static final int LEVEL = ERROR;

	public static void v(String tag, String msg) {
		if (LEVEL <= VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LEVEL <= DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LEVEL <= INFO) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LEVEL <= WARN) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LEVEL <= ERROR) {
			Log.e(tag, msg);
		}
	}

	protected static String buildMessage(String msg) {
		StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

		return new StringBuilder()
				.append(caller.getClassName())
				.append(".")
				.append(caller.getMethodName())
				.append("(): ")
				.append(msg).toString();
	}
}
