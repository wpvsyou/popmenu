package com.wp.demo.menupopup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangpeng on 16-1-3.
 */
public class MyDatabase extends SQLiteOpenHelper {

    private final static String NAME = "MyDatabase";
    private final static int VERSION = 1;

    public MyDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String sql = "CREATE TABLE IF NOT EXISTS " +
                Message.TABLE_NAME + "(" +
                Message._ID + " ID INTEGER PRIMARY KEY, " +
                Message.MESSAGE_DATA + " TEXT, " +
                Message.TIME + " TEXT, " +
                Message.MINI_TYPE + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
