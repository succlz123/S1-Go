package org.succlz123.s1go.database;

import org.succlz123.s1go.MainApplication;
import org.succlz123.s1go.bean.UserInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by succlz123 on 2015/4/18.
 */
public class UserDatabase extends SQLiteOpenHelper {
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

    private UserDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static UserDatabase getInstance() {
        return HelpHolder.INSTANCE;
    }

    private static class HelpHolder {
        private static final UserDatabase INSTANCE = new UserDatabase(MainApplication.getInstance().getApplicationContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void execInsert(UserInfo userInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AUTH, userInfo.Variables.auth);
        cv.put(COLUMN_COOKIEPRE, userInfo.Variables.cookiepre);
        cv.put(COLUMN_FORMHASH, userInfo.Variables.formhash);
        cv.put(COLUMN_GROUPID, userInfo.Variables.groupid);
        cv.put(COLUMN_MEMBER_UID, userInfo.Variables.member_uid);
        cv.put(COLUMN_MEMBER_USERNAME, userInfo.Variables.member_username);
        cv.put(COLUMN_READACCESS, userInfo.Variables.readaccess);
        cv.put(COLUMN_SALTKEY, userInfo.Variables.saltkey);
        cv.put(COLUMN_PASSWORD, userInfo.Variables.password);
        db.replace(TABLE_NAME, null, cv);
    }

    public void execDelete() {
        SQLiteDatabase db = getWritableDatabase();
        int number = db.delete(TABLE_NAME, null, null);
        Log.e("user database delete : ", "" + number);
    }

    public UserInfo.Variables execSelect() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        UserInfo.Variables loginInfo = null;

        if (cursor.moveToFirst()) {
            loginInfo = new UserInfo.Variables();
            loginInfo.auth = (cursor.getString(cursor.getColumnIndex(COLUMN_AUTH)));
            loginInfo.cookiepre = (cursor.getString(cursor.getColumnIndex(COLUMN_COOKIEPRE)));
            loginInfo.formhash = (cursor.getString(cursor.getColumnIndex(COLUMN_FORMHASH)));
            loginInfo.groupid = (cursor.getString(cursor.getColumnIndex(COLUMN_GROUPID)));
            loginInfo.member_uid = (cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_UID)));
            loginInfo.member_username = (cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_USERNAME)));
            loginInfo.readaccess = (cursor.getString(cursor.getColumnIndex(COLUMN_READACCESS)));
            loginInfo.saltkey = (cursor.getString(cursor.getColumnIndex(COLUMN_SALTKEY)));
            loginInfo.password = (cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            cursor.close();
        }
        return loginInfo;
    }
}
