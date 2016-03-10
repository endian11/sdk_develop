package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.v2.model.ServerIp;

public class ServerIpDbHelper
{
    static String tableName = "server_ip";
    
    public static String createTable()
    {
        String table = "CREATE TABLE if not exists " + tableName + " ("
                + "id integer primary key autoincrement,"
                + "mcc text,"
                + "port integer,"
                + "ip text" + ")";
        return table;
    }
    
    private static ServerIpDbHelper instance;

    private ServerIpDbHelper()
    {
        
    }

    public static ServerIpDbHelper getInstance()
    {
        if (instance == null)
        {
            instance = new ServerIpDbHelper();
        }
        return instance;
    }
    
    public void insertAll(List<ServerIp> pkgs)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        db.execSQL("drop table if EXISTS " + tableName);
        db.execSQL(createTable());

        for (ServerIp pkg : pkgs)
        {
            db.insert(tableName, null, pkg.getContentValues());
        }
        
        UserDbManager.getInstance().closeDb();
    }

    public long insert(ServerIp pkg)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        long id = db.insert(tableName, null, pkg.getContentValues());
        UserDbManager.getInstance().closeDb();
        return id;
    }
    
    public List<ServerIp> query()
    {
        List<ServerIp> list = new ArrayList<ServerIp>();
        
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                ServerIp ip = new ServerIp();
                ip.setCursorValue(cursor);

                list.add(ip);
            }

            cursor.close();
        }

        UserDbManager.getInstance().closeDb();

        return list;
    }
}
