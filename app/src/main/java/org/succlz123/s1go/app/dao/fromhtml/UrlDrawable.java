package org.succlz123.s1go.app.dao.fromhtml;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by fashi on 2015/5/24.
 */
public class UrlDrawable extends BitmapDrawable {
	private Drawable mDrawable;

	public void setActualDrawable(Drawable drawable) {
		this.mDrawable = drawable;
		invalidateSelf();
	}

	@Override
	public void draw(Canvas canvas) {
		if (mDrawable != null) {
			mDrawable.draw(canvas);
		}
	}
}
