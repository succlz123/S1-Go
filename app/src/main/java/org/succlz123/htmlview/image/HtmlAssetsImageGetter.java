package org.succlz123.htmlview.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;

import org.succlz123.htmlview.HtmlTextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Assets Image Getter
 * <p>
 * Load image from assets folder
 * <p>
 * Created by succlz123 on 2015/4/13.
 **/

public class HtmlAssetsImageGetter implements Html.ImageGetter {
    private final Context mContext;
    private int mSize;

    public HtmlAssetsImageGetter(Context context) {
        this.mContext = context;
    }

    public HtmlAssetsImageGetter(Context context, int size) {
        this.mContext = context;
        this.mSize = size;
    }

    @Override
    public Drawable getDrawable(String source) {
        try {
            InputStream inputStream = mContext.getAssets().open(source);
            Drawable d = Drawable.createFromStream(inputStream, null);
            if (mSize > 0) {
                d.setBounds(0, 0, mSize, mSize);
            } else {
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            }
            return d;
        } catch (IOException e) {
            Log.e(HtmlTextView.TAG, "source could not be found: " + source);
            return null;
        }
    }
}
