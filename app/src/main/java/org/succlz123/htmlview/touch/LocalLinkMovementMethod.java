package org.succlz123.htmlview.touch;

import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by succlz123 on 2015/4/17.
 */
public class LocalLinkMovementMethod extends LinkMovementMethod {
    private boolean mFind = false;
    private ImageSpan mImageSpan = null;
    private ClickableSpan mClickableSpan = null;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        Layout layout = widget.getLayout();
        if (layout == null) {
            return super.onTouchEvent(widget, buffer, event);
        }

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        x += widget.getScrollX();
        y += widget.getScrollY();

        int line = layout.getLineForVertical(y);
        int offset = layout.getOffsetForHorizontal(line, x);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mImageSpan = null;
                ImageSpan[] imageSpans = buffer.getSpans(0, buffer.length(), ImageSpan.class);
                int findStart = 0;
                int findEnd = 0;
                for (ImageSpan imageSpan : imageSpans) {
                    int start = buffer.getSpanStart(imageSpan);
                    int end = buffer.getSpanEnd(imageSpan);
                    if (start <= offset && offset <= end) {
                        mFind = true;
                        findStart = start;
                        findEnd = end;
                        mImageSpan = imageSpan;
                        break;
                    }
                }
                ClickableSpan[] link = buffer.getSpans(offset, offset, ClickableSpan.class);
                if (link != null && link.length > 0) {
                    mFind = true;
                    mClickableSpan = link[0];
                }
                return mFind || super.onTouchEvent(widget, buffer, event);
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                mImageSpan = null;
                break;
            case MotionEvent.ACTION_UP:
                if (mImageSpan != null) {
                    onImageSpanClick(widget, mImageSpan, mImageSpan.getSource());
                    return true;
                }
                if (mClickableSpan instanceof URLSpan) {
                    onUrlSpanClick(widget, (URLSpan) mClickableSpan, ((URLSpan) mClickableSpan).getURL());
                    return true;
                }
                break;
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    public void onImageSpanClick(TextView widget, @NonNull ImageSpan imageSpan, String source) {
    }

    public void onUrlSpanClick(TextView widget, @NonNull URLSpan urlSpan, String source) {
        urlSpan.onClick(widget);
    }
}