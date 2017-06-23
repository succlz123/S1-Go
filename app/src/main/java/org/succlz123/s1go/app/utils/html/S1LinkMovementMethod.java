package org.succlz123.s1go.app.utils.html;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.TextView;

import org.succlz123.htmlview.touch.LocalLinkMovementMethod;
import org.succlz123.s1go.app.ui.picture.PictureActivity;

/**
 * Created by succlz123 on 2015/4/17.
 */
public class S1LinkMovementMethod extends LocalLinkMovementMethod {
    private static final S1LinkMovementMethod instance = new S1LinkMovementMethod();

    public static S1LinkMovementMethod getInstance() {
        return instance;
    }

    public void onImageSpanClick(TextView widget, @NonNull ImageSpan imageSpan, String source) {
        if (TextUtils.isEmpty(source)) {
            return;
        }
        if (source.startsWith("http://static.saraba1st.com/image/smiley/")) {
            return;
        }
        PictureActivity.start(widget.getContext(), source);
    }
}