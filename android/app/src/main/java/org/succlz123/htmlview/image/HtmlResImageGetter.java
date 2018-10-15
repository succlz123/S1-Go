package org.succlz123.htmlview.image;

import org.succlz123.htmlview.HtmlTextView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;


public class HtmlResImageGetter implements Html.ImageGetter {
    private TextView container;

    public HtmlResImageGetter(TextView textView) {
        this.container = textView;
    }

    public Drawable getDrawable(String source) {
        Context context = container.getContext();
        int id = context.getResources().getIdentifier(source, "drawable", context.getPackageName());

        if (id == 0) {
            // the drawable resource wasn't found in our package, maybe it is a stock android drawable?
            id = context.getResources().getIdentifier(source, "drawable", "android");
        }

        if (id == 0) {
            // prevent a crash if the resource still can't be found
            Log.e(HtmlTextView.TAG, "source could not be found: " + source);
            return null;
        } else {
            Drawable d = context.getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
    }

}