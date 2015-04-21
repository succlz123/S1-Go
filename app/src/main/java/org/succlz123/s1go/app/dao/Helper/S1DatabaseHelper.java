package org.succlz123.s1go.app.dao.Helper;

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
public class S1DatabaseHelper extends SQLiteOpenHelper {

    private static S1DatabaseHelper instance;

    private LoginVariables loginVariables;

    public static synchronized S1DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new S1DatabaseHelper(S1GoApplication.getInstance().getApplicationContext(), "s1.db", null, 1);
        }
        return instance;
    }

    private static String USER_INFO = "create table userinfo ("
            + "auth text primary key,"
            + "cookiepre text,"
            + "formhash text,"
            + "groupid text,"
            + "member_uid text,"
            + "member_username text,"
            + "readaccess text,"
            + "saltkey text)";

    public S1DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(LoginVariables loginVariables) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("auth", loginVariables.getAuth());
        contentValues.put("cookiepre", loginVariables.getCookiepre());
        contentValues.put("formhash", loginVariables.getFormhash());
        contentValues.put("groupid", loginVariables.getGroupid());
        contentValues.put("member_uid", loginVariables.getMember_uid());
        contentValues.put("member_username", loginVariables.getMember_username());
        contentValues.put("readaccess", loginVariables.getReadaccess());
        contentValues.put("saltkey", loginVariables.getSaltkey());
        sqLiteDatabase.delete("userinfo", null, null);
        sqLiteDatabase.replace("userinfo", null, contentValues);
    }

    public void delete() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("userinfo", null, null);
    }

    public LoginVariables get() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("userinfo", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            LoginVariables loginVariables = new LoginVariables();
            loginVariables.setAuth(cursor.getString(cursor.getColumnIndex("auth")));
            loginVariables.setCookiepre(cursor.getString(cursor.getColumnIndex("cookiepre")));
            loginVariables.setFormhash(cursor.getString(cursor.getColumnIndex("formhash")));
            loginVariables.setGroupid(cursor.getString(cursor.getColumnIndex("groupid")));
            loginVariables.setMember_uid(cursor.getString(cursor.getColumnIndex("member_uid")));
            loginVariables.setMember_username(cursor.getString(cursor.getColumnIndex("member_username")));
            loginVariables.setReadaccess(cursor.getString(cursor.getColumnIndex("readaccess")));
            loginVariables.setSaltkey(cursor.getString(cursor.getColumnIndex("saltkey")));

            return loginVariables;
        } else
            return null;
    }
}
