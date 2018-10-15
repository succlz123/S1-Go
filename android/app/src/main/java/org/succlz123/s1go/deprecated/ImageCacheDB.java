package org.succlz123.s1go.deprecated;

import org.succlz123.s1go.MainApplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by succlz123 on 2015/4/25.
 */
public class ImageCacheDB extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "imagecache.db";
    private static final String TABLE_NAME = "imagecache";

    private static final String COLUMN_URL = "url";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_FILE_SIZE = "file_size";
    private static final String COLUMN_LAST_ACCESSED_TIME = "last_accessed_time";
    private static final long CACHE_SIZE = 4;//max cache size is 100M

    private static final String IMAGE_CACHE = "create table if not exists " + TABLE_NAME + " ("
            + COLUMN_URL + " TEXT primary key,"
            + COLUMN_PATH + " TEXT,"
            + COLUMN_FILE_SIZE + " INTEGER,"
            + COLUMN_LAST_ACCESSED_TIME + " INTEGER"
            + ")";

    private static ImageCacheDB instance;

    public synchronized static ImageCacheDB getInstance() {
        if (instance == null) {
            instance = new ImageCacheDB(MainApplication.getInstance().getApplicationContext(), DB_NAME, null, VERSION);
        }
        return instance;
    }

    private ImageCacheDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IMAGE_CACHE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void execInsert(String url, File localCache) {
        trimToSize();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        long time = System.currentTimeMillis();
        cv.put(COLUMN_URL, url);
        cv.put(COLUMN_PATH, localCache.getAbsolutePath());
        cv.put(COLUMN_FILE_SIZE, localCache.length());
        cv.put(COLUMN_LAST_ACCESSED_TIME, String.valueOf(time));
        long result = db.replace(TABLE_NAME, null, cv);
        if (result == -1) {
            Log.e("ImageCacheDB", "insert error");
        }
    }

    public void execDelete() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    /**
     * 查询图片的缓存路径 没有返回null
     */
    public String execSelect(String url) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + COLUMN_URL + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{url});
        if (cursor.getCount() != 0) {
            String path = null;
            if (cursor.moveToFirst()) {
                do {
                    path = cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
                } while (cursor.moveToNext());
            }
            cursor.close();
//			ContentValues cv = new ContentValues();
//			cv.put(COLUMN_URL, url);//主键 用来判断更新的行位置
//			cv.put(COLUMN_LAST_ACCESSED_TIME, String.valueOf(time));
//			db.replace(TABLE_NAME, null, cv);
            execUpdate();
            return path;
        } else {
            cursor.close();
        }
        return null;
    }

    /**
     * 更新最后一次读取图片缓存的时间
     */
    public void execUpdate() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "update " + TABLE_NAME + " set "
                + COLUMN_LAST_ACCESSED_TIME + " = ? where " + COLUMN_URL + " = ?";
        long time = System.currentTimeMillis();
        db.execSQL(sql, new String[]{String.valueOf(time)});
    }

    private void trimToSize() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select sum(" + COLUMN_FILE_SIZE + ") from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        Long totel = (long) 0;
        if (cursor.moveToFirst())
            totel = cursor.getLong(0);
        cursor.close();
        if (totel / 1024 / 1024 > CACHE_SIZE) {
            String queryUrl = "select " + COLUMN_URL + "," + COLUMN_PATH + " from " + TABLE_NAME + " order by" + COLUMN_LAST_ACCESSED_TIME + " asc limit 30";
            Cursor queryCursor = db.rawQuery(queryUrl, null);
            if (queryCursor.moveToFirst()) {
                ArrayList<String> deleteUrls = new ArrayList<>();
                do {
                    String url = queryCursor.getString(queryCursor.getColumnIndex(COLUMN_URL));
                    String path = queryCursor.getString(queryCursor.getColumnIndex(COLUMN_PATH));
                    deleteUrls.add(url);
                    new File(path).delete();
                } while (queryCursor.moveToNext());

                int count = deleteUrls.size();
                if (count > 0) {
                    StringBuilder deleteSql = new StringBuilder();
                    deleteSql.append("delete from " + TABLE_NAME + " where " + COLUMN_URL + " in ( ");

                    for (int i = 0; i < count; i++) {
                        if (i == 0) {
                            deleteSql.append(deleteUrls.get(i));
                        } else {
                            deleteSql.append(",");
                            deleteSql.append(deleteUrls.get(i));
                        }
                    }
                    deleteSql.append(" ) ");
                    db.execSQL(deleteSql.toString());
                }
            }
        }
    }
}
