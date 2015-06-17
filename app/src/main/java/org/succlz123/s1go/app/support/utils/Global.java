package org.succlz123.s1go.app.support.utils;

/**
 * Created by fashi on 2015/6/11.
 */
public class Global {

	/**
	 * int转string
	 *
	 * @param length
	 * @return int大于0 才返回转换后的string 其他返回空
	 */
	private static String intToString(int length) {
		String width;
		if (length > 0) {
			width = String.valueOf(length);
		} else {
			width = "";
		}

		return width;
	}

//	public static int dpToPx(int dpValue) {
//		return (int) (dpValue * MyApp.sScale + 0.5f);
//	}
//
//	public static int dpToPx(double dpValue) {
//		return (int) (dpValue * MyApp.sScale + 0.5f);
//	}
//
//	public static int pxToDp(float pxValue) {
//		return (int) (pxValue / MyApp.sScale + 0.5f);
//	}
}
