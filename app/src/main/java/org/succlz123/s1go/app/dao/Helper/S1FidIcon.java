package org.succlz123.s1go.app.dao.Helper;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.succlz123.s1go.app.S1GoApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fashi on 2015/4/13.
 */
public class S1FidIcon {
    private static HashMap<String, Bitmap> bitmapList = new HashMap<String, Bitmap>();
    private static List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private static String[] ss = new String[2];

    public static void getS1FidImg() {

        AssetManager assetFileDescriptor = S1GoApplication.getInstance().getAssets();
        try {
            String[] emoticonDirs = assetFileDescriptor.list("emoticon");
            for (String dir : emoticonDirs) {
                String[] emoticons = assetFileDescriptor.list("emoticon/" + dir);
                for (String fileName : emoticons) {
                    InputStream inputStream = assetFileDescriptor.open("emoticon/" + dir + "/" + fileName);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Pattern p = Pattern.compile("\\d{1,3}");
                    Matcher m = p.matcher(fileName);
                    while (m.find()) {
                        bitmapList.put(m.group(), bitmap);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmap(int random) {
        bitmaps.addAll(bitmapList.values());
        return bitmaps.get(random);
    }

    public static Bitmap getBitmap(String fileName) {
        return bitmapList.get(fileName);
    }


}
