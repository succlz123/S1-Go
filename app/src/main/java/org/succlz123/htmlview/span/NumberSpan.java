package org.succlz123.htmlview.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;

/**
 * Class to use Numbered Lists in TextViews.
 * The span works the same as {@link android.text.style.BulletSpan} and all lines of the entry have
 * the same leading margin.
 */
public class NumberSpan implements LeadingMarginSpan {
    private final String mNumber;
    private final int mTextWidth;

    public NumberSpan(TextPaint textPaint, int number) {
        mNumber = Integer.toString(number).concat(". ");
        mTextWidth = (int) textPaint.measureText(mNumber);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return mTextWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline,
                                  int bottom, CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (text instanceof Spanned) {
            int spanStart = ((Spanned) text).getSpanStart(this);
            if (spanStart == start) {
                c.drawText(mNumber, x, baseline, p);
            }
        }
    }
}
