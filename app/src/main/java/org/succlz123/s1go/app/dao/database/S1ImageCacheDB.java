package org.succlz123.s1go.app.dao.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.succlz123.s1go.app.S1GoApplication;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by fashi on 2015/4/25.
 */
public class S1ImageCacheDB extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "imagecache.db";
    private static final String TABLE_NAME = "imagecache";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_FILE_SIZE = "file_size";
    private static final String COLUMN_LAST_ACCESSED_TIME = "last_accessed_time";
    private static final long CACHE_SIZE = 100;//max cache size is 100M

    private static S1ImageCacheDB instance;

    public synchronized static S1ImageCacheDB getInstance() {
        if (instance == null) {
            instance = new S1ImageCacheDB(S1GoApplication.getInstance().getApplicationContext(), DB_NAME, null, VERSION);
        }
        return instance;
    }

    private static final String IMAGE_CACHE = "create table if not exists" + TABLE_NAME + " ("
            + COLUMN_URL + " TEXT primary key,"
            + COLUMN_PATH + " TEXT,"
            + COLUMN_FILE_SIZE + " INTEGER,"
            + COLUMN_LAST_ACCESSED_TIME + " INTEGER"
            + ")";

    private S1ImageCacheDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IMAGE_CACHE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertCache(String url, File localCache) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long time = System.currentTimeMillis();
        contentValues.put(COLUMN_URL, url);
        contentValues.put(COLUMN_PATH, localCache.getAbsolutePath());
        contentValues.put(COLUMN_FILE_SIZE, localCache.length());
        contentValues.put(COLUMN_LAST_ACCESSED_TIME, String.valueOf(time));
        long result = sqLiteDatabase.replace(TABLE_NAME, null, contentValues);
        if (result == -1) {
            Log.e("S1ImageCacheDB", "insert error");
        }

    }

    public String getCache(String url) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String sql = "select * from " + TABLE_NAME + " where " + COLUMN_URL + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{url});
        if (cursor.getCount() != 0) {
            String path = null;
            if (cursor.moveToFirst()) {
                do {
                    path = cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
                } while (cursor.moveToNext());
            }
            cursor.close();
            long time = System.currentTimeMillis();
            contentValues.put(COLUMN_URL, url);//主键 用来判断更新的行  位置
            contentValues.put(COLUMN_LAST_ACCESSED_TIME, String.valueOf(time));
            sqLiteDatabase.replace(TABLE_NAME, null, contentValues);
            return path;
        } else {
            cursor.close();
        }
        return null;
    }

    public void clearCache() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
    }

    private void trimToSize() {
        SQLiteDatabase sqLiteDatabase = instance.getWritableDatabase();
        String sql = "sum(" + COLUMN_FILE_SIZE + ") from " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        Long totel = cursor.getLong(0);
        cursor.close();
        if (totel / 1024 / 1024 > CACHE_SIZE) {
            String queryUrl = "select " + COLUMN_URL + "," + COLUMN_PATH + " from " + TABLE_NAME + " order by" + COLUMN_LAST_ACCESSED_TIME + " asc limit 30";
            Cursor queryCursor = sqLiteDatabase.rawQuery(queryUrl, null);
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
                    sqLiteDatabase.execSQL(deleteSql.toString());
                }
            }
        }
    }
}
