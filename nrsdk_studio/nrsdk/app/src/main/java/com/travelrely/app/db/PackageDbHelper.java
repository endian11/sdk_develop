package com.travelrely.app.db;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.v2.model.Package0;

public class PackageDbHelper
{
    static String tableName = "package";
    
    public static String createTable()
    {
        String table = "CREATE TABLE if not exists " + tableName + " ("
                + "id integer primary key autoincrement,"
                + "mcc text,"
                + "mnc text,"
                + "days integer,"
                + "price text,"
                + "data text,"
                + "localvoice text,"
                + "iddvoice text" + ")";
        return table;
    }
    
    private static PackageDbHelper instance;

    private PackageDbHelper()
    {
        
    }

    public static PackageDbHelper getInstance()
    {
        if (instance == null)
        {
            instance = new PackageDbHelper();
        }
        return instance;
    }
    
    public void insertAll(List<Package0> pkgs)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        
        db.execSQL("drop table if EXISTS " + tableName);
        db.execSQL(createTable());

        for (Package0 pkg : pkgs)
        {
            db.insert(tableName, null, pkg.getContentValues());
        }
        
        ComDbManager.getInstance().closeDb();
    }

    public long insert(Package0 pkg)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        
        long id = db.insert(tableName, null, pkg.getContentValues());
        ComDbManager.getInstance().closeDb();
        return id;
    }
    
    public Package0 query(String mcc, String mnc, String days)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        
        String sql = "select * from " + tableName
                + " where mcc=? and mnc=? and days=?";
        Cursor cursor = db.rawQuery(sql, new String[] {mcc, mnc, days});
        if (cursor == null)
        {
            ComDbManager.getInstance().closeDb();
            return null;
        }

        Package0 profile = new Package0();

        while (cursor.moveToNext())
        {
            profile.setCursorValue(cursor);
        }
        cursor.close();

        ComDbManager.getInstance().closeDb();

        return profile;
    }
}
