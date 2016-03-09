package com.travelrely.v2.db;

import com.travelrely.core.nrs.Engine;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/** 
 * 
 * @author zhangyao
 * @version 2014年10月31日上午11:09:50
 */

public class CallLogContentProvider extends ContentProvider{

    public static final String TABLE_NAME = "call_record";
//    public static final String AUTHORITY = CallLogContentProvider.class
//        .getCanonicalName();
    public static final String AUTHORITY = "com.travelrely.sdk";
    
    private static final int ITEM = 1;
    private static final int ITEM_ID = 2;
    
    public static final Uri CONTENT_URI = Uri.parse("content://com.travelrely.sdk" + "/" + TABLE_NAME);
    
    private static UriMatcher sUriMatcher;
    private UserDBOpenHelper dbHelper;
    
    static{
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, ITEM);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", ITEM_ID);
    }
    
    
    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        dbHelper = new UserDBOpenHelper(getContext(), "");
        
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // TODO Auto-generated method stub
        ContentValues values;
        if (initialValues != null) {
          values = new ContentValues(initialValues);
        } else {
          values = new ContentValues();
        }
        String table = null;
        String nullableCol = null;

        switch (sUriMatcher.match(uri)) {
        case ITEM:
          table = TABLE_NAME;
          break;
        default:
          new RuntimeException("Invalid URI for inserting: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(table, nullableCol, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(uri, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }


}
