package com.wp.demo.menupopup;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Created by wangpeng on 16-1-3.
 */
public class MyProvider extends ContentProvider {

    public final static String AUTHORITY = "com.wp.demo.authority";

    private final static int MESSAGE = 1;
    private final static int MESSAGE_ID = 2;
    private final static int MESSAGE_DATES = MESSAGE + 2;
    private final static int MESSAGE_DATES_ID = MESSAGE + 3;
    private final static int MESSAGE_TIMES = MESSAGE + 4;
    private final static int MESSAGE_TIMES_ID = MESSAGE + 5;
    private final static int MESSAGE_MINI_TYPES = MESSAGE + 6;
    private final static int MESSAGE_MINI_TYPES_ID = MESSAGE + 7;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, "/" + Message.TABLE_NAME, MESSAGE);
        sURIMatcher.addURI(AUTHORITY, "/" + Message.TABLE_NAME +
                "/#", MESSAGE_ID);
        sURIMatcher.addURI(AUTHORITY, "/" + Message.TABLE_NAME +
                "/#/" + Message.MESSAGE_DATA, MESSAGE_DATES);
        sURIMatcher.addURI(AUTHORITY, "/" + Message.TABLE_NAME +
                "/#/" + Message.MESSAGE_DATA + "/#", MESSAGE_DATES_ID);
        sURIMatcher.addURI(AUTHORITY, "/" + Message.TABLE_NAME +
                "/#/" + Message.TIME, MESSAGE_TIMES);
        sURIMatcher.addURI(AUTHORITY, "/" + Message.TABLE_NAME +
                "/#/" + Message.TIME + "/#", MESSAGE_TIMES_ID);
        sURIMatcher.addURI(AUTHORITY, "/" + Message.TABLE_NAME +
                "/#/" + Message.MINI_TYPE, MESSAGE_MINI_TYPES);
        sURIMatcher.addURI(AUTHORITY, "/" + Message.TABLE_NAME +
                "/#/" + Message.MINI_TYPE + "/#", MESSAGE_MINI_TYPES_ID);
    }

    private SQLiteOpenHelper mDb;

    @Override
    public boolean onCreate() {
        mDb = new MyDatabase(getContext());
        return true;
    }

    private synchronized SQLiteOpenHelper getDb() {
        return mDb;
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        int match = sURIMatcher.match(uri);
        SQLiteDatabase db = getDb().getWritableDatabase();
        Cursor retCur = null;
        switch (match) {
            case MESSAGE:
                retCur = db.query(Message.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        return retCur;
    }

    @Override
    public String getType(Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match) {
            case MESSAGE:
                return "vnd.android.cursor.dir/" + Message.TABLE_NAME;
            case MESSAGE_ID:
                return "vnd.android.cursor.item/" + Message.TABLE_NAME;
            case MESSAGE_DATES:
                return "vnd.android.cursor.dir/" + Message.TABLE_NAME;
            case MESSAGE_DATES_ID:
                return "vnd.android.cursor.item/" + Message.TABLE_NAME;
            case MESSAGE_TIMES:
                return "vnd.android.cursor.dir/" + Message.TABLE_NAME;
            case MESSAGE_TIMES_ID:
                return "vnd.android.cursor.item/" + Message.TABLE_NAME;
            case MESSAGE_MINI_TYPES:
                return "vnd.android.cursor.dir/" + Message.TABLE_NAME;
            case MESSAGE_MINI_TYPES_ID:
                return "vnd.android.cursor.item/" + Message.TABLE_NAME;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = sURIMatcher.match(uri);
        Uri retUri = null;
        SQLiteDatabase db = getDb().getWritableDatabase();
        switch (match) {
            case MESSAGE:
                db.insert(Message.TABLE_NAME, null, values);
                break;
        }
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sURIMatcher.match(uri);
        SQLiteDatabase db = getDb().getWritableDatabase();
        switch (match) {
            case MESSAGE:
                return db.delete(Message.TABLE_NAME, selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        int match = sURIMatcher.match(uri);
        SQLiteDatabase db = getDb().getWritableDatabase();
        switch (match) {
            case MESSAGE:
                return db.update(Message.TABLE_NAME, values, selection, selectionArgs);
        }
        return 0;
    }
}
