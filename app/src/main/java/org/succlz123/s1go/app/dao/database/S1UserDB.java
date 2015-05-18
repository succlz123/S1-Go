package org.succlz123.s1go.app.dao.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

    private static final String USER_INFO = "create table " + TABLE_NAME + " ("
            + COLUMN_AUTH + " TEXT primary key,"
            + COLUMN_COOKIEPRE + " TEXT,"
            + COLUMN_FORMHASH + " TEXT,"
            + COLUMN_GROUPID + " TEXT,"
            + COLUMN_MEMBER_UID + " TEXT,"
            + COLUMN_MEMBER_USERNAME + " TEXT,"
            + COLUMN_READACCESS + " TEXT,"
            + COLUMN_SALTKEY + " TEXT)";

    private LoginVariables loginVariables;

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

    public void execInsert(LoginVariables loginVariables) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AUTH, loginVariables.getAuth());
        contentValues.put(COLUMN_COOKIEPRE, loginVariables.getCookiepre());
        contentValues.put(COLUMN_FORMHASH, loginVariables.getFormhash());
        contentValues.put(COLUMN_GROUPID, loginVariables.getGroupid());
        contentValues.put(COLUMN_MEMBER_UID, loginVariables.getMember_uid());
        contentValues.put(COLUMN_MEMBER_USERNAME, loginVariables.getMember_username());
        contentValues.put(COLUMN_READACCESS, loginVariables.getReadaccess());
        contentValues.put(COLUMN_SALTKEY, loginVariables.getSaltkey());
        sqLiteDatabase.replace(TABLE_NAME, null, contentValues);
    }

    public void execDelete() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
    }

    public LoginVariables execSelect() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            LoginVariables loginVariables = new LoginVariables();
            loginVariables.setAuth(cursor.getString(cursor.getColumnIndex(COLUMN_AUTH)));
            loginVariables.setCookiepre(cursor.getString(cursor.getColumnIndex(COLUMN_FORMHASH)));
            loginVariables.setFormhash(cursor.getString(cursor.getColumnIndex(COLUMN_FORMHASH)));
            loginVariables.setGroupid(cursor.getString(cursor.getColumnIndex(COLUMN_GROUPID)));
            loginVariables.setMember_uid(cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_UID)));
            loginVariables.setMember_username(cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_USERNAME)));
            loginVariables.setReadaccess(cursor.getString(cursor.getColumnIndex(COLUMN_READACCESS)));
            loginVariables.setSaltkey(cursor.getString(cursor.getColumnIndex(COLUMN_SALTKEY)));

            return loginVariables;
        } else
            return null;
    }
}
