package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.v2.model.SmsSeg;

public class SmsSegDbHelper
{
    static String tableName = "sms_segment";
    
    public static String createTable()
    {
        String table = "CREATE TABLE if not exists " + tableName + " ("
                + "id integer primary key autoincrement,"
                + "peer text,"
                + "xx integer,"
                + "num integer,"
                + "sn integer,"
                + "len integer,"
                + "content text" + ")";
        return table;
    }
    
    private static SmsSegDbHelper instance;

    private SmsSegDbHelper()
    {
        
    }

    public static SmsSegDbHelper getInstance()
    {
        if (instance == null)
        {
            instance = new SmsSegDbHelper();
        }
        return instance;
    }

    public long insert(SmsSeg smsSeg)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        long id = db.insert(tableName, null, smsSeg.getContentValues());
        UserDbManager.getInstance().closeDb();
        return id;
    }
    
    public List<SmsSeg> query(String peer, String xx)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        String sql = "select * from " + tableName
                + " where peer=? and xx=? order by sn";
        Cursor cursor = db.rawQuery(sql, new String[] {peer, xx});
        if (cursor == null)
        {
            UserDbManager.getInstance().closeDb();
            return null;
        }

        List<SmsSeg> list = new ArrayList<SmsSeg>();
        
        while (cursor.moveToNext())
        {
            SmsSeg seg = new SmsSeg();
            seg.setCursorValue(cursor);
            list.add(seg);
        }
        cursor.close();

        UserDbManager.getInstance().closeDb();

        return list;
    }
    
    public void delet(String peer, String xx)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        String sql = "peer=? and xx=?";
        db.delete(tableName, sql, new String[] {peer, xx});
        
        UserDbManager.getInstance().closeDb();
    }
}
