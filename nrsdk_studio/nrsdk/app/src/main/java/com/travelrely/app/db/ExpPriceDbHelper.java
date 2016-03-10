package com.travelrely.app.db;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.v2.model.ExpressPrice;

public class ExpPriceDbHelper
{
    static String tableName = "express_price";
    
    public static String createTable()
    {
        String table = "CREATE TABLE if not exists " + tableName + " ("
                + "id integer primary key autoincrement,"
                + "state_name text,"
                + "express_price text,"
                + "currency_unit integer"
                + ")";
        return table;
    }
    
    private static ExpPriceDbHelper instance;

    private ExpPriceDbHelper()
    {
        
    }

    public static ExpPriceDbHelper getInstance()
    {
        if (instance == null)
        {
            instance = new ExpPriceDbHelper();
        }
        return instance;
    }
    
    public void insertAll(List<ExpressPrice> pkgs)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        
        db.beginTransaction();
        db.execSQL("drop table if EXISTS " + tableName);
        db.execSQL(createTable());

        for (ExpressPrice pkg : pkgs)
        {
            db.insert(tableName, null, pkg.getContentValues());
        }
        
        db.setTransactionSuccessful();
        db.endTransaction();
        
        ComDbManager.getInstance().closeDb();
    }

    public long cover(ExpressPrice pkg)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        
        // 先删除已有记录
        String sql = "state_name like ?";
        db.delete(tableName, sql, new String[] {pkg.getStateName() + "%"});
        
        long id = db.insert(tableName, null, pkg.getContentValues());
        ComDbManager.getInstance().closeDb();
        return id;
    }
    
    public ExpressPrice query(String state_name)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        
        String sql = "select * from " + tableName
                + " where state_name like ?";
        Cursor cursor = db.rawQuery(sql,
                new String[] {state_name + "%"});
        if (cursor == null)
        {
            ComDbManager.getInstance().closeDb();
            return null;
        }

        ExpressPrice profile = new ExpressPrice();

        while (cursor.moveToNext())
        {
            profile.setCursorValue(cursor);
        }
        cursor.close();

        ComDbManager.getInstance().closeDb();

        return profile;
    }
    
    public float getPrice(String state)
    {
        ExpressPrice ep = query(state);
        if (ep == null)
        {
            return 0;
        }
        
        return ep.getExpressPrice();
    }
}
