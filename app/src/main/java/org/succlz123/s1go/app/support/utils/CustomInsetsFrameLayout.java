package org.succlz123.s1go.app.support.utils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author Kevin
 *         Date Created: 3/7/14
 *         <p/>
 *         https://code.google.com/p/android/issues/detail?id=63777
 *         <p/>
 *         When using a translucent status bar on API 19+, the window will not
 *         resize to make room for input methods (i.e.
 *         {@link android.view.WindowManager.LayoutParams#SOFT_INPUT_ADJUST_RESIZE} and
 *         {@link android.view.WindowManager.LayoutParams#SOFT_INPUT_ADJUST_PAN} are
 *         ignored).
 *         <p/>
 *         To work around this; override {@link #fitSystemWindows(android.graphics.Rect)},
 *         capture and override the system insets, and then call through to FrameLayout's
 *         implementation.
 *         <p/>
 *         For reasons yet unknown, modifying the bottom inset causes this workaround to
 *         fail. Modifying the top, left, and right insets works as expected.
 */
public final class CustomInsetsFrameLayout extends FrameLayout {
	private int[] mInsets = new int[4];

	public CustomInsetsFrameLayout(Context context) {
		super(context);
	}

	public CustomInsetsFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomInsetsFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public final int[] getInsets() {
		return mInsets;
	}

	@Override
	protected final boolean fitSystemWindows(Rect insets) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// Intentionally do not modify the bottom inset. For some reason,
			// if the bottom inset is modified, window resizing stops working.
			// TODO: Figure out why.

			mInsets[0] = insets.left;
			mInsets[1] = insets.top;
			mInsets[2] = insets.right;

			insets.left = 0;
			insets.top = 0;
			insets.right = 0;
		}

		return super.fitSystemWindows(insets);
	}
}