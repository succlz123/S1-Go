package org.succlz123.s1go.app.utils.fromhtml;

import org.succlz123.s1go.app.MainApplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by fashi on 2015/5/24.
 */
public class UrlDrawable extends BitmapDrawable {
	private Drawable mDrawable;

	public UrlDrawable() {
		super(MainApplication.getInstance().getResources(), Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888));
	}

	public void setActualDrawable(Drawable drawable) {
		this.mDrawable = drawable;
	}

	@Override
	public void draw(Canvas canvas) {
		if (mDrawable != null) {
			mDrawable.draw(canvas);
		} else {
			canvas.drawColor(Color.WHITE);
		}
	}

	@Override
	public void setBounds(Rect bounds) {
		super.setBounds(bounds);
		if (mDrawable != null) {
			this.mDrawable.setBounds(bounds);
		}
	}

	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		super.setBounds(left, top, right, bottom);
		if (mDrawable != null) {
			this.mDrawable.setBounds(left, top, right, bottom);
		}
	}
}
