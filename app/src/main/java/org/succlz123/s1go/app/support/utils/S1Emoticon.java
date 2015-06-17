package org.succlz123.s1go.app.support.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.succlz123.s1go.app.MyApplication;

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
public class S1Emoticon {
	private static HashMap<String, Bitmap> iconBitmapList = new HashMap<String, Bitmap>();
	private static List<Bitmap> icons = new ArrayList<Bitmap>();

	public static void initEmoticon() {
		AssetManager assetFileDescriptor = MyApplication.getInstance().getAssets();
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
						iconBitmapList.put(m.group(), bitmap);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getEmoticon(int postion) {
		if (icons.size() == 0) {
			icons.addAll(iconBitmapList.values());
 		}
		return icons.get(postion);
	}

	public static Bitmap getEmoticon(String fileName) {
		return iconBitmapList.get(fileName);
	}

	public static HashMap<String, Bitmap> getIconBitmapList() {
		return iconBitmapList;
	}

	public static List<Bitmap> getIcons() {
		return icons;
	}
}
