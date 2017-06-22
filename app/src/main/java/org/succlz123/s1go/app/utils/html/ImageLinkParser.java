package org.succlz123.s1go.app.utils.html;

import org.succlz123.s1go.app.ui.picture.PictureActivity;

import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by succlz123 on 2015/4/17.
 */
public class ImageLinkParser extends LinkMovementMethod {
    private static final ImageLinkParser instance = new ImageLinkParser();

    public static ImageLinkParser getInstance() {
        return instance;
    }

    private boolean find = false;
    private ImageSpan findSpan = null;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        Layout layout = widget.getLayout();

        if (layout == null) {
            return super.onTouchEvent(widget, buffer, event);
        }

        int x = (int) event.getX();
        int y = (int) event.getY();

        int line = layout.getLineForVertical(y);
        int offset = layout.getOffsetForHorizontal(line, x);

        SpannableString value = SpannableString.valueOf(widget.getText());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                findSpan = null;
                ImageSpan[] imageSpans = value.getSpans(0, value.length(), ImageSpan.class);
                int findStart = 0;
                int findEnd = 0;

                for (ImageSpan imageSpan : imageSpans) {
                    int start = value.getSpanStart(imageSpan);
                    int end = value.getSpanEnd(imageSpan);
                    if (start <= offset && offset <= end) {
                        find = true;
                        findStart = start;
                        findEnd = end;
                        findSpan = imageSpan;
                        break;
                    }
                }
                return find || super.onTouchEvent(widget, buffer, event);
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                findSpan = null;
                break;
            case MotionEvent.ACTION_UP:
                if (findSpan != null) {
                    String url = findSpan.getSource();
                    if (url.startsWith("static/image/smiley/")) {
                        return false;
                    }
                    PictureActivity.start(widget.getContext(), url);
                    return true;
                }
        }
        return super.onTouchEvent(widget, buffer, event);
    }
}