package org.succlz123.s1go.app.dao.Api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fashi on 2015/4/1.
 */
public class GetBmApi {

    public static Bitmap getBitMap(String url) {

        Bitmap bitmap = null;
        URL myFileUrl = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
