package org.succlz123.s1go.app.dao.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.bean.login.LoginVariables;

/**
 * Created by fashi on 2015/4/18.
 */
public class S1UserDB extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "userinfo.db";
    private static final String TABLE_NAME = "userinfo";
    private static final String COLUMN_AUTH = "auth";
    private static final String COLUMN_COOKIEPRE = "cookiepre";
    private static final String COLUMN_FORMHASH = "formhash";
    private static final String COLUMN_GROUPID = "groupid";
    private static final String COLUMN_MEMBER_UID = "member_uid";
    private static final String COLUMN_MEMBER_USERNAME = "member_username";
    private static final String COLUMN_READACCESS = "readaccess";
    private static final String COLUMN_SALTKEY = "saltkey";
    private static final String COLUMN_PASSWORD = "password";

    private static final String USER_INFO = "create table " + TABLE_NAME + " ("
            + COLUMN_AUTH + " TEXT,"
            + COLUMN_COOKIEPRE + " TEXT,"
            + COLUMN_FORMHASH + " TEXT,"
            + COLUMN_GROUPID + " TEXT,"
            + COLUMN_MEMBER_UID + " TEXT primary key,"
            + COLUMN_MEMBER_USERNAME + " TEXT,"
            + COLUMN_READACCESS + " TEXT,"
            + COLUMN_SALTKEY + " TEXT,"
            + COLUMN_PASSWORD + " TEXT"
            + ")";

    private static S1UserDB instance;

    public static synchronized S1UserDB getInstance() {
        if (instance == null) {
            instance = new S1UserDB(S1GoApplication.getInstance().getApplicationContext(), DB_NAME, null, VERSION);
        }
        return instance;
    }

    private S1UserDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void execInsert(LoginVariables userInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AUTH, userInfo.getAuth());
        cv.put(COLUMN_COOKIEPRE, userInfo.getCookiepre());
        cv.put(COLUMN_FORMHASH, userInfo.getFormhash());
        cv.put(COLUMN_GROUPID, userInfo.getGroupid());
        cv.put(COLUMN_MEMBER_UID, userInfo.getMember_uid());
        cv.put(COLUMN_MEMBER_USERNAME, userInfo.getMember_username());
        cv.put(COLUMN_READACCESS, userInfo.getReadaccess());
        cv.put(COLUMN_SALTKEY, userInfo.getSaltkey());
        cv.put(COLUMN_PASSWORD, userInfo.getPassword());
        db.insert(TABLE_NAME, null, cv);
    }

    public void execDelete() {
        SQLiteDatabase db = getWritableDatabase();
        int number = db.delete(TABLE_NAME, null, null);
        Log.e("S1UserDBexecDelete", "" + number);
    }

    public LoginVariables execSelect() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            LoginVariables userInfo = new LoginVariables();
            userInfo.setAuth(cursor.getString(cursor.getColumnIndex(COLUMN_AUTH)));
            userInfo.setCookiepre(cursor.getString(cursor.getColumnIndex(COLUMN_FORMHASH)));
            userInfo.setFormhash(cursor.getString(cursor.getColumnIndex(COLUMN_FORMHASH)));
            userInfo.setGroupid(cursor.getString(cursor.getColumnIndex(COLUMN_GROUPID)));
            userInfo.setMember_uid(cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_UID)));
            userInfo.setMember_username(cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_USERNAME)));
            userInfo.setReadaccess(cursor.getString(cursor.getColumnIndex(COLUMN_READACCESS)));
            userInfo.setSaltkey(cursor.getString(cursor.getColumnIndex(COLUMN_SALTKEY)));
            userInfo.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            cursor.close();
            return userInfo;
        } else
            return null;
    }
}
