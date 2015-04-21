package org.succlz123.s1go.app.dao.Api;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.dao.Helper.S1UrlHelper;

/**
 * Created by fashi on 2015/4/21.
 */
public class GetAvatarApi {

    public static class GetAvatar extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private String uid;
        private ImageView imageView;

        public GetAvatar(String uid, ImageView imageView) {
            this.uid = uid;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (uid.length() < 9) {
                int count = 9 - uid.length();
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < count; i++) {
                    builder.append("0");
                }
                uid = builder.toString() + uid;
                uid = uid.substring(0, 3) + "/" + uid.substring(3, 5) + "/" + uid.substring(5, 7) + "/" + uid.substring(7, 9);
            }
            url = S1UrlHelper.AVATAR_MIDDLE.replace("000/00/00/00", uid);
            imageView.setTag(url);
            imageView.setImageResource(R.drawable.noavatar);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            return GetBmApi.getBitMap(url);
        }

        @Override
        protected void onPostExecute(Bitmap aVoid) {
            super.onPostExecute(aVoid);
            if (url.equals(imageView.getTag())) {
                if (aVoid != null) {
                    imageView.setImageBitmap(aVoid);
                } else if (aVoid == null) {
//                    imageView.setImageBitmap(null);
                    imageView.setImageResource(R.drawable.noavatar);
                }
            }
        }
    }
}
