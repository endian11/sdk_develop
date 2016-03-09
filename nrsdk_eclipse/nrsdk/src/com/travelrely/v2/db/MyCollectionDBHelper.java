package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.Engine;
import com.travelrely.v2.model.MyCollection;

public class MyCollectionDBHelper {
    
    static String tableNnam = "collection_info";
    
    public static String createTable() {

        String table = "CREATE TABLE if not exists " + tableNnam + " ("
                + "id integer primary key autoincrement," + "from_name text," + "type integer,"
                + "type_id text," + "time datetime," + "file_name text," + "mark text," + "other text" + ")";
        return table;
    }
    
    private static MyCollectionDBHelper myCollectionDBHelper;

    public static MyCollectionDBHelper getInstance() {

        if (myCollectionDBHelper == null) {
            myCollectionDBHelper = new MyCollectionDBHelper();
        }
        return myCollectionDBHelper;
    }
    
    public synchronized long addCollection(MyCollection mCollection) {

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(),Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        long row = sqLiteDatabase.insert(tableNnam, null, mCollection.generateValues());
        sqLiteDatabase.close();
        return row;
    }
    
    public synchronized boolean isCollection(String name) {

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam + " where type_id = ? ";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                name
        });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    return true;
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return false;
    }

    public List<MyCollection> query()
    {
        UserDBOpenHelper traOpenHelper;
        SQLiteDatabase sqLiteDatabase;
        Cursor cursor;

        MyCollection mCollection;
        List<MyCollection> list;

        list = new ArrayList<MyCollection>();

        traOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        sqLiteDatabase = traOpenHelper.getReadableDatabase();

        cursor = sqLiteDatabase.query(tableNnam, null, null, null,
                null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                mCollection = new MyCollection();
                mCollection.setValue(cursor);

                list.add(mCollection);
            }

            cursor.close();
        }

        sqLiteDatabase.close();

        return list;
    }
}
