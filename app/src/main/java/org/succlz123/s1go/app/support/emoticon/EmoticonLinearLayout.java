package org.succlz123.s1go.app.support.emoticon;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by fashi on 2015/6/8.
 */
public class EmoticonLinearLayout extends LinearLayout {

	public EmoticonLinearLayout(Context context) {
		super(context);
	}

	public EmoticonLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EmoticonLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public EmoticonLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
 	}

}
